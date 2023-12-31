package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DAO.*;
import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class AuthServiceImp implements AuthService{

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private RecruitmentRepository recruitmentRepository;
    private CategoryRepository categoryRepository;
    private ApplyPostRepository applyPostRepository;
    private JavaMailSender javaMailSender;

    public AuthServiceImp(UserRepository userRepository, CompanyRepository companyRepository,
                          RecruitmentRepository recruitmentRepository, CategoryRepository categoryRepository,
                          ApplyPostRepository applyPostRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.categoryRepository = categoryRepository;
        this.applyPostRepository = applyPostRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Page<User> findCandidatePagination(int companyId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAllCandidateByCompanyId(companyId, pageable);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void setUserInf(User user, UserDTO userDTO) {
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDescription(userDTO.getDescription());
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Company getCompanyByUserId(int userId) {
        return companyRepository.findByUserId(userId);
    }

    @Override
    public void setCompanyInf(Company company, CompanyDTO companyDTO) {
        company.setEmail(companyDTO.getEmail());
        company.setCompanyName(companyDTO.getCompanyName());
        company.setAddress(companyDTO.getAddress());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setDescription(companyDTO.getDescription());
    }

    @Override
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void saveRecruitment(Recruitment newRecruitment) {
        recruitmentRepository.save(newRecruitment);
    }

    @Override
    public Page<Recruitment> findRecruitmentManagePagination(int companyId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCompanyId(companyId, pageable);
    }

    @Override
    public Recruitment getRecruitmentById(int id) {
        return recruitmentRepository.findById(id).get();
    }

    @Override
    public void updateRecruitment(Recruitment recruitmentUpdate, Recruitment recruitment) {
        recruitmentUpdate.setTitle(recruitment.getTitle());
        recruitmentUpdate.setDescription(recruitment.getDescription());
        recruitmentUpdate.setExperience(recruitment.getExperience());
        recruitmentUpdate.setQuantity(recruitment.getQuantity());
        recruitmentUpdate.setAddress(recruitment.getAddress());
        recruitmentUpdate.setDeadline(recruitment.getDeadline());
        recruitmentUpdate.setSalary(recruitment.getSalary());
        recruitmentUpdate.setType(recruitment.getType());
        recruitmentUpdate.setCategory(recruitment.getCategory());

        recruitmentRepository.save(recruitmentUpdate);
    }

    @Override
    public void deleteRecruitment(Recruitment recruitment) {
        recruitmentRepository.delete(recruitment);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public ApplyPost getApplyPostById(int id) {
        return applyPostRepository.findById(id).get();
    }

    @Override
    public void saveApplyPost(ApplyPost applyPost) {
        applyPostRepository.save(applyPost);
    }

    @Override
    public void sendEmailToVerify(String email, String confirmUrl) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String subject = "Verify employer account";
        String content = "<p>Hello,</p>" +
                "<p>Bạn đã tạo một tài khoản là nhà tuyển dụng trong trang web của chúng tôi. Bấm vào link dưới đây để xác thực: </p>" +
                "<p style=\"color: blue;\"><a href=\"" + confirmUrl + "\">Confirm account</a></p>";

        helper.setFrom("conglvfx20485@funix.edu.vn", "Admin");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
    }

    @Override
    public void verifyAccount(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setConfirmAccount(1);
        userRepository.save(user);
    }


}
