package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.service.ICoachService;
import com.m4case.service.IMyUserService;
import com.m4case.service.IPlayerService;
import com.m4case.service.IRoleService;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    IMyUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    ICoachService coachService;

    @Autowired
    IPlayerService playerService;

    @Autowired
    private EmailValidator emailValidator;
    @GetMapping("/")
    public ModelAndView landing() {
        return new ModelAndView("/landing");
    }

    @GetMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("/test");
    }

    @GetMapping("/admin")
    public ModelAndView admin() {
        return new ModelAndView("/test");
    }

    @GetMapping("/coach")
    public ModelAndView coach(Authentication authentication) {
        String email = authentication.getName();
        Coach coach = coachService.findByEmail(email);
        ModelAndView modelAndView = new ModelAndView("/testCoach");
        modelAndView.addObject("coach", coach);
        return modelAndView;
    }

    @GetMapping("/createUser")
    public ModelAndView showCreate(){
        ModelAndView modelAndView = new ModelAndView("/createUser");
        modelAndView.addObject("myUser", new MyUser());
        modelAndView.addObject("roles",roleService.findAll());
        return modelAndView;
    }
    @PostMapping("/createUser")
    public ModelAndView create(@Valid @ModelAttribute("myUser") MyUser myUser, BindingResult bindingResult){
        String role = myUser.getRole().getName();
        emailValidator.validate(myUser,bindingResult);
        if(bindingResult.hasFieldErrors()){
            ModelAndView modelAndView = new ModelAndView("/createUser");
            modelAndView.addObject("myUser", myUser);
            modelAndView.addObject("roles",roleService.findAll());
            return modelAndView;
        }
        if(role.equals("ROLE_ADMIN")){
            userService.save(myUser);
            ModelAndView modelAndView = new ModelAndView("/landing");
            modelAndView.addObject("message", "AdminCreated");
            return modelAndView;
        } else if (role.equals("ROLE_COACH")){
            ModelAndView modelAndView = new ModelAndView("/createCoach");
            userService.save(myUser);
            modelAndView.addObject("coachAccount",myUser);
            modelAndView.addObject("coach",new Coach());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/createPlayer");
            userService.save(myUser);
            modelAndView.addObject("playerAccount",myUser);
            modelAndView.addObject("player",new Player());
            return modelAndView;
        }
    }

    @PostMapping("/createCoach")
    public ModelAndView createCoach(@ModelAttribute("coach") Coach coach){
        long weekly = coach.getSalary() + coach.getBonus();
        coach.setWeeklySalary(weekly);
        coachService.save(coach);
        ModelAndView modelAndView = new ModelAndView("/landing");
        modelAndView.addObject("message", "Created");
        return modelAndView;
    }
}
