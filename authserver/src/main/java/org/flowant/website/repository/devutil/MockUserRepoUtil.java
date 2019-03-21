package org.flowant.website.repository.devutil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.flowant.website.event.MockDataGenerateEvent;
import org.flowant.website.model.User;
import org.flowant.website.repository.UserRepository;
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
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    List<User> mocks = new ArrayList<>();

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

    protected void saveMockUsers(int cntUser) {
        for (int i = 0; i < cntUser; i++) {
            User user = UserMaker.largeRandom();
            user.setUsername("authuser" + i);
            user.setPassword("authpass" + i);
            if (0 == userRepository.findByUsername(user.getUsername()).count().block()) {
                user = saveUserWithEncodedPassword(user);
                mocks.add(user);
                log.debug("saved mock user:{}", user);
            }
        }
    }

    @EventListener
    public void onApplicationEvent(MockDataGenerateEvent event) {
        log.debug(event::toString);
        saveMockUsers(3);
    }

    @PreDestroy
    public void onPreDestroy() throws Exception {
        if (mocks.size() > 0) {
            userRepository.deleteAll(mocks).block();
            log.debug("Mock data are deleted before shutting down.");
        }
    }
}
