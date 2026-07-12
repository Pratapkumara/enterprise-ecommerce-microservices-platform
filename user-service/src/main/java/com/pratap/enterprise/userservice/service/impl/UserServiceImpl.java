package com.pratap.enterprise.userservice.service.impl;

import com.pratap.enterprise.userservice.dto.UserRequest;
import com.pratap.enterprise.userservice.dto.UserResponse;
import com.pratap.enterprise.userservice.entity.User;
import com.pratap.enterprise.userservice.exception.ResourceNotFoundException;
import com.pratap.enterprise.userservice.mapper.UserMapper;
import com.pratap.enterprise.userservice.repository.UserRepository;
import com.pratap.enterprise.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;



    @Override
    public UserResponse createUser(UserRequest request) {


        User user = userMapper.toEntity(request);


        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );


        User savedUser = userRepository.save(user);


        return userMapper.toResponse(savedUser);
    }



    @Override
    public UserResponse getUserById(UUID id) {


        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id));


        return userMapper.toResponse(user);
    }



    @Override
    public List<UserResponse> getAllUsers() {


        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }



    @Override
    public UserResponse updateUser(UUID id, UserRequest request) {


        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id));


        userMapper.updateEntity(request, existingUser);



        if(request.getPassword() != null &&
                !request.getPassword().isBlank()) {


            existingUser.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );

        }


        User updatedUser = userRepository.save(existingUser);


        return userMapper.toResponse(updatedUser);
    }



    @Override
    public void deleteUser(UUID id) {


        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id));


        userRepository.delete(user);
    }

}
