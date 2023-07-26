package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DAO.*;
import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImp implements CommonService{

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private RecruitmentRepository recruitmentRepository;
    private CategoryRepository categoryRepository;
    private CVRepository cvRepository;

    public CommonServiceImp(RoleRepository roleRepository, UserRepository userRepository,
                            CompanyRepository companyRepository, RecruitmentRepository recruitmentRepository,
                            CategoryRepository categoryRepository, CVRepository cvRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.categoryRepository = categoryRepository;
        this.cvRepository = cvRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
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
    public void setUserDTOInf(UserDTO userDTO, User user) {
        userDTO.setId(user.getId());
        userDTO.setImage(user.getImage());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setDescription(user.getDescription());
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void save(UserDTO userDTO) {
        User user = new User();

        // Lấy thông tin từ UserDTO để lưu User mới
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    @Override
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getTopCompanies() {
        return companyRepository.findTopCompanies();
    }

    @Override
    public Company getCompanyById(int id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public Company getCompanyByUserId(int userId) {
        return companyRepository.findByUserId(userId);
    }

    @Override
    public void setCompanyDTOInf(CompanyDTO companyDTO, Company company) {
        companyDTO.setLogo(company.getLogo());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setCompanyName(company.getCompanyName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setDescription(company.getDescription());
    }

    @Override
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public List<Recruitment> getAllRecruitment() {
        return recruitmentRepository.findAll();
    }

    @Override
    public List<Recruitment> getTopRecruitment() {
        return recruitmentRepository.findTopRecruitments();
    }

    @Override
    public Page<Recruitment> findRecruitmentPagination(int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAll(pageable);
    }

    @Override
    public Page<Recruitment> findRecruitmentByCategoryPagination(int categoryId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCategoryId(categoryId, pageable);
    }

    @Override
    public Recruitment getRecruitmentById(int id) {
        return recruitmentRepository.findById(id).get();
    }

    @Override
    public List<Recruitment> getAllByTitle(String title) {
        return recruitmentRepository.findAllByTitle(title);
    }

    @Override
    public List<Category> getTopCategories() {
        return categoryRepository.findTopCategories();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Cv getCvByUserId(int userId) {
        return cvRepository.findCvByUserId(userId);
    }

    @Override
    public void saveCv(Cv cv) {
        cvRepository.save(cv);
    }
}
