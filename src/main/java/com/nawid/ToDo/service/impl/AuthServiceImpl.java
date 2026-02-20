package com.nawid.ToDo.service.impl;

import com.nawid.ToDo.dto.LoginDto;
import com.nawid.ToDo.dto.RegisterDto;
import com.nawid.ToDo.entity.Role;
import com.nawid.ToDo.entity.User;
import com.nawid.ToDo.exception.TodoAPIException;
import com.nawid.ToDo.repository.RoleRepo;
import com.nawid.ToDo.repository.UserRepo;
import com.nawid.ToDo.security.JwtTokenProvider;
import com.nawid.ToDo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String register(RegisterDto registerDto) {

        if(userRepo.existsByUsername(registerDto.getUsername())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST,"Username already exists");
        }

        if(userRepo.existsByEmail(registerDto.getEmail())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);
        userRepo.save(user);

        return "User Registered Successfully";
    }

    @Override
    public String login(LoginDto loginDto) {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }
}
