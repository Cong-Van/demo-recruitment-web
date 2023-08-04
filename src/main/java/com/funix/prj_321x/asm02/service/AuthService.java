package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AuthService {

    Page<User> findCandidatePagination(int companyId, int pageNo, int pageSize);

    User getUserByEmail(String email);

    void setUserInf(User user, UserDTO userDTO);

    void saveUser(User user);

    User getUserById(int id);

    Company getCompanyByUserId(int userId);

    void setCompanyInf(Company company, CompanyDTO companyDTO);

    void saveCompany(Company company);

    void saveRecruitment(Recruitment newRecruitment);

    Page<Recruitment> findRecruitmentManagePagination(int companyId, int pageNo, int pageSize);

    Recruitment getRecruitmentById(int id);

    void updateRecruitment(Recruitment recruitmentUpdate, Recruitment recruitment);

    void deleteRecruitment(Recruitment recruitment);

    List<Category> getAllCategory();

    Category getCategoryById(int id);

    void saveCategory(Category category);

    ApplyPost getApplyPostById(int id);

    void saveApplyPost(ApplyPost applyPost);

    void sendEmailToVerify(String email, String confirmUrl) throws MessagingException, UnsupportedEncodingException;

    void verifyAccount(String email);
}
