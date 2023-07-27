package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users WHERE role_id=?1", nativeQuery = true)
    List<User> findAllByRoleId(int theId);

    @Query(value = "SELECT * FROM users u WHERE email=?1", nativeQuery = true)
    User findUserByEmail(String email);

    @Query(value = "SELECT * FROM users u JOIN " +
            "(SELECT user_id, company_id from apply_posts a JOIN recruitments r ON a.recruitment_id = r.id " +
            "WHERE company_id = ?1 GROUP BY user_id, company_id) a " +
            "ON u.id = a.user_id", nativeQuery = true)
    Page<User> findAllCandidateByCompanyId(int companyId, PageRequest pageable);
}
