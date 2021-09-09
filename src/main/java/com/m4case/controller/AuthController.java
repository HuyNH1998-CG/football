package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.service.*;
import com.m4case.validator.EmailChecker;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

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

//    @Autowired
//    IWeeklySalaryService weeklySalaryService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private EmailChecker emailChecker;

    @GetMapping("/")
    public ModelAndView landing() {
        return new ModelAndView("/landing");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/home");
        modelAndView.addObject("coaches", coachService.findAll());
        return modelAndView;
    }

    @PostMapping("/home")
    public ModelAndView homeSearch(@RequestParam Optional<String> min, @RequestParam Optional<String> max) {
        ModelAndView modelAndView = new ModelAndView("/home");
        if (min.isPresent() && max.isPresent()) {
            if (!min.get().equals("") && !max.get().equals("")) {
                Long salMin = Long.parseLong(min.get());
                Long salMax = Long.parseLong(max.get());
                modelAndView.addObject("players", playerService.findBySalaryBetween(salMin, salMax));
            } else if (!min.get().equals("")) {
                Long sal = Long.parseLong(min.get());
                modelAndView.addObject("players", playerService.findAllBySalaryGreaterThanEqual(sal));
            } else {
                if (!max.get().equals("")) {
                    Long sal = Long.parseLong(max.get());
                    modelAndView.addObject("players", playerService.findBySalaryBetween(0L, sal));
                } else {
                    modelAndView.addObject("players", playerService.findAll());
                }
            }
        } else {
            modelAndView.addObject("players", playerService.findAll());
        }
        modelAndView.addObject("coaches", coachService.findAll());
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/createUser")
    public ModelAndView showCreate() {
        ModelAndView modelAndView = new ModelAndView("/createUser");
        modelAndView.addObject("myUser", new MyUser());
        modelAndView.addObject("roles", roleService.findAll());
        return modelAndView;
    }

    @PostMapping("/createUser")
    public ModelAndView create(@Valid @ModelAttribute("myUser") MyUser myUser, BindingResult bindingResult) {
        String role = myUser.getRole().getName();
        emailValidator.validate(myUser, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/createUser");
            modelAndView.addObject("myUser", myUser);
            modelAndView.addObject("roles", roleService.findAll());
            return modelAndView;
        }
        if (role.equals("ROLE_ADMIN")) {
            userService.save(myUser);
            ModelAndView modelAndView = new ModelAndView("/landing");
            modelAndView.addObject("message", "AdminCreated");
            return modelAndView;
        } else if (role.equals("ROLE_COACH")) {
            ModelAndView modelAndView = new ModelAndView("/coach/createCoach");
            userService.save(myUser);
            modelAndView.addObject("coachAccount", myUser);
            modelAndView.addObject("coach", new Coach());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/players/createPlayer");
            userService.save(myUser);
            modelAndView.addObject("playerAccount", myUser);
            modelAndView.addObject("player", new Player());
            return modelAndView;
        }
    }

    @GetMapping("/forgotPassword")
    public ModelAndView showForgotPassword(){
        ModelAndView modelAndView = new ModelAndView("/forgotPassword");
        modelAndView.addObject("user",new MyUser());
        return modelAndView;
    }

    @PostMapping("/forgotPassword")
    public ModelAndView forgotPassword(@Valid @ModelAttribute("user") MyUser myUser,BindingResult bindingResult){
        emailChecker.validate(myUser,bindingResult);
        if(bindingResult.hasFieldErrors()){
            ModelAndView modelAndView = new ModelAndView("/forgotPassword");
            modelAndView.addObject("user",myUser);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/showPassword");
            modelAndView.addObject("user",userService.loadUserByUsername(myUser.getEmail()));
            return modelAndView;
        }
    }
    @GetMapping("/userProfile/{email}")
    public ModelAndView userProfile(@PathVariable String email){
        MyUser myUser = userService.findByEmail(email);
        ModelAndView modelAndView = new ModelAndView("/userDetail");
        modelAndView.addObject("user",myUser);
        return modelAndView;
    }
}
