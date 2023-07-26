package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.ApplyPost;
import com.funix.prj_321x.asm02.entity.Company;
import com.funix.prj_321x.asm02.entity.Recruitment;
import com.funix.prj_321x.asm02.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CandidateService {

    List<User> getAllCandidate();

    User getUserById(int id);

    User getUserByEmail(String email);

    void setUserInf(User user, UserDTO userDTO);

    void saveUser(User user);

    List<Company> getAllCompany();

    Company getCompanyById(int id);

    Page<Company> findCompanyFollowedPagination(int userId, int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentSavedPagination(int userId, int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentManagePagination(int companyId, int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentPaginationSearchByTitle(String keySearch, int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentPaginationSearchByCompany(String keySearch, int pageNo, int pageSize);

    Page<Recruitment> findRecruitmentPaginationSearchByAddress(String keySearch, int pageNo, int pageSize);

    List<Recruitment> getAllRecruitment();

    Recruitment getRecruitmentById(int id);

    void deleteRecruitment(Recruitment recruitment);

    ApplyPost getApplyPostByUserIdAndRecruitmentId(int userId, int recruitmentId);

    void saveApplyPost(ApplyPost applyPost);

    Page<ApplyPost> findApplyPostPagination(int userId, int pageNo, int pageSize);

    ApplyPost getApplyPostById(int id);

    void deleteApplyPost(ApplyPost applyPost);

    void deleteCvById(int id);
}
