package com.kin.springbootproject1.security.filter;

import com.kin.springbootproject1.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

/*
    jwt - JSON Web Token
    기존 웹 애플리케이션의 쿠키나 세션을 사용하는 경우 동일한 사이트에서만 동작하기 때문에
    api 서버처럼 외부에서 자유롭게 데이터를 주고 받을 때는 유용하지 못함.
    외부에서 api를 호출 할때 주로 인증정보나 인증키를 같이 전송함.
    이러한 키를 토큰이라는 용어로 사용하기도 하는데, 대표적으로 jwt가 있다.
    이과정에서 필요한 것은 특정한 url 호출 시 전달된 토큰을 검사하는 필터다.
 */
@Slf4j
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUEST URI : " + request.getRequestURI());
        log.info("antPathMatcher.match : " + antPathMatcher.match(pattern, request.getRequestURI()));

        if (antPathMatcher.match(pattern, request.getRequestURI())) {
            log.info("apiCheckFilter.............................");
            log.info("apiCheckFilter.............................");
            log.info("apiCheckFilter.............................");

            boolean checkHeader = checkAuthHeader(request);

            if (checkHeader) {
                filterChain.doFilter(request, response);
                return;
            }else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //json 리턴 및 한글 깨짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FALL CHECK API TOKEN";
                json.put("message", message);
                json.put("code", "403");

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }

        }

        //다음 필터의 단계로 넘어가는 역할을 위해 필요
        filterChain.doFilter(request, response);
    }

    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        //헤더 메시지의 경우 앞에는 인증타입을 사용하는데, 일반적인 경우 Basic, jwt 이용 시 Bearer
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist : " + authHeader);
            /*if(authHeader.equals("1234")){
                checkResult = true;
            }*/
            try {
                String id = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validation result id : " + id);
                checkResult = id.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return checkResult;
    }
}
