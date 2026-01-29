package com.project.gamingplatform.controller;

import com.project.gamingplatform.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final UsersService usersService;

    @Autowired
    public DashboardController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/dashboard/")
    public String showDashboard(Model model) {
//      Object currentUser = usersService.getCurrentUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        System.out.println("dashboard user: " + currentUserName);
        model.addAttribute("username", currentUserName);
        return "dashboard";
    }
}