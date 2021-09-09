package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.Player;
import com.m4case.model.WeeklySalary;
import com.m4case.service.ICoachService;
import com.m4case.service.IPlayerService;
import com.m4case.service.WeeklySalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;

@Controller
public class SalaryController {
    @Autowired
    private ICoachService coachService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private WeeklySalaryService weeklySalaryService;

    @GetMapping("/coachSalary/{id}")
    public ModelAndView coachSalaryr(@PathVariable long id) {
        Coach coach = coachService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/salary/coachSalary");
        modelAndView.addObject("coach",coach);
        return modelAndView;
    }
    @PostMapping("/coachSalary")
    public ModelAndView coachSalaryCalculator(@RequestParam long id, @RequestParam long salary, @RequestParam long bonus) {
        Coach coach = coachService.findById(id).get();
        WeeklySalary weeklySalary;
        weeklySalary = weeklySalaryService.findByCoach_IdAndDate(coach.getId(),new Date(System.currentTimeMillis()));
        if(weeklySalary == null){
            weeklySalary = new WeeklySalary();
            weeklySalary.setCoach(coach);
        }
        weeklySalary.setTotalSalary(salary+bonus);
        coach.setWeeklySalary(weeklySalaryService.saveSalary(weeklySalary));
        coachService.save(coach);
        return new ModelAndView("redirect:/coachProfile/" + coach.getId());
    }

    @GetMapping("/playerSalary/{id}")
    public ModelAndView playerSalary(@PathVariable long id) {
        Player player = playerService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/salary/playerSalary");
        modelAndView.addObject("player",player);
        return modelAndView;
    }
    @PostMapping("/playerSalary")
    public ModelAndView playerSalaryCalculator(@RequestParam long id, @RequestParam long salary, @RequestParam long bonus, @RequestParam long time, @RequestParam long bonus2) {
        Player player = playerService.findById(id).get();
        WeeklySalary weeklySalary;
        weeklySalary = weeklySalaryService.findByPlayer_IdAndDate(player.getId(),new Date(System.currentTimeMillis()));
        if(weeklySalary == null){
            weeklySalary = new WeeklySalary();
            weeklySalary.setPlayer(player);
        }
        weeklySalary.setTotalSalary((salary+bonus+bonus2)*time);
        player.setWeeklySalary(weeklySalaryService.saveSalary(weeklySalary));
        playerService.save(player);
        return new ModelAndView("redirect:/playerProfile/" + player.getId());
    }
}
