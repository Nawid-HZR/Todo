package com.nawid.ToDo.service;

import com.nawid.ToDo.dto.LoginDto;
import com.nawid.ToDo.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}
