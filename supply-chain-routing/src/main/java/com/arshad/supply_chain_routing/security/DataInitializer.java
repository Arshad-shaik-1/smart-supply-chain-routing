package com.arshad.supply_chain_routing.security;

import com.arshad.supply_chain_routing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {

                AppUser user = new AppUser();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole(Role.USER);

                userRepository.save(user);
            }
        };
    }
}