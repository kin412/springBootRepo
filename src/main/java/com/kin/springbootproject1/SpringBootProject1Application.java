package com.kin.springbootproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing //jpa @ElementConllection 사용을 위해
@SpringBootApplication
public class SpringBootProject1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject1Application.class, args);
    }

}
