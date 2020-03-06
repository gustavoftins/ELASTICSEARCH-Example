package com.hb.elastic.controllers;

import com.hb.elastic.models.User;
import com.hb.elastic.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void create(@RequestBody User user) throws IOException {

        this.userService.save(user);
    }

    @GetMapping("/{id}")
    public List<Map<String, Object>> find(@PathVariable("id") String id) throws IOException {
        return this.userService.findById(id);
    }

}
