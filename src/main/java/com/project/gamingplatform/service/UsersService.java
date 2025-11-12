package com.project.gamingplatform.service;

import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // конструтор без шифрования
//    @Autowired
//    public UsersService(UsersRepository usersRepository) {
//        this.usersRepository = usersRepository;
//    }

    public Users addNew(Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);
        return savedUser;

//        users.setActive(true);
//        users.setRegistrationDate(new Date(System.currentTimeMillis()));
//        //encrypt user password during registration
//        users.setPassword(passwordEncoder.encode(users.getPassword()));
//        //-----------
//        Users savedUsers = usersRepository.save(users);
//        int userTypeId = users.getUserTypeId().getUserTypeId();
//        if(userTypeId == 1){
//            recruiterProfileRepository.save(new RecruiterProfile(savedUsers));
//        } else {
//            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUsers));
//        }
//        return savedUsers;
    }

    public Optional<Users> getUsersByUsername(String username){
        return usersRepository.findByUsername(username);
    }
}
