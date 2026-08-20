package com.foodapp.controller;

import com.foodapp.model.*;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.JwtService;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, BCryptPasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest r) {
        if (users.findByEmail(r.email()).isPresent())
            return java.util.Map.of("error", "Email already exists");
        User u = new User();
        u.setEmail(r.email());
        u.setName(r.name());
        u.setPhone(r.phone());
        u.setPassword(encoder.encode(r.password()));
        u.setRole(Role.CUSTOMER);
        users.save(u);
        return java.util.Map.of("message", "Registered successfully");
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest r) {
        var u = users.findByEmail(r.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!encoder.matches(r.password(), u.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        return java.util.Map.of("token", jwt.generate(u.getEmail(), u.getRole().name()),
                "name", u.getName(), "role", u.getRole().name());
    }

    public record RegisterRequest(String name, String email, String password, String phone) {
    }

    public record LoginRequest(String email, String password) {
    }
}
