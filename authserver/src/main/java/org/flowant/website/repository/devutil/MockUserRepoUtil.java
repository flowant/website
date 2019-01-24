package org.flowant.website.repository.devutil;

import org.flowant.website.event.MockDataGenerateEvent;
import org.flowant.website.model.User;
import org.flowant.website.repository.AuthserverUserRepository;
import org.flowant.website.util.test.UserMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class MockUserRepoUtil {

    @Autowired
    AuthserverUserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User saveUserWithEncodedPassword(User user) {
        String orgPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(orgPassword));
        userRepository.save(user).block();
        user.setPassword(orgPassword);
        return user;
    }

    public void deleteUser(User user) {
        userRepository.delete(user).block();
    }

    public void saveMockUsers(int cntUser) {
        int count = userRepository.count().block().intValue();
        for (int i = count; i < cntUser; i++) {
            User user = saveUserWithEncodedPassword(UserMaker.large(i));
            log.debug("saved mock user:{}", user);
        }
    }

    @EventListener
    public void onApplicationEvent(MockDataGenerateEvent event) {
        log.debug(event::toString);
        saveMockUsers(5);
    }

}
