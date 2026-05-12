package com.bank.services.impl;

import com.bank.repositories.RoleRepo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bank.dto.UserDto;
import com.bank.entities.Role;
import com.bank.entities.User;
import com.bank.exceptions.ResourceNotFoundException;
import com.bank.repositories.UserRepo;
import com.bank.services.AdminService;

@Service
public class AdminServiceImp implements AdminService{

    private final RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired 
    private ModelMapper mapper;

    AdminServiceImp(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }


    @Override
    public Page<UserDto> searchUsers(String keyword, Pageable pageable) {
        return userRepo.searchUsers(keyword, pageable).map(user -> mapper.map(user, UserDto.class));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User oldUser = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        oldUser.setName(userDto.getName());
        oldUser.setEmail(userDto.getEmail());
        oldUser.setPhoneNumber(userDto.getPhoneNumber());
        User save = userRepo.save(oldUser);
        return mapper.map(save, UserDto.class);
    }

    @Override
    public void blockUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user.setEnabled(false);
        userRepo.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        user.setEnabled(true);
        userRepo.save(user);
    }

    @Override
    public UserDto createUserByAdmin(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        Role roleUser = roleRepo.findByName("ROLE_USER").orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        user.setRoles(List.of(roleUser));
        User saved = userRepo.save(user);
        return mapper.map(saved, UserDto.class);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable).map(user -> mapper.map(user, UserDto.class));
    }


    @Override
    public long countUsers() {
        return userRepo.count();
    }


    @Override
    public long countBlockedUsers() {
       return userRepo.countByEnabledFalse();
    }

    
}
