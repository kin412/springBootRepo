package com.kin.springbootproject1.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Getter
@Setter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    private String id;

    private String email;

    private String name;

    private boolean fromSocial;

    private Map<String, Object> attr;

    //기존회원
    //부모 클래스인 User클래스의 생성자 호출
    public AuthMemberDTO(String username, String email, String password, boolean fromSocial,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = username;
        this.email = email;
        this.fromSocial = fromSocial;
    }

    //OAuth2 구글 연동 회원
    public AuthMemberDTO(String username, String password, boolean fromSocial,
                         Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username, null, password, fromSocial, authorities);
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
