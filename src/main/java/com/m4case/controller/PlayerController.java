package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.MyUser;
import com.m4case.model.Player;
import com.m4case.model.WeeklySalary;
import com.m4case.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/p")
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
    private IPositionService positionService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IHypeService hypeService;

    @Value("${uploadPart}")
    String uploadPart;

    @GetMapping("/edit/{id}")
    public ModelAndView editPlayer(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/players/edit");
        modelAndView.addObject("player", playerService.findById(id).get());
        modelAndView.addObject("positions", positionService.findAll());
        modelAndView.addObject("hypes", hypeService.findAll());
        modelAndView.addObject("statuses", statusService.findAll());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute("player") Player player) {
        Player player1 = playerService.findById(player.getId()).get();
        player1.setName(player.getName());
        player1.setStatus(player.getStatus());
        player1.setBirthday(player.getBirthday());
        player1.setHype(player.getHype());
        player1.setPosition(player.getPosition());
        player1.setSalary(player.getSalary());
        player1.setHometown(player.getHometown());
        player1.setWeight(player.getWeight());
        player1.setHeight(player.getHeight());
        float weight = player.getWeight();
        float height = player.getHeight();
        float bmi = weight / (height / 100 * 2);
        player1.setBmi(bmi);
        player1 = (Player) playerService.saveObj(player1);
        return new ModelAndView("redirect:/p/profile/" + player1.getId());
    }

    @GetMapping("/coacheditplayer/{id}")
    public ModelAndView coachEditPlayerForm(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/players/changePlayerbyCoach");
        modelAndView.addObject("player", playerService.findById(id).get());
        modelAndView.addObject("positions", positionService.findAll());
        modelAndView.addObject("hypes", hypeService.findAll());
        modelAndView.addObject("statuses", statusService.findAll());
        return modelAndView;
    }

    @PostMapping("/coacheditplayer")
    public ModelAndView coachEditPlayer(@ModelAttribute("player") Player player) {
        Player realPlayer = playerService.findById(player.getId()).get();
        realPlayer.setHeight(player.getHeight());
        realPlayer.setWeight(player.getWeight());
        realPlayer.setPosition(player.getPosition());
        realPlayer.setHype(player.getHype());
        realPlayer.setStatus(player.getStatus());
        playerService.save(realPlayer);
        return new ModelAndView("redirect:/u/home");
    }


    @GetMapping("/delete/{id}")
    public ModelAndView showDelete(@PathVariable long id) {
        Optional<Player> player = playerService.findById(id);
        if (player.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/players/delete");
            modelAndView.addObject("player", player.get());
            return modelAndView;
        } else {
            return new ModelAndView("/focus-2/page-error-404");
        }
    }

    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam long id) {
        Player coach = playerService.findById(id).get();
        MyUser user = userService.findByEmail(coach.getEmail());
        weeklySalaryService.deleteAllByPlayer_Id(coach.getId());
        playerService.delete(id);
        userService.delete(user.getId());
        return new ModelAndView("redirect:/a/playerList");
    }

    @PostMapping("/create")
    public ModelAndView createPlayer(@RequestAttribute MultipartFile file, @ModelAttribute("player") Player player) throws IOException {
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        player.setAvatar(fileName);
        float weight = player.getWeight();
        float height = player.getHeight();
        float bmi = weight / (height / 100 * 2);
        player.setBmi(bmi);
        player = (Player) playerService.saveObj(player);
        return new ModelAndView("redirect:/p/profile/" + player.getId());
    }

    @GetMapping("/profile/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        Optional<Player> player = playerService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        Map<String, Long> data = new LinkedHashMap<>();
        List<WeeklySalary> weeklySalaryList = (List<WeeklySalary>) weeklySalaryService.findByPlayer_Id(id);
        if(weeklySalaryList != null){
            for (WeeklySalary weeklySalary : weeklySalaryList) {
                data.put(String.valueOf(weeklySalary.getDate()), weeklySalary.getTotalSalary());
            }
        }
        modelAndView.addObject("keySet", data.keySet());
        modelAndView.addObject("values", data.values());
        modelAndView.addObject("player", player.get());
        return modelAndView;
    }

    @GetMapping("/changeAvatar/{id}")
    public ModelAndView showChangeCoachAvatar(@PathVariable long id) {
        Optional<Player> player = playerService.findById(id);
        ModelAndView modelAndView;
        if (player.isPresent()) {
            modelAndView = new ModelAndView("/players/changePlayerAvatar");
            modelAndView.addObject("player", player.get());
        } else {
            modelAndView = new ModelAndView("/focus-2/page-error-404");
        }
        return modelAndView;
    }

    @PostMapping("/changeAvatar")
    public ModelAndView changeCoachAvatar(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Player player) throws IOException {
        player = playerService.findById(player.getId()).get();
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        player.setAvatar(fileName);
        playerService.save(player);
        return new ModelAndView("redirect:/p/profile/" + player.getId());
    }
}
