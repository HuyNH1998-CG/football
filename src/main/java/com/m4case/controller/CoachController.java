package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.model.WeeklySalary;
import com.m4case.service.ICoachService;
import com.m4case.service.IMyUserService;
import com.m4case.service.IWeeklySalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/c")
public class CoachController {
    @Autowired
    ICoachService iCoachService;
    @Autowired
    IMyUserService iMyUserService;
    @Autowired
    IWeeklySalaryService weeklySalaryService;

    @Value("${uploadPart}")
    String uploadPart;

//    @GetMapping("/create")
//    public ModelAndView showCreate() {
//        ModelAndView modelAndView = new ModelAndView("/coach/createCoach");
//        modelAndView.addObject("coach", new Coach());
//        return modelAndView;
//    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEdit(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        if (coach.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/coach/editCoach");
            modelAndView.addObject("coach", coach.get());
            return modelAndView;
        } else {
            return new ModelAndView("/focus-2/page-error-404");
        }
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute("coach") Coach coach) {
        Coach realCoach = iCoachService.findById(coach.getId()).get();
        realCoach.setName(coach.getName());
        realCoach.setAge(coach.getAge());
        realCoach.setHometown(coach.getHometown());
        realCoach.setSalary(coach.getSalary());
        iCoachService.save(realCoach);
        return new ModelAndView("redirect:/c/profile/" + coach.getId());
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDelete(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        if (coach.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/coach/deleteCoach");
            modelAndView.addObject("coach", coach.get());
            return modelAndView;
        } else {
            return new ModelAndView("/focus-2/page-error-404");
        }
    }

    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam Long id) {
        Coach coach = iCoachService.findById(id).get();
        MyUser user = iMyUserService.findByEmail(coach.getEmail());
        weeklySalaryService.deleteAllByCoach_Id(coach.getId());
        iCoachService.delete(id);
        iMyUserService.delete(user.getId());
        return new ModelAndView("redirect:/a/coachList");
    }

    @PostMapping("/create")
    public ModelAndView createCoach(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Coach coach) throws IOException {
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        coach.setAvatar(fileName);
        coach = (Coach) iCoachService.saveObj(coach);
        return new ModelAndView("redirect:/c/profile/" + coach.getId());
    }

    @GetMapping("/changeAvatar/{id}")
    public ModelAndView showChangeCoachAvatar(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        if (coach.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/coach/changeCoachAvatar");
            modelAndView.addObject("coach", coach.get());
            return modelAndView;
        } else {
            return new ModelAndView("/focus-2/page-error-404");
        }
    }

    @PostMapping("/changeAvatar")
    public ModelAndView changeCoachAvatar(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Coach coach) throws IOException {
        coach = iCoachService.findById(coach.getId()).get();
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        coach.setAvatar(fileName);
        iCoachService.save(coach);
        return new ModelAndView("redirect:/c/profile/" + coach.getId());
    }

    @GetMapping("/profile/{id}")
    public ModelAndView coachProfile(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/coach/coachDetail");
        Map<String, Long> data = new LinkedHashMap<>();
        List<WeeklySalary> weeklySalaryList = (List<WeeklySalary>) weeklySalaryService.findByCoach_Id(id);
        if (weeklySalaryList != null) {
            for (WeeklySalary weeklySalary : weeklySalaryList) {
                data.put(String.valueOf(weeklySalary.getDate()), weeklySalary.getTotalSalary());
            }
        }
        modelAndView.addObject("keySet", data.keySet());
        modelAndView.addObject("values", data.values());
        modelAndView.addObject("coach", coach.get());
        return modelAndView;
    }
}
