package com.funix.prj_321x.asm02.controller;

import com.funix.prj_321x.asm02.DTO.CompanyDTO;
import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import com.funix.prj_321x.asm02.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("userDTO") UserDTO userDTO, Model theModel) {

        // Lấy ra user và cập nhật thông tin cho user
        User user = authService.getUserByEmail(userDTO.getEmail());
        authService.setUserInf(user, userDTO);
        authService.saveUser(user);

        return "redirect:/profile";
    }

    @PostMapping("/update-company")
    public String updateCompany(@ModelAttribute("companyDTO") CompanyDTO companyDTO,
                                @RequestParam("userId") int userId,
                                Model theModel) {

        /*
        Lấy ra company và cập nhật thông tin cho company
        Tạo mới company và liên kết với user nếu chưa có
         */
        User user = authService.getUserById(userId);
        Company company = authService.getCompanyByUserId(userId);

        if (company == null) {
            company = new Company();
            company.setUser(user);
        }

        authService.setCompanyInf(company, companyDTO);
        authService.saveCompany(company);

        return "redirect:/profile";
    }



    @GetMapping("/postRecruitment")
    public String postRecruitment(Model theModel) {

        List<Category> categories = authService.getAllCategory();

        theModel.addAttribute("newRecruitment", new Recruitment());
        theModel.addAttribute("categories", categories);

        return "public/post-job";
    }

    @PostMapping("/addRecruitment")
    public String addRecruitment(@ModelAttribute("newRecruitment") Recruitment newRecruitment,
                                 HttpServletRequest http,
                                 RedirectAttributes theRedirectModel) {

        boolean success = false;

        /*
        Lấy ra user quản lý công ty để tìm công ty
        Thêm bài đăng tuyển dụng cho công ty
         */
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");
        Company company = authService.getCompanyByUserId(userSes.getId());

        if (company != null) {
            newRecruitment.setCompany(company);
            authService.saveRecruitment(newRecruitment);

            Category category = authService.getCategoryById(newRecruitment.getCategory().getId());
            category.setNumberChoose(category.getNumberChoose() + 1);
            authService.saveCategory(category);

            success = true;
        }

        // Trạng thái thêm mới thành công hoặc thất bại
        theRedirectModel.addFlashAttribute("success", success);

        return "redirect:/auth/postRecruitment";
    }



    // Hiển thị các bài đăng tuyển dụng của công ty
    @GetMapping("/postList")
    public String postList(Model theModel, HttpServletRequest http) {

        return findRecruitmentManagePagination(1, theModel, http);
    }

    @GetMapping("/postListPage")
    public String findRecruitmentManagePagination(@RequestParam("page") int page,
                                                  Model theModel,
                                                  HttpServletRequest http) {

        // Tìm công ty được quản lý theo HR, nếu chưa có tạo mới (companyId = 0)
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");
        Company company = authService.getCompanyByUserId(userSes.getId());
        if (company == null) company = new Company();

        int pageSize = 5;

        Page<Recruitment> eachPage = authService.findRecruitmentManagePagination(company.getId(), page, pageSize);
        List<Recruitment> recruitments = eachPage.getContent();

        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", eachPage.getTotalPages());

        return "public/post-list";
    }

    @GetMapping("/recruitmentForUpdate/{recruitmentId}")
    public String showRecruitmentForUpdate(@PathVariable("recruitmentId") int recruitmentId, Model theModel) {

        Recruitment recruitment = authService.getRecruitmentById(recruitmentId);
        List<Category> categories = authService.getAllCategory();

        theModel.addAttribute("recruitment", recruitment);
        theModel.addAttribute("categories", categories);

        return "public/edit-job";
    }

    @PostMapping("/updateRecruitment")
    public String updateRecruitment(@ModelAttribute("recruitment") Recruitment recruitment,
                                    @RequestParam("recruitmentId") int recruitmentId) {

        Recruitment recruitmentUpdate = authService.getRecruitmentById(recruitmentId);

        authService.updateRecruitment(recruitmentUpdate, recruitment);

        return "redirect:/auth/postList";
    }

    @PostMapping("deleteRecruitment")
    public String deleteRecruitment(@RequestParam("recruitmentId") int recruitmentId) {

        Recruitment recruitment = authService.getRecruitmentById(recruitmentId);
        Category category = authService.getCategoryById(recruitment.getCategory().getId());

        authService.deleteRecruitment(recruitment);

        category.setNumberChoose(category.getNumberChoose() - 1);
        authService.saveCategory(category);

        return "redirect:/auth/postList";
    }

    // Hiển thị danh sách các ứng cử viên
    @GetMapping("/candidateList")
    public String showCandidateList(Model theModel, HttpServletRequest http) {

        return findCandidatePagination(1, theModel, http);
    }

    @GetMapping("/candidateListPage")
    public String findCandidatePagination(@RequestParam("page") int page,
                                          Model theModel,
                                          HttpServletRequest http) {

        // Tìm công ty được quản lý theo HR, nếu chưa có tạo mới (companyId = 0)
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");
        Company company = authService.getCompanyByUserId(userSes.getId());

        if (company == null) company = new Company();

        int pageSize = 5;

        Page<User> eachPage = authService.findCandidatePagination(company.getId(), page, pageSize);
        List<User> candidates = eachPage.getContent();

        theModel.addAttribute("candidates", candidates);
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", eachPage.getTotalPages());

        return "public/list-user";
    }

    @GetMapping("/approve/{applyPostId}/{recruitmentId}")
    public String approveApplyPost(@PathVariable("applyPostId") int applyPostId,
                                   @PathVariable("recruitmentId") int recruitmentId) {

        ApplyPost applyPost = authService.getApplyPostById(applyPostId);
        applyPost.setStatus(1);
        authService.saveApplyPost(applyPost);

        return "redirect:/recruitment/detail/" + recruitmentId;
    }

}
