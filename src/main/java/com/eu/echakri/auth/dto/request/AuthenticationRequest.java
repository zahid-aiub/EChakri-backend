package com.eu.echakri.auth.dto.request;


public record AuthenticationRequest(
        String username,
        String password
) {
}
