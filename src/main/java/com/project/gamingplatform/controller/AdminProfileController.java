package com.project.gamingplatform.controller;

import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.service.AdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class AdminProfileController {
    private final UsersRepository usersRepository;
    private final AdminProfileService adminProfileService;

    public AdminProfileController(UsersRepository usersRepository, AdminProfileService adminProfileService) {
        this.usersRepository = usersRepository;
        this.adminProfileService = adminProfileService;
    }

    private String adminProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByUsername(currentUsername).orElseThrow(() -> new
                    UsernameNotFoundException("Could not found user"));
            Optional<Users> recruiterProfile = adminProfileService.getOne(user.getUserId());
            if (!recruiterProfile.isEmpty()){
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "admin-profile";
    }


}
