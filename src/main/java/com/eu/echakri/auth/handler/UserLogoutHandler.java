package com.eu.echakri.auth.handler;

import com.eu.echakri.auth.entity.Token;
import com.eu.echakri.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class UserLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public UserLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(token).orElse(null);

        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }
}
