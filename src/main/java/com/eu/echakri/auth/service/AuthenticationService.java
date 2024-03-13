package com.eu.echakri.auth.service;

import com.eu.echakri.auth.dto.request.AuthenticationRequest;
import com.eu.echakri.auth.dto.request.RegistrationRequest;
import com.eu.echakri.auth.dto.response.AuthenticationResponse;
import com.eu.echakri.auth.entity.Token;
import com.eu.echakri.auth.entity.User;
import com.eu.echakri.auth.repository.TokenRepository;
import com.eu.echakri.auth.repository.UserRepository;
import com.eu.echakri.auth.dto.response.RegistrationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TokenRepository tokenRepository,
            AuthenticationManager authenticationManager
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public RegistrationResponse register(RegistrationRequest request) {

        if (repository.findByUsername(request.username()).isPresent()) {
            return new RegistrationResponse(403, "User already exist");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        this.repository.save(user);
        return new RegistrationResponse(200, "User registration successful");

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = repository.findByUsername(request.username()).orElseThrow();
        String jwt = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login successful");

    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> t.setLoggedOut(true));

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
