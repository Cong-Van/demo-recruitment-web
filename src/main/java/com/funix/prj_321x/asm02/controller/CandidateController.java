package com.funix.prj_321x.asm02.controller;

import com.funix.prj_321x.asm02.DTO.UserDTO;
import com.funix.prj_321x.asm02.entity.*;
import com.funix.prj_321x.asm02.service.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
@RequestMapping("/candidate")
public class CandidateController {

    private CommonService commonService;

    public CandidateController(CommonService commonService) {
        this.commonService = commonService;
    }



    // Kết quả tìm kiếm công việc bằng tên (title) của bài đăng
    @GetMapping("/searchJob")
    public String searchJob(@RequestParam("keySearch") String keySearch, Model theModel, HttpServletRequest http) {

        return findRecruitmentPaginationSearchByJob(1, keySearch, theModel, http);
    }

    @GetMapping("/searchJob/page/{pageNo}")
    public String findRecruitmentPaginationSearchByJob(@PathVariable("pageNo") int pageNo,
                                                       @ModelAttribute("keySearch") String keySearch,
                                                       Model theModel,
                                                       HttpServletRequest http) {

        int pageSize = 5;

        Page<Recruitment> page = commonService.findRecruitmentPaginationSearchByTitle(keySearch, pageNo, pageSize);
        List<Recruitment> recruitments = page.getContent();

        addAttributeToModel(theModel, keySearch, http);

        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/result-search-job";
    }

    // Kết quả tìm kiếm công  việc với tên của company
    @GetMapping("/searchCompany")
    public String searchCompany(@RequestParam("keySearch") String keySearch, Model theModel, HttpServletRequest http) {

        return findRecruitmentPaginationSearchByCompany(1, keySearch, theModel, http);
    }

    @GetMapping("/searchCompany/page/{pageNo}")
    public String findRecruitmentPaginationSearchByCompany(@PathVariable("pageNo") int pageNo,
                                                           @ModelAttribute("keySearch") String keySearch,
                                                           Model theModel,
                                                           HttpServletRequest http) {

        int pageSize = 5;

        Page<Recruitment> page = commonService.findRecruitmentPaginationSearchByCompany(keySearch, pageNo, pageSize);
        List<Recruitment> recruitments = page.getContent();

        addAttributeToModel(theModel, keySearch, http);

        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/result-search-company";
    }

    // Kết quả tìm kiếm theo địa điểm
    @GetMapping("/searchAddress")
    public String searchAddress(@RequestParam("keySearch") String keySearch, Model theModel, HttpServletRequest http) {

        return findRecruitmentPaginationSearchByAddress(1, keySearch, theModel, http);
    }

    @GetMapping("/searchAddress/page/{pageNo}")
    public String findRecruitmentPaginationSearchByAddress(@PathVariable("pageNo") int pageNo,
                                                           @RequestParam("keySearch") String keySearch,
                                                           Model theModel,
                                                           HttpServletRequest http) {

        int pageSize = 5;

        Page<Recruitment> page = commonService.findRecruitmentPaginationSearchByAddress(keySearch, pageNo, pageSize);
        List<Recruitment> recruitments = page.getContent();

        addAttributeToModel(theModel, keySearch, http);

        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/result-search-address";
    }

    public void addAttributeToModel(Model theModel, String keySearch, HttpServletRequest http) {

        // Số lượng ứng viên, công ty và bài đăng hiển thị đầu trang
        List<User> candidates =  commonService.getAllCandidate();
        List<Company> companies = commonService.getAllCompany();
        List<Recruitment> recruitments = commonService.getAllRecruitment();

        theModel.addAttribute("candidateQuantity", candidates.size());
        theModel.addAttribute("companyQuantity", companies.size());
        theModel.addAttribute("recruitmentQuantity", recruitments.size());
        theModel.addAttribute("keySearch", keySearch);

        // Lấy ra user và đưa vào danh sách các công việc đã lưu của user
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());

