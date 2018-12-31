package org.flowant.users;

import org.flowant.users.data.LoggingEventListener;
import org.flowant.users.data.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;




@SpringBootApplication
//@EntityScan(basePackageClasses = User.class)
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}

	@Bean
    LoggingEventListener listener() {
        return new LoggingEventListener();
    }
}

