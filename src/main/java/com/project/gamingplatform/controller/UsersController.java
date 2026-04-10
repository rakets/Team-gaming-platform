package com.project.gamingplatform.controller;

import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    // метод перехода на станицу регистрации
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Users());
        System.out.println("data is sent");
        return "registerForm";
    }

    // метод регистрации нового пользователя / возврата ошибки
    @PostMapping("/register/new")
    public String userRegistration(@Valid @ModelAttribute("user") Users user, BindingResult bindingResult, Model model){
        // обработка ошибки, если выслать пустую форму
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Error with registration");
            return "registerForm";
        }
        try {
            usersService.addNew(user);
            return "redirect:/login";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("user", user);
            model.addAttribute("error", "User already exists");
            return "registerForm";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Error with registration");
            return "registerForm";
        }
    }

    @GetMapping("/login")
    public String login(){
        System.out.println("user start login");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
