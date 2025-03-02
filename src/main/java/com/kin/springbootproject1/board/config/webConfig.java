package com.kin.springbootproject1.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //설정 클래스 지정 어노테이션
public class webConfig implements WebMvcConfigurer {
    // savePath를 resourcePath로 접근하겠다.
    private String resourcePath = "/upload/**"; //view에서 접근할 경로
    private String savePath = "file:///C:/intellij_workspace/springBootProject1/images/"; //실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
    }
}
