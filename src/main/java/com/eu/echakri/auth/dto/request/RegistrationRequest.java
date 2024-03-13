package com.eu.echakri.auth.dto.request;


import com.eu.echakri.auth.entity.enums.Role;

public record RegistrationRequest(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String username,
        String password,
        Role role
) {
}
