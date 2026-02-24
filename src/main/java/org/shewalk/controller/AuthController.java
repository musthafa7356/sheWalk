package org.shewalk.controller;


import jakarta.validation.Valid;
import org.shewalk.Dto.JwtResponseDto;
import org.shewalk.Dto.LoginRequestDto;
import org.shewalk.Util.JwtUtil;
import org.shewalk.model.User;
import org.shewalk.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,  JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        User user = userService.login(request.getEmail(), request.getPassword());

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());


        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {

        User savesUser = userService.addUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

}
