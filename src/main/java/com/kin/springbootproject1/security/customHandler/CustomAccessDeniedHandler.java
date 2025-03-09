package com.kin.springbootproject1.security.customHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 로그 출력
        System.out.println("디나이 핸들러: " + accessDeniedException.getMessage());

        // 2. 예외 발생의 원인도 확인 가능 (AccessDeniedException은 Throwable을 확장)
        Throwable cause = accessDeniedException.getCause();
        if (cause != null) {
            System.out.println("발생원인: " + cause.getMessage());
        }

        // 3. 사용자에게 보다 구체적인 에러 메시지 전달
        //response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 상태 코드
        //response.getWriter().write("Access Denied! You do not have permission to access this resource.");

        // 리디렉션 또는 커스텀 응답
        //response.sendRedirect("/access-denied"); // 권한 부족 시 /access-denied 페이지로 리디렉션
    }
}
