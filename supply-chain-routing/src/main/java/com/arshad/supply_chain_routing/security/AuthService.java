package com.arshad.supply_chain_routing.security;

import com.arshad.supply_chain_routing.dto.LoginRequest;
import com.arshad.supply_chain_routing.dto.LoginResponse;
import com.arshad.supply_chain_routing.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        System.out.println("===== LOGIN START =====");
        System.out.println("USERNAME: " + request.getUsername());

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            System.out.println("AUTHENTICATION SUCCESS");
            System.out.println(
                    "AUTHENTICATED USER: " +
                            authentication.getName()
            );

            System.out.println(
                    "AUTHORITIES: " +
                            authentication.getAuthorities()
            );

            AppUser user = userRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            System.out.println("DATABASE USER FOUND");
            System.out.println("ROLE: " + user.getRole());

            String token = jwtService.generateToken(user);

            System.out.println("JWT GENERATED");
            System.out.println("===== LOGIN SUCCESS =====");

            return new LoginResponse(
                    token,
                    user.getUsername(),
                    user.getRole().name()
            );

        } catch (Exception e) {

            System.out.println("===== LOGIN FAILED =====");
            System.out.println(
                    "EXCEPTION TYPE: " +
                            e.getClass().getName()
            );
            System.out.println(
                    "EXCEPTION MESSAGE: " +
                            e.getMessage()
            );

            e.printStackTrace();

            throw e;
        }
    }
}