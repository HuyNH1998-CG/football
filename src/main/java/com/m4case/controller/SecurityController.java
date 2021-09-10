package com.m4case.controller;

import com.m4case.model.ForgotEmailToken;
import com.m4case.model.MyUser;
import com.m4case.repository.IForgotTokenRepository;
import com.m4case.service.*;
import com.m4case.validator.EmailChecker;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SecurityController {
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

    @Autowired
    private IForgotTokenRepository iForgotTokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public ModelAndView landingPage(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView("/landing");
        modelAndView.addObject("user", userService.findByEmail(authentication.getName()));
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public ModelAndView showForgotPassword() {
        ModelAndView modelAndView = new ModelAndView("/forgotPassword");
        modelAndView.addObject("user", new MyUser());
        return modelAndView;
    }

    @PostMapping("/forgotPassword")
    public ModelAndView forgotPassword(@Valid @ModelAttribute("user") MyUser myUser, BindingResult bindingResult) {
        emailChecker.validate(myUser, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/forgotPassword");
            modelAndView.addObject("user", myUser);
            return modelAndView;
        } else {
            ForgotEmailToken token = new ForgotEmailToken(userService.findByEmail(myUser.getEmail()));
            iForgotTokenRepository.save(token);
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(myUser.getEmail());
            email.setSubject("Forgot your password?");
            email.setFrom("kanze911@gmail.com");
            email.setText("We received a forgot password SOS from you or someone imposing you. Use the following link to reset your password" +
                    " http://localhost:8080/confirm-reset?token=" + token.getConfirmToken());
            emailService.sendEmail(email);
            ModelAndView modelAndView = new ModelAndView("/showPassword");
            modelAndView.addObject("message", "Request sent, Please Check inbox or spam to reset your password");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/confirm-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateToken(@RequestParam("token") String forgotEmailToken) {
        ForgotEmailToken token = iForgotTokenRepository.findByConfirmToken(forgotEmailToken);
        ModelAndView modelAndView;
        if (token != null) {
            MyUser myUser = userService.findByEmail(token.getUser().getEmail());
            modelAndView = new ModelAndView("/resetPassword");
            modelAndView.addObject("user",myUser);
            return modelAndView;
        } else {
            return new ModelAndView("/error-404");
        }
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(@ModelAttribute("user") MyUser myUser){
        MyUser user = userService.findByEmail(myUser.getEmail());
        user.setPassword(myUser.getPassword());
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("resetSuccess");
        modelAndView.addObject("message", "Password Reset please login");
        return modelAndView;
    }
}
