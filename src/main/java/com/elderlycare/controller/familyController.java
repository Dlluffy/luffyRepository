package com.elderlycare.controller;

import com.elderlycare.model.ElderPhoto;
import com.elderlycare.mapper.ElderPhotoMapper;
import com.elderlycare.mapper.HealthRecordMapper;
import com.elderlycare.mapper.InstitutionMapper;
import com.elderlycare.model.*;
import com.elderlycare.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class familyController {

    @Autowired
    UserService userService;

    @Autowired
    RelativeElderService userElderRelationService;

    @Autowired
    ElderService elderService;

    @Autowired
    InstitutionMapper institutionMapper;

    @Autowired
    CarePlanService carePlanService;

    @Autowired
    HealthService healthService;

    @Autowired
    HealthRecordMapper healthRecordMapper;

    @Autowired
    ElderPhotoMapper elderPhotoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/family")
    public String home() {
        return "family-login";  // 这里的 "home" 应该是你页面的名称（home.html）
    }

    @GetMapping("/family-login")
    public String home2() {
        return "family-login";  // 这里的 "home" 应该是你页面的名称（home.html）
    }

    @GetMapping("/family-logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除登录会话
        return "redirect:/family-login";
    }

    @GetMapping("/family-dashboard")
    public String familyDashboard( Model model,HttpSession session){
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return "redirect:/login"; // 或跳转你自己的登录页面
        }
        RelativeElder  relativeElder= userElderRelationService.getRelativeElderByUserId(user.getUid());
        Elder elder = elderService.get(relativeElder.getElderId());
        CarePlan carePlan =carePlanService.getPlanByElderId(relativeElder.getElderId());
        model.addAttribute("carePlan",carePlan);
        model.addAttribute("elder",elder);
        HealthRecord healthRecords = healthRecordMapper.getNewestRecord(elder.getElderId());
        model.addAttribute("healthRecordsNew",healthRecords);
        List<HealthRecord> healthRecordslist = healthService.findByElderId(elder.getElderId());
        List<String> labels = healthRecordslist.stream()
                .map(record -> new SimpleDateFormat("MM-dd").format(record.getRecordDate()))
                .collect(Collectors.toList());
        // 只取收缩压（血压格式如 "120/80"）
        List<Integer> systolicBP = healthRecordslist.stream()
                .map(record -> {
                    String bp = record.getBloodPressure();
                    if (bp != null && bp.contains("/")) {
                        try {
                            return Integer.parseInt(bp.split("/")[0]);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList());

        List<BigDecimal> bloodSugar = healthRecordslist.stream()
                .map(HealthRecord::getBloodSugar)
                .collect(Collectors.toList());

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartBloodPressure", systolicBP);
        model.addAttribute("chartBloodSugar", bloodSugar);

        List<ElderPhoto> elderPhotos = elderPhotoMapper.findByElderId(elder.getElderId());
        model.addAttribute("elderPhotos", elderPhotos);

        return "family-dashboard";
    }

    @GetMapping("/family-dashboard2")
    public String familyDashboard2(){
        return "family-register";
    }

    @GetMapping("/family-personalPage")
    public String familyHomePage(Model model, HttpSession session){
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return "redirect:/family-login"; // 或跳转你自己的登录页面
        }
        RelativeElder relativeElder =userElderRelationService.getRelativeElderByUserId(user.getUid());
        Elder elder = elderService.get(relativeElder.getElderId());
        Institution institution =institutionMapper.selectByPrimaryKey(elder.getInstitutionId());
        model.addAttribute("elder",elder);
        model.addAttribute("user",user);
        model.addAttribute("relativeElder",relativeElder);
        model.addAttribute("institution",institution);
        return "family-personalPage";
    }

    @PostMapping("/familyLogin")
    public String familyLogin(String username, String password, HttpSession session, Model model) {
        String encodedPassword = userService.getPasswordByUser(username);
        if (encodedPassword != null && passwordEncoder.matches(password, encodedPassword)) {
            User user = userService.getUserByName(username);
            if (user == null) {
                return "redirect:/family-login"; // 或跳转你自己的登录页面
            }
            session.setAttribute("User", user);
            System.out.println(user.getUid());
            RelativeElder relativeElder = userElderRelationService.getRelativeElderByUserId(user.getUid());
            Elder elder = elderService.get(relativeElder.getElderId());
            CarePlan carePlan = carePlanService.getPlanByElderId(relativeElder.getElderId());
            model.addAttribute("carePlan", carePlan);
            model.addAttribute("elder", elder);
            HealthRecord healthRecords = healthRecordMapper.getNewestRecord(elder.getElderId());
            model.addAttribute("healthRecordsNew", healthRecords);

            List<HealthRecord> healthRecordslist = healthService.findByElderId(elder.getElderId());

            List<String> labels = healthRecordslist.stream()
                    .map(record -> new SimpleDateFormat("MM-dd").format(record.getRecordDate()))
                    .collect(Collectors.toList());

            // 只取收缩压（血压格式如 "120/80"）
            List<Integer> systolicBP = healthRecordslist.stream()
                    .map(record -> {
                        String bp = record.getBloodPressure();
                        if (bp != null && bp.contains("/")) {
                            try {
                                return Integer.parseInt(bp.split("/")[0]);
                            } catch (Exception e) {
                                return null;
                            }
                        }
                        return null;
                    })
                    .collect(Collectors.toList());

            List<BigDecimal> bloodSugar = healthRecordslist.stream()
                    .map(HealthRecord::getBloodSugar)
                    .collect(Collectors.toList());

            model.addAttribute("chartLabels", labels);
            model.addAttribute("chartBloodPressure", systolicBP);
            model.addAttribute("chartBloodSugar", bloodSugar);

            List<ElderPhoto> elderPhotos = elderPhotoMapper.findByElderId(elder.getElderId());
            model.addAttribute("elderPhotos", elderPhotos);

            return "family-dashboard";
        } else {
            return "family-login";
        }
    }

    @GetMapping("/to-family-register")
    public String toFamilyRegister() {
        return "family-register";
    }
    @PostMapping("/family-register")
    public String familyRegister(String username, String password,
                                 String role,HttpSession session,  Model model
    ) {
        String encodedPassword = passwordEncoder.encode(password);
        if (userService.registerUser(username,encodedPassword,role)){
            model.addAttribute("username",username);
            User user=userService.getUserByName(username);
            model.addAttribute("UserId",user.getUid());
            return  "family-register2";
        }
        else {
            return  "family-login";
        }
    }

    @PostMapping("/family-register-elder")
    public String register(
            @RequestParam String elderName,
            @RequestParam String gender,
            @RequestParam String idCard,
            @RequestParam int age,
            @RequestParam String contact,
            @RequestParam String address,
            @RequestParam(required = false) String allergies,
            @RequestParam(required = false) String medicalHistory,
            @RequestParam(required = false) String emergencyContact,
            @RequestParam Long institutionId,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String relation,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Elder elder = new Elder();
            elder.setName(elderName);
            elder.setGender(gender);
            elder.setIdCard(idCard);
            elder.setAge(age);
            elder.setContact(contact);
            elder.setAddress(address);
            elder.setAllergies(allergies);
            elder.setMedicalHistory(medicalHistory);
            elder.setEmergencyContact(emergencyContact);
            elder.setInstitutionId(institutionId);

            elderService.save(elder);
            Elder elder2=elderService.getElderByIdCard(idCard);

            User user=userService.getUserByName(username);
            Long userId = Optional.ofNullable(user.getUid()).map(Number::longValue).orElseThrow(() -> new RuntimeException("用户ID为空"));

            userElderRelationService.linkUserToElder(userId, elder2.getElderId(),  relation);

            redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
            return "family-login";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "注册失败: " + e.getMessage());
            return "family-login";
        }

    }
    @PostMapping("/family/updateProfile")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> data) {
        String name = data.get("name");
        String idCard = data.get("elderIdCard");
        String contact = data.get("contact");
        String relation = data.get("relation");
        String address = data.get("address");
        String emergencyContact = data.get("emergencyContact");

        try {
            elderService.updateElderInfo(idCard,name, contact, address, emergencyContact);
//            relativeElderService.updateRelation(contact, relation); // 假设通过手机号绑定
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }


}
