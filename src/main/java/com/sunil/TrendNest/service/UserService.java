package com.sunil.TrendNest.service;

import com.sunil.TrendNest.Constants.UserRolesData;
import com.sunil.TrendNest.model.UserRoles;
import com.sunil.TrendNest.model.Users;
import com.sunil.TrendNest.repo.UserRepo;
import com.sunil.TrendNest.repo.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserRoleRepo roleRepo;

    public ResponseEntity<String> register(Users user){
        Optional<Users> existingUser = Optional.ofNullable(repo.findByUsername(user.getUsername()));

        if (existingUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username '" + user.getUsername() + "' is already taken.");
        }

        UserRoles userRole = roleRepo.findByName(UserRolesData.USER.name());

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("user created");
    }

    public ResponseEntity<String> verify(Users user) {
        try {
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());
                return ResponseEntity.ok(token);
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Wrong password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Authentication failed");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Authentication failed");
    }


}
