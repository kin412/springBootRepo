package com.kin.springbootproject1.security.service;

import com.kin.springbootproject1.security.dto.AuthMemberDTO;
import com.kin.springbootproject1.security.entity.MemberEntity;
import com.kin.springbootproject1.security.entity.MemberRole;
import com.kin.springbootproject1.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Auth2UserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("----------loadUser-------------");
        log.info("userRequest : " + userRequest);
        //org.springframework.security.oauth2.client.userinfo.OAuth2UserREquest 객체

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName : " + clientName);
        log.info("userRequest.getAdditionalParameters() : " + userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        oAuth2User.getAttributes().forEach((key, value) -> {
            log.info("key : " + key + " value : " + value);
        });

        String email = null;

        if(clientName.equals("Google")) { // 구글 로그인인 경우
            email = oAuth2User.getAttribute("email");
        }

        log.info("email : " + email);
        MemberEntity memberEntity = saveSocialMember(email);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                memberEntity.getId(),
                memberEntity.getPassword(),
                true, // 어차피 소셜로그인은 true인 사람만 되니까
                memberEntity.getRoles().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        authMemberDTO.setName(memberEntity.getName());

        log.info("----------loadUser End-----------------");

        //return super.loadUser(userRequest);
        //return oAuth2User;
        return authMemberDTO;
    }


    private MemberEntity saveSocialMember(String id) {
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<MemberEntity> result = memberRepository.findByIdAndFromSocial(id, true);

        if(result.isPresent()){
            return result.get();
        }

        MemberEntity memberEntity = MemberEntity.builder().id(id)
                .name(id)
                .password(passwordEncoder.encode("1"))
                .fromSocial(true)
                .build();

        memberEntity.addMemberRole(MemberRole.USER);

        memberRepository.save(memberEntity);

        return memberEntity;

    }

}
