package com.funix.prj_321x.asm02.security;

import com.funix.prj_321x.asm02.entity.User;
import com.funix.prj_321x.asm02.service.CommonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    private CommonService commonService;

    public SecurityConfig(CommonService commonService) {
        this.commonService = commonService;
    }

    // Add support for JDBC

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, status FROM users WHERE email = ?"
        );

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email, r.role_name FROM roles r JOIN users u ON u.role_id = r.id WHERE u.email=?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/", "/register", "/assets/**", "/auth/verifyAccount/**").permitAll()
                                .requestMatchers("/profile").hasAnyAuthority("Công ty", "Ứng cử viên")
                                .requestMatchers("/recruitment/**").hasAnyAuthority("Công ty", "Ứng cử viên")
                                .requestMatchers("/company/**").hasAnyAuthority("Công ty", "Ứng cử viên")
                                .requestMatchers("/auth/**").hasAuthority("Công ty")
                                .requestMatchers("/candidate/**").hasAuthority("Ứng cử viên")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showLoginPage")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler((request, response, authentication) -> {
                                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                    String email = userDetails.getUsername();

                                    // Lấy thông tin người dùng từ CSDL theo email và password để đưa vào session
                                    User user = commonService.getUserByEmail(email);

                                    HttpSession session = request.getSession();
                                    session.setAttribute("user", user);
                                    response.sendRedirect("/home");
                                })
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}
