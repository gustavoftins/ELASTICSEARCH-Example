package com.hb.elastic.controllers;

import com.hb.elastic.models.User;
import com.hb.elastic.services.SocketServer;
import com.hb.elastic.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final SocketServer socketServer;

    public UserController(UserService userService, SocketServer socketServer) {
        this.userService = userService;
        this.socketServer = socketServer;
    }

    @PostMapping
    public void create(@RequestBody User user) throws IOException {

        this.userService.save(user);
    }

    @GetMapping("/{id}")
    public User find(@PathVariable("id") String id) throws IOException {
        return this.userService.findById(id);
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") String id, @RequestBody User user) throws IOException {
        return this.userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id) throws IOException {
        return this.userService.deleteById(id);
    }

    @GetMapping
    public List<User> findAll() throws IOException {
       return this.userService.findAll();
    }

    @GetMapping("/teste/{string}")
    public void teste(@PathVariable("string") String teste) throws IOException {
        this.socketServer.streamFile(teste);
    }
}
