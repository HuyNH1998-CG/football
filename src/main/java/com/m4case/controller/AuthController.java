package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.service.ICoachService;
import com.m4case.service.IMyUserService;
import com.m4case.service.IPlayerService;
import com.m4case.service.IRoleService;
import com.m4case.validator.EmailChecker;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private EmailChecker emailChecker;

    @GetMapping("/landing")
    public ModelAndView landing() {
        return new ModelAndView("/landing");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/home");
        modelAndView.addObject("players", playerService.findAll());
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
    @GetMapping("/userProfile")
    public ModelAndView userProfile(Authentication authentication){
        MyUser myUser = userService.findByEmail(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/userDetail");
        modelAndView.addObject("user",myUser);
        return modelAndView;
    }

    @GetMapping("/workProfile")
    public ModelAndView workProfile(Authentication authentication){
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        List<String> role = new ArrayList<>();
        for (GrantedAuthority authority : roles){
            role.add(authority.getAuthority());
        }
        if(role.get(0).equals("ROLE_COACH")){
            Coach coach = coachService.findByEmail(authentication.getName());
            if(coach==null){
                return new ModelAndView("redirect:/profileNotFound");
            }
            return new ModelAndView("redirect:/coachProfile/" + coach.getId());
        } else {
            Player player = playerService.findByEmail(authentication.getName());
            if(player==null){
                return new ModelAndView("redirect:/profileNotFound");
            }
            return new ModelAndView("redirect:/playerProfile/" + player.getId());
        }
    }

    @GetMapping("/profileNotFound")
    public ModelAndView profileNotFound(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView("/noProfile");
        modelAndView.addObject("user",userService.findByEmail(authentication.getName()));
        return modelAndView;
    }

    @GetMapping("/createProfile")
    public ModelAndView createProfile(Authentication authentication){
        MyUser myUser = userService.findByEmail(authentication.getName());
        ModelAndView modelAndView;
        if (myUser.getRole().getName().equals("ROLE_COACH")) {
            modelAndView = new ModelAndView("/coach/createCoach");
            modelAndView.addObject("coachAccount", myUser);
            modelAndView.addObject("coach", new Coach());
        } else {
            modelAndView = new ModelAndView("/players/createPlayer");
            modelAndView.addObject("playerAccount", myUser);
            modelAndView.addObject("player", new Player());
        }
        return modelAndView;
    }

    @GetMapping("/changePassword")
    public ModelAndView changePassword(){
        ModelAndView modelAndView = new ModelAndView("/changePassword-checkpassword");
        modelAndView.addObject("user",new MyUser());
        return modelAndView;
    }

    @PostMapping("/changePassword")
    public ModelAndView checkPassword(Authentication authentication,@Valid @ModelAttribute("user") MyUser myUser, BindingResult bindingResult){
        MyUser user = userService.findByEmail(authentication.getName());
        if(user.getPassword().equals(myUser.getPassword())){
            ModelAndView modelAndView = new ModelAndView("/changePassword-newPassword");
            modelAndView.addObject("user", new MyUser());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/changePassword-checkpassword");
            bindingResult.rejectValue("password","WrongPassword.MyUser.email");
            modelAndView.addObject("user",myUser);
            return modelAndView;
        }
    }

    @PostMapping("/changePass")
    public ModelAndView changePass(@Valid @ModelAttribute("user") MyUser myUser, BindingResult bindingResult, Authentication authentication){
        if(bindingResult.hasFieldErrors()){
            ModelAndView modelAndView = new ModelAndView("/changePassword-newPassword");
            modelAndView.addObject("user",myUser);
            return modelAndView;
        } else {
            MyUser user = userService.findByEmail(authentication.getName());
            user.setPassword(myUser.getPassword());
            userService.save(user);
            ModelAndView modelAndView =  new ModelAndView("/landing");
            modelAndView.addObject("message","Password Change Successful");
            return modelAndView;
        }
    }
}
