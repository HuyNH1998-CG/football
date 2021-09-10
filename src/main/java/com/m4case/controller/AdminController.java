package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.model.WeeklySalary;
import com.m4case.service.*;
import com.m4case.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping("/a")
public class AdminController {
    @Autowired
    private ICoachService coachService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private WeeklySalaryService weeklySalaryService;

    @Autowired
    private IMyUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private IPositionService positionService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IHypeService hypeService;

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
            modelAndView.addObject("positions", positionService.findAll());
            modelAndView.addObject("hypes", hypeService.findAll());
            modelAndView.addObject("statuses", statusService.findAll());
            return modelAndView;
        }
    }

    @GetMapping("/getCoachList")
    public ModelAndView coachList() {
        ModelAndView modelAndView = new ModelAndView("/coach/listCoachSalary");
        modelAndView.addObject("coaches", coachService.findAll());
        return modelAndView;
    }

    @GetMapping("/getPlayerList")
    public ModelAndView playerList() {
        ModelAndView modelAndView = new ModelAndView("/players/listPlayerSalary");
        modelAndView.addObject("players", playerService.findAll());
        return modelAndView;
    }

    @GetMapping("/coachSalary/{id}")
    public ModelAndView coachSalary(@PathVariable long id) {
        Coach coach = coachService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/salary/coachSalary");
        modelAndView.addObject("coach", coach);
        return modelAndView;
    }

    @PostMapping("/coachSalary")
    public ModelAndView coachSalaryCalculator(@RequestParam long id, @RequestParam long salary, @RequestParam long bonus) {
        Coach coach = coachService.findById(id).get();
        WeeklySalary weeklySalary;
        weeklySalary = weeklySalaryService.findByCoach_IdAndDate(coach.getId(), new Date(System.currentTimeMillis()));
        if (weeklySalary == null) {
            weeklySalary = new WeeklySalary();
            weeklySalary.setCoach(coach);
        }
        weeklySalary.setTotalSalary(salary + bonus);
        coach.setWeeklySalary(weeklySalaryService.saveSalary(weeklySalary));
        coachService.save(coach);
        return new ModelAndView("redirect:/c/coachProfile/" + coach.getId());
    }

    @GetMapping("/playerSalary/{id}")
    public ModelAndView playerSalary(@PathVariable long id) {
        Player player = playerService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/salary/playerSalary");
        modelAndView.addObject("player", player);
        return modelAndView;
    }

    @PostMapping("/playerSalary")
    public ModelAndView playerSalaryCalculator(@RequestParam long id, @RequestParam long salary, @RequestParam long bonus, @RequestParam long time, @RequestParam long bonus2) {
        Player player = playerService.findById(id).get();
        WeeklySalary weeklySalary;
        weeklySalary = weeklySalaryService.findByPlayer_IdAndDate(player.getId(), new Date(System.currentTimeMillis()));
        if (weeklySalary == null) {
            weeklySalary = new WeeklySalary();
            weeklySalary.setPlayer(player);
        }
        weeklySalary.setTotalSalary((salary + bonus + bonus2) * time);
        player.setWeeklySalary(weeklySalaryService.saveSalary(weeklySalary));
        playerService.save(player);
        return new ModelAndView("redirect:/p/playerProfile/" + player.getId());
    }
}
