package com.kin.springbootproject1.security.service;

import com.kin.springbootproject1.security.dto.AuthMemberDTO;
import com.kin.springbootproject1.security.entity.MemberEntity;
import com.kin.springbootproject1.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberUserDetailsService loadUserByUsername : " + username);

        //Optional<MemberEntity> result = memberRepository.findbyEmail(username, false);
        Optional<MemberEntity> result = memberRepository.findByIdAndFromSocial(username, false);

        //result.isEmpty()
        if(!result.isPresent()){
            throw new UsernameNotFoundException("check id!");
        }

        MemberEntity memberEntity = result.get();

        log.info("-----------------------------------");
        log.info("memberEntity : " + memberEntity);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.isFromSocial(),
                memberEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_"+role.name())).collect(Collectors.toSet())
        );

        authMemberDTO.setName(memberEntity.getName());
        authMemberDTO.setFromSocial(memberEntity.isFromSocial());

        log.info("authMemberDTO : " + authMemberDTO);

        return authMemberDTO;
    }
}
