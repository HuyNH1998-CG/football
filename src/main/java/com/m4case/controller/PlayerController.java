package com.m4case.controller;

import com.m4case.model.Player;
import com.m4case.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private IPlayerService playerService;

    @GetMapping("/list")
    public ModelAndView findAll(){
        ModelAndView modelAndView = new ModelAndView("/players/list");
        modelAndView.addObject("players", playerService.findAll());
        return modelAndView;
    }

    @GetMapping("/create-player")
    public ModelAndView showFormCreate(){
        ModelAndView modelAndView = new ModelAndView("/players/create");
        modelAndView.addObject("player", new Player());
        return modelAndView;
    }

    @PostMapping("/create-player")
    public ModelAndView createPlayer(@ModelAttribute("player") Player player){
        playerService.save(player);
        ModelAndView modelAndView = new ModelAndView("/players/create");
        modelAndView.addObject("player", new Player());
        modelAndView.addObject("message", "Create new Player successfully !");
        return modelAndView;
    }

    @GetMapping("/detail-player/{id}")
    public ModelAndView detail(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id));
        return modelAndView;
    }

    @GetMapping("/delete-player/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id));
        modelAndView.addObject("message", "Are you sure ???");
        return modelAndView;
    }
}
