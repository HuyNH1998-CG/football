package com.m4case.controller;

import com.m4case.model.Coach;
import com.m4case.model.Player;
import com.m4case.service.*;
import com.m4case.validator.EmailValidator;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/player")
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
    @Value("${uploadPart}")
    String uploadPart;



    @GetMapping("/list")
    public ModelAndView findAll() {
        ModelAndView modelAndView = new ModelAndView("/players/listPlayer");
        modelAndView.addObject("players", playerService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showFormCreate() {
        ModelAndView modelAndView = new ModelAndView("/players/createPlayer");
        modelAndView.addObject("player", new Player());
        modelAndView.addObject("message", "Create new Player successfully !");
        return modelAndView;
    }


//    @PostMapping("/createPlayer")
//    public ModelAndView createPlayer(@RequestAttribute MultipartFile file, @ModelAttribute("player") Player player) throws IOException {
//        String fileName = file.getOriginalFilename();
//        FileCopyUtils.copy(file.getBytes(), new File("E:\\CodeGym\\M4-Case\\src\\main\\resources\\static\\", fileName));
//        player.setAvatar(fileName);
//        int bmi = player.getWeight() / (player.getHeight()/100*2);
//        player.setBmi(bmi);
//        player.setStatus("Playing");
//        playerService.save(player);
//        return new ModelAndView("/home");
//    }

//    @GetMapping("/create")
//    public ModelAndView createPlayer(@ModelAttribute("player") Player player) {
//        playerService.save(player);
//        ModelAndView modelAndView = new ModelAndView("/players/create");
//        modelAndView.addObject("player", new Player());
//        modelAndView.addObject("message", "Create new Player successfully !");
//        return modelAndView;
//    }

    @PostMapping("/create")
    public ModelAndView create(@RequestParam("upAvatar")MultipartFile upAvatar, @ModelAttribute("player") Player player, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            ModelAndView modelAndView=new ModelAndView("/players/createPlayer");
            modelAndView.addObject("player", player);
            modelAndView.addObject("message", "!Create new Player !successfully");

            return modelAndView;
        }
        String namefile=upAvatar.getOriginalFilename();
        try {
            FileCopyUtils.copy(upAvatar.getBytes(),new File("/Users/tam/Desktop/caseCode/football/src/main/resources/static/"+namefile));

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setAvatar("/"+namefile);
        int bmi = player.getWeight() / (player.getHeight()*2);
        player.setBmi(bmi);
        player.setStatus("Playing");
        playerService.save(player);
        return new ModelAndView("redirect:/player/list");
    }
    @GetMapping("delete/{id}")
    public ModelAndView showDelete(@PathVariable long id){
        Optional<Player> player=playerService.findById(id);
        if (player.isPresent()){
            ModelAndView modelAndView=new ModelAndView("/players/delete");
            modelAndView.addObject("player",player.get());
            modelAndView.addObject("message", "Are you sure ???");

            return modelAndView;
        }
        else {
            return new ModelAndView("/erroe.404");
        }
    }
    @GetMapping("/edit/{id}")
    public ModelAndView showEdit(@PathVariable long id){
        Optional<Player> player=playerService.findById(id);
        if (player.isPresent()){
            ModelAndView modelAndView=new ModelAndView("/players/editPlayer");
            modelAndView.addObject("ahihi",player.get());
            return modelAndView;
        }
        else {
            return new ModelAndView("/erroe.404");
        }
    }
    @PostMapping("/delete/{id}")
    public ModelAndView delete(@ModelAttribute("player")Player player){
        playerService.delete(player.getId());
        return new ModelAndView("redirect:/player/list");
    }
    @PostMapping("/edit/{id}")
    public ModelAndView edit(@RequestParam("tam") MultipartFile Upavatar,@ModelAttribute("player") Player player,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            ModelAndView modelAndView=new ModelAndView("/players/editPlayer");
            return modelAndView;
        }
        String nameFile=Upavatar.getOriginalFilename();
        try {
            FileCopyUtils.copy(Upavatar.getBytes(),new File("/Users/tam/Desktop/caseCode/football/src/main/resources/static/"+nameFile));
            player.setAvatar("/"+nameFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int bmi = player.getWeight() / (player.getHeight()/100*2);
        player.setBmi(bmi);
        player.setStatus("Playing");
        playerService.save(player);
        return new ModelAndView("redirect:/player/list");
    }

    @GetMapping("/detail-player/{id}")
    public ModelAndView detail(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/players/detailPlayer");
        modelAndView.addObject("player", playerService.findById(id).get());
        return modelAndView;
    }

//    @GetMapping("/delete-player/{id}")
//    public ModelAndView showFormDelete(@PathVariable Long id) {
//        ModelAndView modelAndView = new ModelAndView("/players/detail");
//        modelAndView.addObject("player", playerService.findById(id));
//        modelAndView.addObject("message", "Are you sure ???");
//        return modelAndView;
//    }
}
