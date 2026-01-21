package com.project.gamingplatform.service;

import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminProfileService {
    private final UsersRepository usersRepository;

    public AdminProfileService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> getOne(Integer id){
        return usersRepository.findById(id);
    }
}
