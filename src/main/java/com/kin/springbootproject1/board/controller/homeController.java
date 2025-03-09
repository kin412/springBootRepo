package com.kin.springbootproject1.board.controller;

import com.kin.springbootproject1.security.dto.AuthMemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class homeController {
    @GetMapping("/")
    public String index(@AuthenticationPrincipal AuthMemberDTO member) {
        /*
            컨트롤러에서 로그인된 사용자 정보 확인 방법
            1. SecurityContextHolder 객체 사용
            2. @AuthenticationPrincipal 어노테이션 사용 - permitAll의 경우 위의 매개변수처럼 선언은 해도 상관없지만
            아래 member.toString()와 같이 접근하면 로그인정보가 필요하므로 로그인으로 돌아감 혹은 403
         */
        log.info("/*************************로그인 정보****************************/");
        log.info(member.toString());
        log.info("/**************************************************************/");
        log.info("homeController.index Log");
        return "index";
        //return "index2"; // sec 태그(sec:authentication)를 쓰지않는 index - permitall 정상 작동
    }
}
