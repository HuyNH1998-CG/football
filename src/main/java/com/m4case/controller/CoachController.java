package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.WeeklySalary;
import com.m4case.service.ICoachService;
import com.m4case.service.IWeeklySalaryService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    ICoachService iCoachService;

    @Autowired
    IWeeklySalaryService iWeeklySalaryService;

//    @ModelAttribute("listWeeklySalary")
//    public ArrayList<WeeklySalary> weeklySalaries(){
//        return (ArrayList<WeeklySalary>) iWeeklySalaryService.findAll();
//    }


    @GetMapping("/show")
    public ModelAndView finAll(){
        ModelAndView modelAndView=new ModelAndView("/showCoach");
        modelAndView.addObject("listcoach",iCoachService.findAll());
        return modelAndView;
    }


    @GetMapping("/create")
    public ModelAndView showCreate(){
        ModelAndView modelAndView=new ModelAndView("/createCoach");
        modelAndView.addObject("coach",new Coach());
        return modelAndView;
    }


    @GetMapping("/edit/{id}")
    public ModelAndView showEdit(@PathVariable long id){
        Optional<Coach> coach=iCoachService.findById(id);
        if (coach.isPresent()){
            ModelAndView modelAndView=new ModelAndView("/editCoach");
            modelAndView.addObject("coach",coach.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView=new ModelAndView("/error.404");
            return modelAndView;

        }

    }


    @GetMapping("/delete/{id}")
    public ModelAndView showDelete(@PathVariable long id){
        Optional<Coach> coach=iCoachService.findById(id);
        if (coach.isPresent()){
            ModelAndView modelAndView=new ModelAndView("/deleteCoach");
            modelAndView.addObject("coach",coach.get());
            return modelAndView;
        }else {
            ModelAndView modelAndView=new ModelAndView("/error.404");
            return modelAndView;

        }
    }


    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute("coach") Coach coach){
        iCoachService.save(coach);
        ModelAndView modelAndView=new ModelAndView("createCoach");
        modelAndView.addObject("coach",new Coach());
        modelAndView.addObject("message","New Coach created successfully");
        return modelAndView;
    }


    @PostMapping("/edit/{id}")
    public ModelAndView edit(@ModelAttribute("coach") Coach coach) {
        iCoachService.save(coach);
        ModelAndView modelAndView = new ModelAndView("editCoach");
        modelAndView.addObject("coach", new Coach());
        modelAndView.addObject("message", "New Coach update successfully");
        return modelAndView;
    }


    @PostMapping("/delete/{id}")
    public ModelAndView delete(@ModelAttribute("coach") Coach coach){
        iCoachService.delete(coach.getId());
        return new ModelAndView("redirect:/coach/show");
    }

}
