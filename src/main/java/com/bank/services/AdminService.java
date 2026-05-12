package com.bank.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bank.dto.UserDto;

public interface AdminService {

    UserDto createUserByAdmin(UserDto userDto);

    Page<UserDto> getAllUsers(Pageable pageable);

    Page<UserDto> searchUsers(String keyword, Pageable pageable);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    long countUsers();
    long countBlockedUsers();
}
