package com.kin.springbootproject1.security;

import com.kin.springbootproject1.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore.......");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String id="kin412@naver.com";
        String str = jwtUtil.generateToken(id);

        System.out.println("str : " + str);
    }

    @Test
    public void testValidate() throws Exception {
        String id="kin412@naver.com";
        String str = jwtUtil.generateToken(id);

        Thread.sleep(5000); // 토큰 유효시간보다 길어지게 되면 아래에서 에러 발생함을 확인하기위한 코드 ex)토큰유지시간 1초면 5초후 실행시 에러

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println("resultEmail : " + resultEmail);
    }

}
