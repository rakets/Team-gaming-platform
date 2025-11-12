package com.project.gamingplatform.controller;

import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/users")
@RequestMapping
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Users());
        System.out.println("data is sent");
        return "registerForm";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users){
        usersService.addNew(users);
        System.out.println("data is saved");
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(){
        System.out.println("user start login");
        return "login";
    }


}
