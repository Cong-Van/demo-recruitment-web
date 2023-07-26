package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommonService {

    List<Role> getAllRoles();

    List<User> getAllCandidate();

    User getUserById(int id);

    User getUserByEmail(String email);

    void setUserDTOInf(UserDTO userDTO, User user);

    void saveUser(User user);

    void save(UserDTO userDTO);

    List<Company> getAllCompany();

    List<Company> getTopCompanies();

    Company getCompanyById(int id);

    Company getCompanyByUserId(int UserId);

    void setCompanyDTOInf(CompanyDTO companyDTO, Company company);

    void saveCompany(Company company);

    List<Recruitment> getAllRecruitment();

    List<Recruitment> getTopRecruitment();

    Page<Recruitment> findRecruitmentPagination(int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentByCategoryPagination(int categoryId, int pageNo, int pageSize);

    Recruitment getRecruitmentById(int id);

    List<Recruitment> getAllByTitle(String title);

    List<Category> getTopCategories();

    Category getCategoryById(int id);

    Cv getCvByUserId(int UserId);

    void saveCv(Cv cv);
}
