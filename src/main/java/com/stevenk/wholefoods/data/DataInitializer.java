package com.stevenk.wholefoods.data;

import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.model.Role;
import com.stevenk.wholefoods.repository.RoleRepository;
import com.stevenk.wholefoods.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER");
         createDefaultUsersIfDontExist();
         createDefaultRolesIfDontExist(roles);
         createDefaultAdmin();
    }

    private void createDefaultUsersIfDontExist() {
        Role userRole = roleRepo.findByName("ROLE_CUSTOMER").get();
        for(int i = 0; i < 5; i++){
            String email = "user" + i + "@gmail.com";
            if(userRepo.existsByEmail(email)){
                continue;
            }
            User user = new User();
            user.setFirstname("User");
            user.setLastname("#" + i);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("PASSWORD"));
            user.setRoles(Set.of(userRole));
            userRepo.save(user);
            System.out.println("Created user: " + user.getEmail());
        }
    }

    private void createDefaultAdmin(){
        Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
        for(int i = 1; i <= 2; i++){
            String email = "admin" + i + "@gmail.com";
            if(userRepo.existsByEmail(email)){
                continue;
            }
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("#" + i);
            admin.setEmail(email);
            admin.setRoles(Set.of(adminRole));
            admin.setPassword(passwordEncoder.encode("PASSWORD"));
            userRepo.save(admin);
            System.out.println("Created admin: " + admin.getEmail());
        }
    }


    private void createDefaultRolesIfDontExist(Set<String> roles) {
        roles.stream().filter(role -> roleRepo.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }
}
