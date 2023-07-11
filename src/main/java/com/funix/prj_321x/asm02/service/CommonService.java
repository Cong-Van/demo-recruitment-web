package com.funix.prj_321x.asm02.service;

import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.DAO.*;
import com.funix.prj_321x.asm02.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private CompanyRepository companyRepository;

    private CVRepository cvRepository;

    private CategoryRepository categoryRepository;

    private RecruitmentRepository recruitmentRepository;

    private ApplyPostRepository applyPostRepository;

    public CommonService(RoleRepository roleRepository, UserRepository userRepository,
                         CompanyRepository companyRepository, CVRepository cvRepository,
                         CategoryRepository categoryRepository, RecruitmentRepository recruitmentRepository,
                         ApplyPostRepository applyPostRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.cvRepository = cvRepository;
        this.categoryRepository = categoryRepository;
        this.recruitmentRepository = recruitmentRepository;
        this.applyPostRepository = applyPostRepository;
    }



    // Thao tác với User chung (cả HR và Candidate)
    public User getUserById(int theId) {
        return userRepository.findById(theId).get();
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    // Tạo mới người dùng
    public void save(UserDTO userDTO) {
        User user = new User();

        // Lấy thông tin từ UserDTO để lưu User mới
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        userRepository.save(user);
    }

    // Đưa thông tin của user vào userDTO
    public void setUserDTOInf(UserDTO userDTO, User user) {
        userDTO.setId(user.getId());
        userDTO.setImage(user.getImage());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setDescription(user.getDescription());
    }

    // Lấy thông tin của userDTO để cập nhật cho user
    public void setUserInf(User user, UserDTO userDTO) {
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDescription(userDTO.getDescription());
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Ứng viên (candidate) có roleId là 2
    public List<User> getAllCandidate() {
        return userRepository.findAllByRoleId(2);
    }

    // Các ứng viên đã ứng tuyển các bài đăng của công ty
    public Page<User> findCandidatePagination(int companyId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAllCandidateByCompanyId(companyId, pageable);
    }



    // Thao tác với Company
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(int theId) {
        return companyRepository.findById(theId).get();
    }

    // Lấy top 3 công ty có nhiều ứng cử viên ứng tuyển nhất
    public List<Company> getTopCompanies() {
        return companyRepository.findTopCompanies();
    }

    public Company getCompanyByUserId(int theId) {
        return companyRepository.findByUserId(theId);
    }

    // Đưa thông tin của company vào companyDTO
    public void setCompanyDTOInf(CompanyDTO companyDTO, Company company) {
        companyDTO.setLogo(company.getLogo());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setCompanyName(company.getCompanyName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setPhoneNumber(company.getPhoneNumber());
        companyDTO.setDescription(company.getDescription());
    }

    // Lấy thông tin của companyDTO để cập nhật cho company
    public void setCompanyInf(Company company, CompanyDTO companyDTO) {
        company.setEmail(companyDTO.getEmail());
        company.setCompanyName(companyDTO.getCompanyName());
        company.setAddress(companyDTO.getAddress());
        company.setPhoneNumber(companyDTO.getPhoneNumber());
        company.setDescription(companyDTO.getDescription());
    }

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    // Phân trang cho danh sách các company được follow, lấy từ userId của candidate
    public Page<Company> findCompanyFollowedPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return companyRepository.findCompanyByUserId(userId, pageable);
    }



    // Thao tác với các bài đăng tuyển dụng (Recruitment)
    public List<Recruitment> getAllRecruitment() {
        return recruitmentRepository.findAll();
    }

    // Lấy top 5 bài đăng có nhiều ứng cử viên apply nhất, lương cao nhất, số lượng tuyển nhiều nhất
    public List<Recruitment> getTopRecruitment() {
        return recruitmentRepository.findTopRecruitments();
    }

    // Phân trang cho tất cả các bài đăng công việc
    public Page<Recruitment> findRecruitmentPagination(int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAll(pageable);
    }

    // Phân trang cho các bài đăng theo loại công việc
    public Page<Recruitment> findRecruitmentByCategoryPagination(int categoryId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCategoryId(categoryId, pageable);
    }

    // Phân trang cho các bài đăng tuyển dụng của HR
    public Page<Recruitment> findRecruitmentManagePagination(int companyId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCompanyId(companyId, pageable);
    }

    // Phân trang cho các bài đăng tuyển dụng sau khi tìm kiếm
    public Page<Recruitment> findRecruitmentPaginationSearchByTitle(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByTitlePagination(keySearch, pageable);
    }

    public Page<Recruitment> findRecruitmentPaginationSearchByCompany(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByCompanyPagination(keySearch, pageable);
    }

    public Page<Recruitment> findRecruitmentPaginationSearchByAddress(String keySearch, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findAllByAddressPagination(keySearch, pageable);
    }

    // Phân trang các bài đăng đã lưu
    public Page<Recruitment> findRecruitmentSavedPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return recruitmentRepository.findRecruitmentsSavedByUserId(userId, pageable);
    }

    public List<Recruitment> getAllByTitle(String title) {
        return recruitmentRepository.findAllByTitle(title);
    }

    // Tìm kiếm, lưu, cập nhật, xóa bài đăng
    public Recruitment getRecruitmentById(int theId) {
        return recruitmentRepository.findById(theId).get();
    }

    public void saveRecruitment(Recruitment newRecruitment) {
        recruitmentRepository.save(newRecruitment);
    }

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

    public void deleteRecruitment(Recruitment recruitment) {
        recruitmentRepository.delete(recruitment);
    }



    // Thao tác với loại công việc (category)
    public List<Category> getTopCategories() {
        return categoryRepository.findTopCategories();
    }

    /*
    Cập nhật số lượng chọn công việc (category) khi HR tạo mới và xóa bài đăng
    Lấy ra loại công việc tương ứng
    Lưu lại
     */
    public Category getCategoryById(int theId) {
        return categoryRepository.findById(theId).get();
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }



    // Thao tác với hồ sơ (cv)
    public Cv getCvByUserId(int theId) {
        return cvRepository.findCvByUserId(theId);
    }

    public Cv getCvById(int theId) {
        return cvRepository.findById(theId).get();
    }

    public void saveCv(Cv cv) {
        cvRepository.save(cv);
    }

    public void deleteCvById(int theId) {
        cvRepository.deleteById(theId);
    }



    // Thao tác với các lượt ứng tuyển (apply post)
    public ApplyPost getApplyPostByUserIdAndRecruitmentId(int userId, int recruitmentId) {
        return applyPostRepository.findAllByUserIdAndRecruitmentId(userId, recruitmentId);
    }

    public void saveApplyPost(ApplyPost applyPost) {
        applyPostRepository.save(applyPost);
    }

    public ApplyPost getApplyPostById(int theId) {
        return applyPostRepository.findById(theId).get();
    }

    // Phân trang cho các lượt ứng tuyển (quản lý bởi HR)
    public Page<ApplyPost> findApplyPostPagination(int userId, int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);
        return applyPostRepository.findAllByUserId(userId, pageable);
    }

    public void deleteApplyPost(ApplyPost applyPost) {
        applyPostRepository.delete(applyPost);
    }



    // Common
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

}
