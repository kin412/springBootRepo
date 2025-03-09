package com.kin.springbootproject1.security.customHandler;

import com.kin.springbootproject1.security.dto.AuthMemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public CustomSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("------------onAuthenticationSuccess--------------");

        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();

        log.info("authMemberDTO: " + authMemberDTO);

        boolean fromSocial = authMemberDTO.isFromSocial();

        log.info("need modify member? " + fromSocial);

        //연동 로그인하면 password가 없다. 근데 db엔 넣어준거 되있긴함. 나중에 찾아보기
        //boolean passwordResult = passwordEncoder.matches("1", authMemberDTO.getPassword());

        log.info("authMemberDTO.getPassword(): " + authMemberDTO.getPassword());
        //log.info("passwordResult: " + passwordResult);

        /*if(fromSocial && passwordResult) { //구글 로그인 시 비밀번호 변경으로 갈때 설정
            redirectStrategy.sendRedirect(request, response, "/");
        }*/

        redirectStrategy.sendRedirect(request, response, "/");

    }
}
