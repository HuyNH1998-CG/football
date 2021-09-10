package com.m4case.controller;

import com.m4case.model.Player;
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
    private IPositionService positionService;
    @Autowired
    private IStatusService statusService;
    @Autowired
    private IHypeService hypeService;

    @Value("${uploadPart}")
    String uploadPart;


    @GetMapping("/list")
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView("/players/list");
        modelAndView.addObject("players", playerService.findAll());
        return modelAndView;
    }

    @GetMapping("/editplayer/{id}")
    public ModelAndView editPlayer(@PathVariable long id){
        ModelAndView modelAndView = new ModelAndView("/players/edit");
        modelAndView.addObject("player", playerService.findById(id).get());
        modelAndView.addObject("positions", positionService.findAll());
        modelAndView.addObject("hypes", hypeService.findAll());
        modelAndView.addObject("statuses", statusService.findAll());
        return modelAndView;
    }
    @GetMapping("/coacheditplayer/{id}")
    public ModelAndView coachEditPlayer(@PathVariable long id){
        ModelAndView modelAndView = new ModelAndView("/players/changePlayerbyCoach");
        modelAndView.addObject("player", playerService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("/createPlayer")
    public ModelAndView createPlayer(@RequestAttribute MultipartFile file, @ModelAttribute("player") Player player) throws IOException {
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        player.setAvatar(fileName);
        float weight = player.getWeight();
        float height = player.getHeight();
        float bmi = weight / (height/100*2);
        player.setBmi(bmi);
        player = (Player) playerService.saveObj(player);
        return new ModelAndView("redirect:/p/playerProfile/" + player.getId());
    }

    @GetMapping("/playerProfile/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id).get());
        return modelAndView;
    }

    @GetMapping("/delete-player/{id}")
    public ModelAndView showFormDelete(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/players/detail");
        modelAndView.addObject("player", playerService.findById(id).get());
        modelAndView.addObject("message", "Are you sure ???");
        return modelAndView;
    }

    @GetMapping("/changePlayerAvatar/{id}")
    public ModelAndView showChangeCoachAvatar(@PathVariable long id) {
        Optional<Player> player = playerService.findById(id);
        if (player.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/players/changePlayerAvatar");
            modelAndView.addObject("player", player.get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/changePlayerAvatar")
    public ModelAndView changeCoachAvatar(@RequestAttribute MultipartFile file, @ModelAttribute("coach") Player player) throws IOException {
        player = playerService.findById(player.getId()).get();
        String fileName = file.getOriginalFilename();
        FileCopyUtils.copy(file.getBytes(), new File(uploadPart, fileName));
        player.setAvatar(fileName);
        playerService.save(player);
        return new ModelAndView("redirect:/p/playerProfile/" + player.getId());
    }
}
