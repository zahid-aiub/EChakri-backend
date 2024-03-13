package com.eu.echakri.auth.handler;

import com.eu.echakri.auth.dto.request.AuthenticationRequest;
import com.eu.echakri.auth.dto.request.RegistrationRequest;
import com.eu.echakri.auth.dto.response.AuthenticationResponse;
import com.eu.echakri.auth.entity.User;
import com.eu.echakri.auth.dto.response.RegistrationResponse;
import com.eu.echakri.auth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
