package com.ecommerce.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.model.Role;
import com.ecommerce.model.User;
import com.ecommerce.repository.RoleRepository;
import com.ecommerce.repository.UserRepository;

@Component
public class AdminUserCreator implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create ADMIN role if it doesn't exist
        Role adminRole;
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole = roleRepository.save(adminRole);
        } else {
            adminRole = roleRepository.findByName("ADMIN").get();
        }
        
        // Create admin user if it doesn't exist
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@gmail.com");
            // Password is 'admin'
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setAddress("Admin Office");
            adminUser.setPhoneNumber("123-456-7890");
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);
            
            userRepository.save(adminUser);
            
            System.out.println("Admin user created successfully: admin@gmail.com / admin");
        } else {
            System.out.println("Admin user already exists. Ensuring it has ADMIN role...");
            User adminUser = userRepository.findByEmail("admin@gmail.com").get();
            
            if (!adminUser.getRoles().contains(adminRole)) {
                adminUser.getRoles().add(adminRole);
                userRepository.save(adminUser);
                System.out.println("ADMIN role added to admin user.");
            }
        }
    }
} 