package com.m4case.controller;

import com.m4case.model.Player;
import com.m4case.service.*;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class PlayerController {
    @Autowired
    IMyUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    ICoachService coachService;

    @Autowired
    IPlayerService playerService;

    @Autowired
    IWeeklySalaryService weeklySalaryService;

    @Autowired
    private EmailValidator emailValidator;


    @GetMapping("/list")
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView("/players/list");
        modelAndView.addObject("players", playerService.findAll());
        return modelAndView;
    }

//    @GetMapping("/create-player")
//    public ModelAndView showFormCreate() {
//        ModelAndView modelAndView = new ModelAndView("/players/create");
//        modelAndView.addObject("player", new Player());
//        return modelAndView;
//    }

    @PostMapping("/createPlayer")
    public ModelAndView createPlayer(@RequestAttribute MultipartFile file, @ModelAttribute("player") Player player) throws IOException {
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File("E:\\CodeGym\\M4-Case\\src\\main\\resources\\static\\", fileName));
        player.setAvatar(fileName);
        int bmi = player.getWeight() / (player.getHeight()/100*2);
        player.setBmi(bmi);
        player.setStatus("Playing");
        playerService.save(player);
        return new ModelAndView("/home");
    }

//    @PostMapping("/create-player")
//    public ModelAndView createPlayer(@ModelAttribute("player") Player player) {
//        playerService.save(player);
//        ModelAndView modelAndView = new ModelAndView("/players/create");
//        modelAndView.addObject("player", new Player());
//        modelAndView.addObject("message", "Create new Player successfully !");
//        return modelAndView;
//    }

    @GetMapping("/detail-player/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id));
        return modelAndView;
    }

    @GetMapping("/delete-player/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id));
        modelAndView.addObject("message", "Are you sure ???");
        return modelAndView;
    }
}
