package com.arshad.supply_chain_routing.security;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name , password;

    @Enumerated(EnumType.STRING)
    private Role role;
    public AppUser() {
    }

}
