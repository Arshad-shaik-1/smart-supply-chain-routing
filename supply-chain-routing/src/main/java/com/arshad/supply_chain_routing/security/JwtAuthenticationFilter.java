package com.arshad.supply_chain_routing.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("REQUEST: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("AUTH HEADER PRESENT: " + (authHeader != null));

        // No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("NO VALID BEARER TOKEN");

            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT
        String token = authHeader.substring(7);

        String username;

        try {

            username = jwtService.extractUsername(token);

            System.out.println("JWT USERNAME: " + username);

        } catch (Exception e) {

            System.out.println("JWT EXTRACTION FAILED: " + e.getMessage());

            filterChain.doFilter(request, response);
            return;
        }

        // Authenticate user
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails;

            try {

                userDetails =
                        userDetailsService.loadUserByUsername(username);

                System.out.println(
                        "USER AUTHORITIES: " +
                                userDetails.getAuthorities()
                );

            } catch (Exception e) {

                System.out.println(
                        "USER DETAILS FAILED: " +
                                e.getMessage()
                );

                filterChain.doFilter(request, response);
                return;
            }

            // Validate token
            if (jwtService.isTokenValid(token, username)) {

                System.out.println("JWT VALID");

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "AUTHENTICATED USER: " +
                                SecurityContextHolder.getContext()
                                        .getAuthentication()
                );

            } else {

                System.out.println("JWT INVALID OR EXPIRED");
            }
        }

        filterChain.doFilter(request, response);
    }
}