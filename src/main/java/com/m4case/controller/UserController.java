package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/u")
public class UserController {
    @Autowired
    IMyUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    ICoachService coachService;

    @Autowired
    IPlayerService playerService;

    @Autowired
    IGenderService genderService;

    @Autowired
    private IPositionService positionService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IHypeService hypeService;

    @Value("${uploadPart}")
    String uploadPart;


    @GetMapping("/landing")
    public ModelAndView landing(Authentication authentication) {
        MyUser user = userService.findByEmail(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/landing");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/home");
        List<Player> players = (List<Player>) playerService.findAll();
        List<Coach> coaches = (List<Coach>) coachService.findAll();
        Map<String, Long> data = new LinkedHashMap<>();
        Map<String, Long> data2 = new LinkedHashMap<>();
        for (Player player : players) {
            if (player.getWeeklySalary() != null) {
                data.put(player.getName(), player.getWeeklySalary().getTotalSalary());
            }
        }
        for (Coach coach : coaches) {
            if (coach.getWeeklySalary() != null) {
                data2.put(coach.getName(), coach.getWeeklySalary().getTotalSalary());
            }
        }
        modelAndView.addObject("keySet", data.keySet());
        modelAndView.addObject("values", data.values());
        modelAndView.addObject("keySet2", data2.keySet());
        modelAndView.addObject("values2", data2.values());
        modelAndView.addObject("players", players);
        modelAndView.addObject("coaches", coaches);
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

    @GetMapping("/profile")
    public ModelAndView userProfile(Authentication authentication) {
        MyUser myUser = userService.findByEmail(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/userDetail");
        modelAndView.addObject("user", myUser);
        return modelAndView;
    }

    @GetMapping("/createUserProfile")
    public ModelAndView createUserProfileForm(Authentication authentication) {
        MyUser myUser = userService.findByEmail(authentication.getName());
        ModelAndView modelAndView = new ModelAndView("/createUserProfile");
        modelAndView.addObject("user", myUser);
        modelAndView.addObject("genders", genderService.findAll());
        return modelAndView;
    }

    @PostMapping("/createUserProfile")
    public ModelAndView createUserProfile(@RequestAttribute MultipartFile file, @ModelAttribute("user") MyUser myUser) throws IOException {
        MyUser user = userService.findByEmail(myUser.getEmail());
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        user.setName(myUser.getName());
        user.setAge(myUser.getAge());
        user.setGender(myUser.getGender());
        user.setAvatar(fileName);
        userService.save(user);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/workProfile")
    public ModelAndView workProfile(Authentication authentication) {
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        List<String> role = new ArrayList<>();
        for (GrantedAuthority authority : roles) {
            role.add(authority.getAuthority());
        }
        if (role.get(0).equals("ROLE_COACH")) {
            Coach coach = coachService.findByEmail(authentication.getName());
            if (coach == null) {
                return new ModelAndView("redirect:/u/profileNotFound");
            }
            return new ModelAndView("redirect:/c/profile/" + coach.getId());
        } else {
            Player player = playerService.findByEmail(authentication.getName());
            if (player == null) {
                return new ModelAndView("redirect:/u/profileNotFound");
            }
            return new ModelAndView("redirect:/p/profile/" + player.getId());
        }
    }

    @GetMapping("/profileNotFound")
    public ModelAndView profileNotFound(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("/noProfile");
        modelAndView.addObject("user", userService.findByEmail(authentication.getName()));
        return modelAndView;
    }

    @GetMapping("/createProfile")
    public ModelAndView createProfile(Authentication authentication) {
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
            modelAndView.addObject("positions", positionService.findAll());
            modelAndView.addObject("hypes", hypeService.findAll());
            modelAndView.addObject("statuses", statusService.findAll());
        }
        return modelAndView;
    }

    @GetMapping("/editWorkProfile")
    public ModelAndView editWorkProfile(Authentication authentication) {
        MyUser myUser = userService.findByEmail(authentication.getName());
        if (myUser.getRole().getName().equals("ROLE_COACH")) {
            return new ModelAndView("redirect:/c/edit/" + coachService.findByEmail(myUser.getEmail()).getId());
        } else {
            return new ModelAndView("redirect:/p/edit/" + playerService.findByEmail(myUser.getEmail()).getId());
        }
    }

    @GetMapping("/editMyProfile")
    public ModelAndView editProfileForm(Authentication authentication) {
        MyUser user = userService.findByEmail(authentication.getName());
        if(user.getGender()!=null){
            ModelAndView modelAndView = new ModelAndView("/userProfileEdit");
            modelAndView.addObject("user", user);
            modelAndView.addObject("genders", genderService.findAll());
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/u/createUserProfile");
        }
    }

    @PostMapping("/editMyProfile")
    public ModelAndView editProfile(@ModelAttribute("user") MyUser myUser) {
        MyUser user = userService.findByEmail(myUser.getEmail());
        user.setName(myUser.getName());
        user.setAge(myUser.getAge());
        user.setGender(myUser.getGender());
        userService.save(user);
        return new ModelAndView("redirect:/u/landing");
    }


    @GetMapping("/changePassword")
    public ModelAndView changePassword() {
        ModelAndView modelAndView = new ModelAndView("/changePassword-checkpassword");
        modelAndView.addObject("user", new MyUser());
        return modelAndView;
    }

    @PostMapping("/changePassword")
    public ModelAndView checkPassword(Authentication authentication, @Valid @ModelAttribute("user") MyUser myUser, BindingResult bindingResult) {
        MyUser user = userService.findByEmail(authentication.getName());
        if (user.getPassword().equals(myUser.getPassword())) {
            ModelAndView modelAndView = new ModelAndView("/changePassword-newPassword");
            modelAndView.addObject("user", new MyUser());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/changePassword-checkpassword");
            bindingResult.rejectValue("password", "WrongPassword.MyUser.email");
            modelAndView.addObject("user", myUser);
            return modelAndView;
        }
    }

    @PostMapping("/changePass")
    public ModelAndView changePass(@Valid @ModelAttribute("user") MyUser myUser, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/changePassword-newPassword");
            modelAndView.addObject("user", myUser);
            return modelAndView;
        } else {
            MyUser user = userService.findByEmail(authentication.getName());
            user.setPassword(myUser.getPassword());
            userService.save(user);
            return new ModelAndView("redirect:/u/landing");
        }
    }

    @GetMapping("/changeAvatar")
    public ModelAndView changeAvatarForm(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("/changeUserAvatar");
        modelAndView.addObject("user", userService.findByEmail(authentication.getName()));
        return modelAndView;
    }

    @PostMapping("/changeAvatar")
    public ModelAndView changeAvatar(@RequestAttribute MultipartFile file, @ModelAttribute("user") MyUser myUser) throws IOException {
        myUser = userService.findById(myUser.getId()).get();
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        myUser.setAvatar(fileName);
        userService.save(myUser);
        return new ModelAndView("redirect:/u/profile/");
    }

    @GetMapping("/changeWorkAvatar")
    public ModelAndView changeWorkAvatar(Authentication authentication) {
        MyUser user = userService.findByEmail(authentication.getName());
        if (user.getRole().getName().equals("ROLE_COACH")) {
            Coach coach = coachService.findByEmail(user.getEmail());
            ModelAndView modelAndView = new ModelAndView("/coach/changeCoachAvatar");
            modelAndView.addObject("coach", coach);
            return modelAndView;
        } else {
            Player player = playerService.findByEmail(user.getEmail());
            ModelAndView modelAndView = new ModelAndView("/players/changePlayerAvatar");
            modelAndView.addObject("player", player);
            return modelAndView;
        }
    }
}
