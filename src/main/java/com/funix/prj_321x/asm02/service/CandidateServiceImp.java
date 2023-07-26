package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DAO.*;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.ApplyPost;
import com.funix.prj_321x.asm02.entity.Company;
import com.funix.prj_321x.asm02.entity.Recruitment;
import com.funix.prj_321x.asm02.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImp implements CandidateService {

    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private RecruitmentRepository recruitmentRepository;
    private ApplyPostRepository applyPostRepository;
    private CVRepository cvRepository;

    public CandidateServiceImp(UserRepository userRepository, CompanyRepository companyRepository,
                               RecruitmentRepository recruitmentRepository, ApplyPostRepository applyPostRepository,
                               CVRepository cvRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.applyPostRepository = applyPostRepository;
        this.cvRepository = cvRepository;
    }

    @Override
    public List<User> getAllCandidate() {
        return userRepository.findAllByRoleId(2);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).get();
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
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(int id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public Page<Company> findCompanyFollowedPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return companyRepository.findCompanyByUserId(userId, pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentSavedPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findRecruitmentsSavedByUserId(userId, pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentManagePagination(int companyId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCompanyId(companyId, pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentPaginationSearchByTitle(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByTitlePagination(keySearch, pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentPaginationSearchByCompany(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCompanyPagination(keySearch, pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentPaginationSearchByAddress(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByAddressPagination(keySearch, pageable);
    }

    @Override
    public List<Recruitment> getAllRecruitment() {
        return recruitmentRepository.findAll();
    }

    @Override
    public Recruitment getRecruitmentById(int id) {
        return recruitmentRepository.findById(id).get();
    }

    @Override
    public void deleteRecruitment(Recruitment recruitment) {
        recruitmentRepository.delete(recruitment);
    }

    @Override
    public ApplyPost getApplyPostByUserIdAndRecruitmentId(int userId, int recruitmentId) {
        return applyPostRepository.findAllByUserIdAndRecruitmentId(userId, recruitmentId);
    }

    @Override
    public void saveApplyPost(ApplyPost applyPost) {
        applyPostRepository.save(applyPost);
    }

    @Override
    public Page<ApplyPost> findApplyPostPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return applyPostRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public ApplyPost getApplyPostById(int id) {
        return applyPostRepository.findById(id).get();
    }

    @Override
    public void deleteApplyPost(ApplyPost applyPost) {
        applyPostRepository.delete(applyPost);
    }

    @Override
    public void deleteCvById(int id) {
        cvRepository.deleteById(id);
    }
}
