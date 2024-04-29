package com.enpm.rasa.controller;


import com.enpm.rasa.dto.AuthenticationRequest;
import com.enpm.rasa.dto.RegisterRequest;
import com.enpm.rasa.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ){
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody AuthenticationRequest request
    ){
        return authenticationService.login(request);
    }
}
