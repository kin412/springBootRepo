package com.kin.springbootproject1.security.filter;

import com.kin.springbootproject1.security.dto.AuthMemberDTO;
import com.kin.springbootproject1.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

//폼 방식이 아닌 특정한 url로 외부에서 로그인이 가능하도록 하고,
//로그인 성공 시 클라이언트가 Authorization 헤더의 값으로 이용할 데이터 전송
@Slf4j
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {

        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("-------------------ApiLoginFilter-------------------");
        log.info("attemptAuthentication");

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        log.info("id : " + id);
        log.info("pw : " + pw);

        /*if(id == null) {
            throw new BadCredentialsException("id cannot be null");
        }*/
        //return null;

        //token - id, pw를 파라미터로 받아서 실제 인증 처리
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pw);
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info(" apiLoginFilter - successfulAuthentication");
        log.info("successfulAuthentication authResult : " + authResult);

        log.info("authResult.getPrincipal : " + authResult.getPrincipal());

        //id
        String id = ((AuthMemberDTO)authResult.getPrincipal()).getId();

        String token = null;

        try{
            token = jwtUtil.generateToken(id);
            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info("token : " + token);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