        theModel.addAttribute("recruitmentsSaved", user.getRecruitments());
    }



    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("userDTO") UserDTO userDTO, Model theModel) {

        // Lấy ra user và cập nhật thông tin cho user
        User user = commonService.getUserByEmail(userDTO.getEmail());
        commonService.setUserInf(user, userDTO);
        commonService.saveUser(user);

        return "redirect:/profile";
    }

    @PostMapping("/deleteCv")
    public String deleteCv(@RequestParam("cvId") int cvId, @RequestParam("userId") int userId) {

        // Xóa Cv và Cv tương ứng trong user
        User user = commonService.getUserById(userId);
        user.setCv(null);

        commonService.deleteCvById(cvId);

        return "redirect:/profile";
    }



    // Ứng tuyển (apply) công việc
    @PostMapping("/applyJobInHome")
    public String applyJobInHome (@RequestParam("userId") int userId,
                                  @RequestParam("recruitmentId") int recruitmentId,
                                  @RequestParam("type") int type,
                                  @RequestParam("text1") String text1,
                                  @RequestParam("file") MultipartFile file,
                                  @RequestParam("text2") String text2,
                                  RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/home";
    }

    @PostMapping("/applyJobInSearchJob")
    public String applyJobInSearchJob (@RequestParam("keySearch") String keySearch,
                                       @RequestParam("userId") int userId,
                                       @RequestParam("recruitmentId") int recruitmentId,
                                       @RequestParam("type") int type,
                                       @RequestParam("text1") String text1,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("text2") String text2,
                                       RedirectAttributes theRedirectModel) throws IOException {

        theRedirectModel.addAttribute("keySearch", keySearch);
        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/candidate/searchJob";
    }

    @PostMapping("/applyJobInSearchCompany")
    public String applyJobInSearchCompany (@RequestParam("keySearch") String keySearch,
                                           @RequestParam("userId") int userId,
                                           @RequestParam("recruitmentId") int recruitmentId,
                                           @RequestParam("type") int type,
                                           @RequestParam("text1") String text1,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("text2") String text2, RedirectAttributes theRedirectModel) throws IOException {

        theRedirectModel.addAttribute("keySearch", keySearch);
        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/candidate/searchCompany";
    }

    @PostMapping("/applyJobInSearchAddress")
    public String applyJobInSearchAddress (@RequestParam("keySearch") String keySearch,
                                           @RequestParam("userId") int userId,
                                           @RequestParam("recruitmentId") int recruitmentId,
                                           @RequestParam("type") int type,
                                           @RequestParam("text1") String text1,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("text2") String text2,
                                           RedirectAttributes theRedirectModel) throws IOException {

        theRedirectModel.addAttribute("keySearch", keySearch);
        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/candidate/searchAddress";
    }

    @PostMapping("/applyJob")
    public String applyJobInDetail (@RequestParam("userId") int userId,
                                    @RequestParam("recruitmentId") int recruitmentId,
                                    @RequestParam("type") int type,
                                    @RequestParam("text1") String text1,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("text2") String text2,
                                    RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/recruitment/detail/" + recruitmentId;
    }

    @PostMapping("/applyJobSaved")
    public String applyJobSaved (@RequestParam("userId") int userId,
                                 @RequestParam("recruitmentId") int recruitmentId,
                                 @RequestParam("type") int type,
                                 @RequestParam("text1") String text1,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("text2") String text2,
                                 RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/candidate/jobSavedList";
    }

    @PostMapping("/applyJobInJobList")
    public String applyJobInJobList(@RequestParam("userId") int userId,
                                    @RequestParam("recruitmentId") int recruitmentId,
                                    @RequestParam("type") int type,
                                    @RequestParam("text1") String text1,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("text2") String text2,
                                    RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/recruitment";
    }

    @PostMapping("/applyJobInCategory/{categoryId}")
    public String applyJobInCategory(@PathVariable("categoryId") int categoryId,
                                     @RequestParam("userId") int userId,
                                     @RequestParam("recruitmentId") int recruitmentId,
                                     @RequestParam("type") int type,
                                     @RequestParam("text1") String text1,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("text2") String text2,
                                     RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/recruitment/category/" + categoryId;
    }

    @PostMapping("/applyJobInCompanyPosts/{companyId}")
    public String applyJobInCompanyPosts(@PathVariable("companyId") int companyId,
                                         @RequestParam("userId") int userId,
                                         @RequestParam("recruitmentId") int recruitmentId,
                                         @RequestParam("type") int type,
                                         @RequestParam("text1") String text1,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam("text2") String text2,
                                         RedirectAttributes theRedirectModel) throws IOException {

        applyJobCommon(userId, recruitmentId, type, text1, file, text2, theRedirectModel);

        return "redirect:/candidate/companyPostList/" + companyId;
    }

    // Dùng chung khi apply job ở các trang khác nhau
    public void applyJobCommon(int userId, int recruitmentId, int type, String text1,
                         MultipartFile file, String text2, RedirectAttributes theRedirectModel)  throws IOException{

        boolean msgApply = false;
        String message = "Bạn chưa upload Cv để ứng tuyển!";

        /*
        Lấy ra lượt ứng tuyển của người dùng cho bài đăng
        Nếu chưa có thì tạo mới lượt ứng tuyển
        Dùng upload Cv mới thì set cvName cho lượt ứng tuyển và lưu lại file Cv
        Hoặc dùng Cv có sẵn và lưu lại
        Nếu đã có lượt ứng tuyển thì báo đã ứng tuyển
         */
        User user = commonService.getUserById(userId);
        Recruitment recruitment = commonService.getRecruitmentById(recruitmentId);

        ApplyPost applyPost = commonService.getApplyPostByUserIdAndRecruitmentId(userId, recruitmentId);

        if (applyPost == null) {
            applyPost = new ApplyPost();
            applyPost.setUser(user);
            applyPost.setRecruitment(recruitment);

            if (type == 2 && !file.isEmpty()) {
                applyPost.setCvName(file.getOriginalFilename());
                applyPost.setText(text2);
                commonService.saveApplyPost(applyPost);

                // Lưu lại file
                File saveFile = new ClassPathResource("static/assets/images").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                msgApply = true;

            } else if (type == 1 && user.getCv() != null) {
                applyPost.setCvName(user.getCv().getFileName());
                applyPost.setText(text1);
                commonService.saveApplyPost(applyPost);

                msgApply = true;
            }
        } else {
            message = "Bạn đã ứng tuyển công việc này!";
        }

        theRedirectModel.addFlashAttribute("msgApply", msgApply);
        theRedirectModel.addFlashAttribute("message", message);
    }



    // Thả tim lưu lại công việc
    @GetMapping("/saveJobInHome/{recruitmentId}")
    public String saveJobInHome(@PathVariable("recruitmentId") int recruitmentId,
                                HttpServletRequest http,
                                RedirectAttributes theRedirectModel) {

        saveJob(recruitmentId, http, theRedirectModel);

        return "redirect:/home";
    }

    @GetMapping("/saveJobInSearchJob/{recruitmentId}/{keySearch}")
    public String saveJobInSearchJob (@PathVariable("recruitmentId") int recruitmentId,
                                      HttpServletRequest http,
                                      RedirectAttributes theRedirectModel,
                                      @PathVariable("keySearch") String keySearch) {

        saveJob(recruitmentId, http, theRedirectModel);

        theRedirectModel.addAttribute("keySearch", keySearch);

        return "redirect:/candidate/searchJob";
    }

    @GetMapping("/saveJobInSearchCompany/{recruitmentId}/{keySearch}")
    public String saveJobInSearchCompany (@PathVariable("recruitmentId") int recruitmentId,
                                          HttpServletRequest http,
                                          RedirectAttributes theRedirectModel,
                                          @PathVariable("keySearch") String keySearch) {

        saveJob(recruitmentId, http, theRedirectModel);

        theRedirectModel.addAttribute("keySearch", keySearch);

        return "redirect:/candidate/searchCompany";
    }

    @GetMapping("/saveJobInSearchAddress/{recruitmentId}/{keySearch}")
    public String saveJobInSearchAddress (@PathVariable("recruitmentId") int recruitmentId,
                                          HttpServletRequest http,
                                          RedirectAttributes theRedirectModel,
                                          @PathVariable("keySearch") String keySearch) {

        saveJob(recruitmentId, http, theRedirectModel);

        theRedirectModel.addAttribute("keySearch", keySearch);

        return "redirect:/candidate/searchAddress";
    }

    @GetMapping("/saveJob/{recruitmentId}")
    public String saveJobInDetail (@PathVariable("recruitmentId") int recruitmentId,
                                   HttpServletRequest http,
                                   RedirectAttributes theRedirectModel) {

        saveJob(recruitmentId, http, theRedirectModel);

        return "redirect:/recruitment/detail/" + recruitmentId;
    }

    @GetMapping("/saveJobInJobList/{recruitmentId}")
    public String saveJobInJobList(@PathVariable("recruitmentId") int recruitmentId,
                                   HttpServletRequest http,
                                   RedirectAttributes theRedirectModel) {

        saveJob(recruitmentId, http, theRedirectModel);

        return "redirect:/recruitment";
    }

    @GetMapping("/saveJobInCategory/{recruitmentId}/{categoryId}")
    public String saveJobInCategory(@PathVariable("categoryId") int categoryId,
                                    @PathVariable("recruitmentId") int recruitmentId,
                                    HttpServletRequest http,
                                    RedirectAttributes theRedirectModel) {

        saveJob(recruitmentId, http, theRedirectModel);

        return "redirect:/recruitment/category/" + categoryId;
    }

    @GetMapping("/saveJobInCompanyPosts/{recruitmentId}/{companyId}")
    public String saveJobInCompanyPosts(@PathVariable("companyId") int companyId,
                                        @PathVariable("recruitmentId") int recruitmentId,
                                        HttpServletRequest http,
                                        RedirectAttributes theRedirectModel) {

        saveJob(recruitmentId, http, theRedirectModel);

        return "redirect:/candidate/companyPostList/" + companyId;
    }

    // Dùng chung khi lưu công việc ở các trang khác nhau
    public void saveJob(int recruitmentId,
                        HttpServletRequest http,
                        RedirectAttributes theRedirectModel) {

        String msgFolJob;

        // Lấy ra ứng cử viên và danh sách các công việc đã lưu
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());
        List<Recruitment> recruitmentsSaved = user.getRecruitments();

        // Lưu hoặc bỏ lưu công việc
        Recruitment recruitment = commonService.getRecruitmentById(recruitmentId);
        if (recruitmentsSaved.contains(recruitment)) {
            recruitmentsSaved.remove(recruitment);
            msgFolJob = "Đã bỏ lưu công việc!";
        } else {
            recruitmentsSaved.add(recruitment);
            msgFolJob = "Lưu thành công!";
        }

        commonService.saveUser(user);

        theRedirectModel.addFlashAttribute("msgFolJob", msgFolJob);
    }



    @GetMapping("/followCompany/{companyId}")
    public String followCompany(@PathVariable("companyId") int companyId,
                                RedirectAttributes theRedirectModel,
                                HttpServletRequest http) {

        // Lấy ra ứng cử viên và danh sách các công ty đã theo dõi
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        User user = commonService.getUserById(userSes.getId());
        List<Company> companiesFollowed = user.getCompanies();

        // Theo dõi hoặc bỏ theo dõi công ty
        Company company = commonService.getCompanyById(companyId);
        if (companiesFollowed.contains(company)) {
            companiesFollowed.remove(company);
        } else {
            companiesFollowed.add(company);
        }

        commonService.saveUser(user);

        return "redirect:/company/detail/" + companyId;
    }



    // Hiển thị danh sách các bài đăng đã lưu/đã ứng tuyển
    @GetMapping("/jobSavedList")
    public String showJobSavedList(Model theModel, HttpServletRequest http) {

        return findRecruitmentSavedPagination(1, theModel, http);
    }

    @GetMapping("/jobSavedList/page/{pageNo}")
    public String findRecruitmentSavedPagination(@PathVariable("pageNo") int pageNo,
                                                 Model theModel,
                                                 HttpServletRequest http) {

        // Lấy danh sách các công việc đã lưu của user bằng userId và phân trang
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        Page<Recruitment> page = commonService.findRecruitmentSavedPagination(userSes.getId(), pageNo, 5);
        List<Recruitment> recruitmentsSaved = page.getContent();

        theModel.addAttribute("recruitmentsSaved", recruitmentsSaved);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/list-save-job";
    }

    @GetMapping("/jobAppliedList")
    public String showJobApplied(Model theModel, HttpServletRequest http) {

        return findApplyPostPagination(1, theModel, http);
    }

    @GetMapping("/jobAppliedList/page/{pageNo}")
    public String findApplyPostPagination(@PathVariable("pageNo") int pageNo,
                                           Model theModel,
                                           HttpServletRequest http) {

        // Lấy danh sách các lượt ứng tuyển của user bằng userId và phân trang
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        Page<ApplyPost> page = commonService.findApplyPostPagination(userSes.getId(), pageNo, 5);
        List<ApplyPost> applyPosts = page.getContent();

        theModel.addAttribute("applyPosts", applyPosts);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/list-apply-job";
    }



    // Bỏ ứng tuyển, bỏ lưu công việc
    @PostMapping("/deleteApplyPost")
    public String deleteApplyPost(@RequestParam("applyPostId") int applyPostId) {

        ApplyPost applyPost = commonService.getApplyPostById(applyPostId);

        commonService.deleteApplyPost(applyPost);

        return "redirect:/candidate/jobAppliedList";
    }

    @PostMapping("/deleteJobSaved")
    public String deleteJobSaved(@RequestParam("recruitmentId") int recruitmentId) {

        Recruitment recruitment = commonService.getRecruitmentById(recruitmentId);

        commonService.deleteRecruitment(recruitment);

        return "redirect:/candidate/jobSavedList";
    }



    // Danh sách các công ty đã follow
    @GetMapping("/companiesFollowed")
    public String showCompanyFollowed(Model theModel, HttpServletRequest http) {

        return showCompanyFollowed(1, theModel, http);
    }

    @GetMapping("/companiesFollowed/page/{pageNo}")
    public String showCompanyFollowed(@PathVariable("pageNo") int pageNo,
                                      Model theModel,
                                      HttpServletRequest http) {

        // Lấy ra danh sách các công ty đã follow và phân trang bằng userId
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");

        Page<Company> page = commonService.findCompanyFollowedPagination(userSes.getId(), pageNo, 5);
        List<Company> companiesFollowed = page.getContent();

        theModel.addAttribute("companiesFollowed", companiesFollowed);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/list-follow-company";
    }

    // Xóa bỏ theo dõi công ty
    @PostMapping("/unfollowCompany")
    public String unFollowCompany(@RequestParam("companyId") int companyId, @RequestParam("userId") int userId) {

        User user = commonService.getUserById(userId);
        List<Company> companiesFollowed = user.getCompanies();
        Company company = commonService.getCompanyById(companyId);

        companiesFollowed.remove(company);

        commonService.saveUser(user);

        return "redirect:/candidate/companiesFollowed";
    }



    // Danh sách các bài đăng của công ty
    @GetMapping("/companyPostList/{companyId}")
    public String showCompanyPostList(@PathVariable("companyId") int companyId, Model theModel, HttpServletRequest http) {

        return showCompanyPostPagination(companyId, 1, theModel, http);
    }

    @GetMapping("/companyPostList/{companyId}/page/{pageNo}")
    public String showCompanyPostPagination(@PathVariable("companyId") int companyId,
                                            @PathVariable("pageNo") int pageNo,
                                            Model theModel,
                                            HttpServletRequest http) {

        /*
        Lấy ra danh sách các bài đăng đã lưu để hiển thị trạng thái lưu/ chưa lưu tương ứng
        Lấy ra danh sách các bài đăng của công ty
        Đưa thông tin lên và phân trang
         */
        HttpSession session = http.getSession();
        User userSes = (User) session.getAttribute("user");
        User user = commonService.getUserById(userSes.getId());
        List<Recruitment> recruitmentsSaved = user.getRecruitments();

        Company company = commonService.getCompanyById(companyId);

        Page<Recruitment> page = commonService.findRecruitmentManagePagination(company.getId(), pageNo, 5);
        List<Recruitment> recruitments = page.getContent();

        theModel.addAttribute("recruitmentsSaved", recruitmentsSaved);
        theModel.addAttribute("company", company);
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", page.getTotalPages());

        return "public/post-company";
    }
}
