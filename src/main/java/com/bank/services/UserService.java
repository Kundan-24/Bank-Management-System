package com.bank.services;

import com.bank.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    void changePassword(String email, String oldPassword, String newPassword);

    
}
