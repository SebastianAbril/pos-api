package com.sebastianabril.pos.api.controller;

import com.sebastianabril.pos.api.controller.dto.UserDTO;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/user")
    public ResponseEntity<User> save(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.save(
                userDTO.getName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRoleId()
            );
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/user")
    public List<User> findAll() {
        return userService.findAll();
    }
}
