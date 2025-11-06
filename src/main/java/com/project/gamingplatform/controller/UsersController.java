package com.project.gamingplatform.controller;

import com.project.gamingplatform.entity.GlobalRole;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model) {
//        model.addAttribute("user", new Users());
//        return "register";
//        Users users = new Users("val",
//                "val@gmail.com",
//                "123456",
//                true,
//                GlobalRole.ADMIN);
//
//        usersService.addNew(users);
        model.addAttribute("user", new Users());
        System.out.println("data is sent");
        return "registerForm";
    }

    @PostMapping("/register")
    public String userRegistration(@Valid Users users){
        //        model.addAttribute("user", new Users());
//        return "register";
//        Users users = new Users("val",
//                "val@gmail.com",
//                "123456",
//                true,
//                GlobalRole.ADMIN);
//
        usersService.addNew(users);
        System.out.println("data is saved");
        return "redirect:/users/register";
    }
}
