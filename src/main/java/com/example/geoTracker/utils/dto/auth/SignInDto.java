package com.example.geoTracker.utils.dto.auth;

import com.example.geoTracker.src.models.Role;
import com.example.geoTracker.src.models.User;
import de.mkammerer.argon2.Argon2;

public class SignInDto {
    private String email;
    private String password;
    private Role role;

    public User toUser(Argon2 argon2){
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(hashPassword(argon2, password));
        user.setRole(role);
        return user;
    }

    private String hashPassword(Argon2 argon2, String rawPassword) {
        return argon2.hash(2, 65536, 1, rawPassword.toCharArray());
    }
}
