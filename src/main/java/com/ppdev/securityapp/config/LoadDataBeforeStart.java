package com.ppdev.securityapp.config;

import com.ppdev.securityapp.entity.Role;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoadDataBeforeStart {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        if (userRepository.getByEmail("some@mail.ru") == null) {
            User user = new User("Petr",
                    "Paramonov",
                    "some@mail.ru",
                    passwordEncoder.encode("petekrut92"),
                    Role.ADMIN);
            user.setEnabled(true);

            userRepository.save(user);
        }
    }
}
