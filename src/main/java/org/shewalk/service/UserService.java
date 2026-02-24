package org.shewalk.service;

import org.shewalk.Dto.UserProfileDto;
import org.shewalk.exception.BadRequestException;
import org.shewalk.model.User;
import org.shewalk.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //adduser
    // Updated addUser in UserService.java
    public User addUser(User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new BadRequestException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // FIX: Only set ROLE_USER if no role was provided in the request
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        return userRepository.save(user);
    }

    //getalluser
    public List<User> findAll(){
        return userRepository.findAll();
    }

    //userbyid
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }


    public User updateUser(Long id, User updatedUser){
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null){
            return null;
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        //encrypt password afterupdation
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setTrustedEmail(updatedUser.getTrustedEmail());

        return userRepository.save(existingUser);
    }


    public boolean deleteUser(Long id){
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

    public User login(String email, String password){
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null){
            return null;
        }

        if (passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }

    public UserProfileDto getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileDto(
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
