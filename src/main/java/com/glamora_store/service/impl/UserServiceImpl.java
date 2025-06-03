package com.glamora_store.service.impl;

import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }
}