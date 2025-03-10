package com.kin.springbootproject1.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class SecurityController {

    @GetMapping("/form")
    public String securityLogin() {
        log.info("security login form");
        //log.info("security login : " +request.toString());
        return "security/securityLogin";
    }

}
