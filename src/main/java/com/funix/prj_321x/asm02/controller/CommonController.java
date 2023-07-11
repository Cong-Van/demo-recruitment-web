package com.funix.prj_321x.asm02.controller;

import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import com.funix.prj_321x.asm02.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class CommonController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }



    // Hiển thị trang home
    @GetMapping("/")
    public String common(Model theModel) {

        // Số lượng ứng viên, công ty, bài đăng hiển thị đầu trang
        theModel.addAttribute("totalCandidates", commonService.getAllCandidate().size());
        theModel.addAttribute("totalCompanies", commonService.getAllCompany().size());
        theModel.addAttribute("totalRecruitments", commonService.getAllRecruitment().size());

        // Đưa vào những công ty, bài đăng nổi bật và top danh mục công việc
        List<Company> companies = commonService.getTopCompanies();
        List<Recruitment> recruitments = commonService.getTopRecruitment();
        List<Category> categories = commonService.getTopCategories();

        theModel.addAttribute("companies", companies);
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("categories", categories);

        return "public/home";
    }

    @GetMapping("/home")
    public String showHome(Model theModel, HttpServletRequest http) {

        // Lấy ra user và đưa vào danh sách các công việc đã lưu của user để hiển thị trạng thái lưu/ chưa lưu
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        theModel.addAttribute("recruitmentsSaved", user.getRecruitments());

        return common(theModel);
    }

    @GetMapping("/showLoginPage")
    public String showLoginPage(Model theModel) {

        List<Role> roles = commonService.getAllRoles();

        theModel.addAttribute("userDTO", new UserDTO());
        theModel.addAttribute("roles", roles);

        return "public/login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                           BindingResult theBindingResult,
                           Model theModel,
                           @RequestParam("rePassword") String rePassword,
                           RedirectAttributes theRedirectModel) {

        String email = userDTO.getEmail();
        logger.info("Processing registration form for: " + email);

        // Form validation
        if (theBindingResult.hasErrors()){
            List<Role> roles = commonService.getAllRoles();
            theModel.addAttribute("roles", roles);
            return "public/login";
        }

        User user = commonService.getUserByEmail(userDTO.getEmail());

        boolean success = false;
        String message = "error";

        if (!userDTO.getPassword().equals(rePassword)) {
            message = "Nhập lại mật khẩu không khớp";
        } else if (user == null) {
            commonService.save(userDTO);
            success = true;
        } else {
            message = "Email đã được sử dụng";
        }

        theRedirectModel.addFlashAttribute("success", success);
        theRedirectModel.addFlashAttribute("message", message);

        return "redirect:/showLoginPage";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "errors/access-denied";
    }



    // Hiển thị các trang khác (Công việc)
    @GetMapping("/recruitment")
    public String showRecruitment(Model theModel, HttpServletRequest http) {

        return findRecruitmentPagination(1, theModel, http);
    }

    @GetMapping("/recruitment/page/{pageNo}")
    public String findRecruitmentPagination(@PathVariable("pageNo") int pageNo, Model theModel, HttpServletRequest http) {

        /*
        Lấy ra và đưa vào các thông tin
        User và danh sách các công việc đã lưu
        Danh sách các công ty nổi bật
        Toàn bộ các bài đăng tuyển
         */
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        List<Company> companies = commonService.getTopCompanies();

        Page<Recruitment> page = commonService.findRecruitmentPagination(pageNo, 5);
        List<Recruitment> recruitments = page.getContent();

        theModel.addAttribute("companies", companies);
        theModel.addAttribute("recruitmentsSaved", user.getRecruitments());
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/recruitment";
    }

    // Hiển thị các công việc tuyển dụng theo loại công việc (category)
    @GetMapping("/recruitment/category/{categoryId}")
    public String showRecruitmentByCategory(@PathVariable("categoryId") int categoryId, Model theModel, HttpServletRequest http) {

        return findRecruitmentByCategoryPagination(categoryId, 1, theModel, http);
    }

    @GetMapping("/recruitment/category/{categoryId}/page/{pageNo}")
    public String findRecruitmentByCategoryPagination(@PathVariable("categoryId") int categoryId,
                                                      @PathVariable("pageNo") int pageNo,
                                                      Model theModel,
                                                      HttpServletRequest http) {

        /*
        Lấy ra và đưa vào các thông tin
        User và danh sách các công việc đã lưu
        Các bài đăng tuyển với loại công việc tương ứng
         */
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        Page<Recruitment> page = commonService.findRecruitmentByCategoryPagination(categoryId, pageNo, 5);
        List<Recruitment> recruitments = page.getContent();

        theModel.addAttribute("category", commonService.getCategoryById(categoryId));
        theModel.addAttribute("recruitmentsSaved", user.getRecruitments());
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/list-re";
    }

    // Hiển thị chi tiết cho bài đăng tuyển dụng
    @GetMapping("/recruitment/detail/{recruitmentId}")
    public String showRecruitmentDetail(@PathVariable("recruitmentId") int recruitmentId,
                                        Model theModel,
                                        HttpServletRequest http) {

        /*
        Đưa vào bài tuyển dụng
        Tìm danh sách các bài tuyển dụng ứng cử viên đã lưu
         */
        Recruitment recruitment = commonService.getRecruitmentById(recruitmentId);

        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        theModel.addAttribute("recruitment", recruitment);
        theModel.addAttribute("recruitmentsSaved", user.getRecruitments());

        // Đưa vào các lượt ứng tuyển và các bài tuyển dụng có tiêu đề tương tự
        List<ApplyPost> applyPosts = recruitment.getApplyPosts();
        List<Recruitment> recruitmentsRelated = commonService.getAllByTitle(recruitment.getTitle());

        theModel.addAttribute("applyPosts", applyPosts);
        theModel.addAttribute("recruitmentsRelated", recruitmentsRelated);

        return "public/detail-post";
    }

    // Hiển thị thông tin company
    @GetMapping("/company/detail/{companyId}")
    public String showCompanyDetail(@PathVariable("companyId") int companyId,
                                    Model theModel,
                                    HttpServletRequest http) {

        /*
        Đưa vào company
        Đưa vào danh sách các company đã follow để hiển thị tương ứng chức năng theo dõi
         */
        Company company = commonService.getCompanyById(companyId);

        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        theModel.addAttribute("companiesFollowed", user.getCompanies());
        theModel.addAttribute("company", company);

        return "public/detail-company";
    }



    // Edit profile (Dùng chung)
    @GetMapping("/profile")
    public String showProfile(HttpServletRequest http, Model theModel) {

        // Lấy thông tin user trong session đưa vào userDTO
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");
        User user = commonService.getUserById(userSes.getId());

        UserDTO userDTO  = new UserDTO();
        commonService.setUserDTOInf(userDTO, user);

        // Lấy thông tin company tương ứng với user để đưa vào companyDTO
        Company company = commonService.getCompanyByUserId(user.getId());
        if (company == null) company = new Company();

        CompanyDTO companyDTO = new CompanyDTO();
        commonService.setCompanyDTOInf(companyDTO, company);

        // Lấy thông tin CV tương ứng với user hoặc tạo mới để lưu thông tin khi upload file
        Cv cv = commonService.getCvByUserId(user.getId());
        if (cv == null) cv = new Cv();

        theModel.addAttribute("userDTO", userDTO);
        theModel.addAttribute("companyDTO", companyDTO);
        theModel.addAttribute("cv", cv);

        return "public/profile";
    }



    // Upload ảnh đại diện cho User
    @PostMapping("/uploadUserImage")
    public String uploadUserImage(@RequestParam("image") MultipartFile image,
                                  @RequestParam("userId") int userId,
                                  RedirectAttributes theRedirectModel) throws IOException {

        User user = commonService.getUserById(userId);
        user.setImage(image.getOriginalFilename());
        commonService.saveUser(user);

        saveFile(image);

        theRedirectModel.addFlashAttribute("msgImg", "Cập nhật ảnh thành công!");

        return "redirect:/profile";
    }

    // Upload logo cho Company
    @PostMapping("/uploadCompanyLogo")
    public String uploadCompanyLogo(@RequestParam("logo") MultipartFile logo,
                                    @RequestParam("userId") int userId,
                                    RedirectAttributes theRedirectModel) throws IOException {

        User user = commonService.getUserById(userId);
        Company company = commonService.getCompanyByUserId(userId);
        if (company == null) {
            company = new Company();
            company.setUser(user);
        }
        company.setLogo(logo.getOriginalFilename());
        commonService.saveCompany(company);

        saveFile(logo);

        theRedirectModel.addFlashAttribute("msgLogo", "Cập nhật logo thành công!");

        return "redirect:/profile";
    }

    // Upload Cv cho Candidate
    @PostMapping("/uploadCandidateCv")
    public String uploadCandidateCv(@RequestParam("file") MultipartFile file,
                                    @RequestParam("userId") int userId,
                                    RedirectAttributes theRedirectModel) throws IOException {

        User user = commonService.getUserById(userId);
        Cv cv = commonService.getCvByUserId(userId);
        if (cv == null) {
            cv = new Cv();
            cv.setUser(user);
        }
        cv.setFileName(file.getOriginalFilename());
        commonService.saveCv(cv);

        saveFile(file);

        theRedirectModel.addFlashAttribute("msgCv", "Cập nhật Cv thành công!");

        return "redirect:/profile";
    }

    public void saveFile(MultipartFile file) throws IOException {

        // Lưu file vào target/classes/static/assets/...
        File saveLogo = new ClassPathResource("static/assets/images").getFile();
        Path path = Paths.get(saveLogo.getAbsolutePath() + File.separator + file.getOriginalFilename());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

}
