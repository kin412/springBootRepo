package com.kin.springbootproject1.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        String password = "1";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("enPw : " + encodedPassword);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        System.out.println("matches : " + matches);

    }
}
