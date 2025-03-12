package com.kin.springbootproject1.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    하나의 프로젝트라도 프론트 단과 백단이 다른 프로세스에서 실행되는 경우 (웹서버와 was가 나뉘는 경우등)
    에는 cors filter를 추가해야만 서로 통신이 가능
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) //corsFilter가 모든 필터중 가장먼저 동작하도록 설정
public class CORSFilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            filterChain.doFilter(request, response);
        }

        /*
            외부에서 ajax로 /notes/1 을 이용할 경우 프론트 코드
            $(.btn).click(function(){
                $.ajax({
                    beforeSend: function(request){
                        request.setRequestHeader("Authorization",  'Bearer '+ jwtValue); //jwtValue는 jwt값
                    },
                    dataType: "json",
                    url: 'http://localhost:8080/notes/1',
                    data: {id:'kin412@naver.com'},
                    success: function(arr){
                        console.log(arr);
                    }
                });
            })
         */

    }
}
