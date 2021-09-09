package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.service.ICoachService;
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
import java.util.Optional;

@Controller
public class CoachController {
    @Autowired
    ICoachService iCoachService;

    @Autowired
    IWeeklySalaryService iWeeklySalaryService;

    @Value("${uploadPart}")
    String uploadPart;

    @GetMapping("/show")
    public ModelAndView finAll() {
        ModelAndView modelAndView = new ModelAndView("/coach/showCoach");
        modelAndView.addObject("listcoach", iCoachService.findAll());
        return modelAndView;
    }

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
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDelete(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        if (coach.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/coach/deleteCoach");
            modelAndView.addObject("coach", coach.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/createCoach")
    public ModelAndView createCoach(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Coach coach) throws IOException {
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File("E:\\CodeGym\\M4-Case\\src\\main\\resources\\static\\", fileName));
        coach.setAvatar(fileName);
        iCoachService.save(coach);
        return new ModelAndView("redirect:/home");
    }


    @PostMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute("coach") Coach coach) {
        iCoachService.save(coach);
        return new ModelAndView("redirect:/coachProfile/" + coach.getId());
    }


    @PostMapping("/delete/{id}")
    public ModelAndView delete(@ModelAttribute("coach") Coach coach) {
        iCoachService.delete(coach.getId());
        return new ModelAndView("redirect:/coach/show");
    }

    @GetMapping("/changeCoachAvatar/{id}")
    public ModelAndView showChangeCoachAvatar(@PathVariable long id) {
        Optional<Coach> coach = iCoachService.findById(id);
        if (coach.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/coach/changeCoachAvatar");
            modelAndView.addObject("coach", coach.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/changeCoachAvatar")
    public ModelAndView changeCoachAvatar(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Coach coach) throws IOException {
        coach = iCoachService.findById(coach.getId()).get();
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        coach.setAvatar(fileName);
        iCoachService.save(coach);
        return new ModelAndView("redirect:/coachProfile/" + coach.getId());
    }

    @GetMapping("/coachProfile/{id}")
    public ModelAndView coachProfile(@PathVariable long id){
        Optional<Coach> coach = iCoachService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/coach/coachDetail");
        modelAndView.addObject("coach", coach.get());
        return modelAndView;
    }
}
