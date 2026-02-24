package org.shewalk.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import org.shewalk.Dto.UserProfileDto;
import org.shewalk.model.User;
import org.shewalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    //constructor injection
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //creating user
    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        User savedUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //list all users
    @GetMapping
    public List<User> getUser(){
        return userService.findAll();
    }

    //list userbyid
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.findById(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    //updating
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @Valid @RequestBody User user){
        User updated = userService.updateUser(id, user);

        if (updated == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    //deleting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        Boolean deleted = userService.deleteUser(id);

        if (!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {

        String email = authentication.getName();

        UserProfileDto profile = userService.getProfile(email);
        return ResponseEntity.ok(profile);
    }

}
