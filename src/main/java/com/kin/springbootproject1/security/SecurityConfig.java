package com.kin.springbootproject1.security;

import com.kin.springbootproject1.security.customHandler.CustomAccessDeniedHandler;
import com.kin.springbootproject1.security.customHandler.CustomAuthFailureHandler;
import com.kin.springbootproject1.security.customHandler.CustomSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//prePostEnabled - @PreAuthorize 사용가능 여부      securedEnabled - 예전버전의 @Secure어노테이션 사용가능 여부
//이 어노테이션을 쓰면 filterChain의 http.authorizeHttpRequests 이부분 제거하고 컨트롤러에 @PreAuthorize("hasRole('ADMIN')") 식으로 적용
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    /*
        스프링 부트 2.5전 시큐리티 관련 설정은 WebSecurityConfiguraterAdapter 라는 클래스를 상속하고
        필요한 설정은 override를 해서 작성했음.
        하지만 2.6부터는 deprecated되어서 주의해야함.
        스프링 부트 2.7.0이전 까지는 WebSecurityConfigurerAdapter 라는 추상 클래스를 상속하여 처리했지만,
        2.7.0 버전 부터는 deprecated되었으므로 주의해야함
     */
    /*
        SecurityConfig - 모든 시큐리티 관련 설정이 추가되는 부분
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 설정 방식 - InMemoryUserDetailsManager
    //간단한 테스트에 좋음
    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1111"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
     */

    //SecurityConfig - 인가(Authorization) 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> {
            auth.requestMatchers("/").hasRole("USER")
            //auth.requestMatchers("/").permitAll() //백단과 프론트에서 시큐리티 관련 객체에 접근하는 코드 사용하면 권한이 없어서 403
            //auth.requestMatchers("/", "/oauth2/**").permitAll()
                    //.requestMatchers("/board/*").permitAll() // /board/ 하위 경로에 대해 모든 권한이 접근가능. 로그인하지않은 익명의 사용자도
                    //.requestMatchers("/board/*").hasRole("ADMIN")
                    //anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                    .requestMatchers("/board/*"
                            , "/restIndex/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers("/admin/*").hasRole("ADMIN")
                    .requestMatchers("/upload/**").permitAll() // 이미지 요청 허용
                    .requestMatchers("/rest/**").permitAll() //rest api 허용
                    .requestMatchers("/notes/**").permitAll(); //rest api 허용


        });

        /*
        인가/인증 시 로그인 화면
        'formLogin()'은(는) 버전 6.1 이상에서 지원 중단되며 제거될 예정입니다
        'csrf()'은(는) 버전 6.1 이상에서 지원 중단되며 제거될 예정입니다
        'logout()'은(는) 버전 6.1 이상에서 지원 중단되며 제거될 예정입니다
        form 에서는 csrf토큰이 보안상 권장 되지만 rest방식등에서는 매번 토큰값을 알아야내야하는 불편함이 있어 경우에 따라 disable() 함.
        logout() 처리시 csrf 토큰을 사용한다면 post방식으로만 처리함. csrf토큰을 비활성화 했다면 get으로도 가능
        */

        //http.formLogin();
        http.formLogin(login -> login // 일반 로그인 (DB 기반)
                //.loginPage("/login/form") // 로그인 페이지 지정
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 페이지
                .failureHandler(new CustomAuthFailureHandler()) // 로그인 실패 핸들러
                .permitAll()
        ).exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()); // 권한 denied 핸들러

        http.csrf().disable();
        http.logout();

        //구글 oauth 로그인
        //'oauth2Login()'은(는) 버전 6.1 이상에서 지원 중단되며 제거될 예정입니다
        http.oauth2Login().successHandler(CustomSuccessHandler());

        /*
        http.oauth2Login(oauth2 -> oauth2 // OAuth 로그인 (구글, 카카오 등)
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 페이지
        );
         */

        //remember me - 소셜로그인은 적용안됨
        http.rememberMe().tokenValiditySeconds(60*60*24*7); //쿠키를 7일간 유지

        /* 이런식으로 chaining해서 사용 가능
        http.authorizeHttpRequests((auth) -> {
            auth.requestMatchers("/board/*").permitAll() // /board/ 하위 경로에 대해 모든 권한이 접근가능
            .requestMatchers("/board/member").hasRole("USER")
        }).formLogin((formLogin) ->
                formLogin
                        .loginPage("/login/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login/login-proc")
                        .defaultSuccessUrl("/", true));
        */

        return http.build();
    }

    //구글 로그인 시에 넣어준 핸들러
    @Bean
    public CustomSuccessHandler CustomSuccessHandler() {
        return new CustomSuccessHandler(passwordEncoder());
    }

}
