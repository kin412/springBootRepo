package com.kin.springbootproject1.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class homeController {
    @GetMapping("/")
    public String index() {
        System.out.println("homeController.index");
        log.info("homeController.index Log");
        return "index";
    }
}
