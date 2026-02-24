package org.shewalk.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Shewalk is running";
    }

    @GetMapping("/home")
    @CrossOrigin(origins = "*") // Allows your JS to read this data
    public String getHomeData() {
        return "Welcome to SheWalk! You have successfully logged in.";
    }
}
