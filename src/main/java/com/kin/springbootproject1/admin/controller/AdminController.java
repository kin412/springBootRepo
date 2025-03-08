package com.kin.springbootproject1.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    //builder패턴
    //private boardDto bbb = boardDto.builder().boardTitle("title").build();

    @GetMapping("/main")
    public String main() {
        return "admin";
    }

}
