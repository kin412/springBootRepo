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
            2. @AuthenticationPrincipal 어노테이션 사용
         */
        log.info("/*************************로그인 정보****************************/");
        log.info(member.toString());
        log.info("/**************************************************************/");
        log.info("homeController.index Log");
        return "index";
    }
}
