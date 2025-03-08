package com.kin.springbootproject1.security;

import com.kin.springbootproject1.security.entity.MemberEntity;
import com.kin.springbootproject1.security.entity.MemberRole;
import com.kin.springbootproject1.security.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        /*
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MemberEntity memberEntity = MemberEntity.builder()
                    .id("user"+i)
                    .password(passwordEncoder.encode("pass"+i))
                    .name("사용자이름"+i)
                    .email("user"+i+"@naver.com")
                    .build();

            //defaultRole
            memberEntity.addMemberRole(MemberRole.USER);

            if(i > 5){
                memberEntity.addMemberRole(MemberRole.ADMIN);
            }

            memberRepository.save(memberEntity);

        });
        */

        MemberEntity memberEntity = MemberEntity.builder()
                .id("2")
                .password(passwordEncoder.encode("2"))
                .name("유저1")
                .email("asd123@naver.com")
                .build();
        memberEntity.addMemberRole(MemberRole.USER);
        //memberEntity.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(memberEntity);


    }

    @Test
    public void testRead(){
        //Optional<MemberEntity> result = memberRepository.findbyEmail("kin412@naver.com", true);
        Optional<MemberEntity> result = memberRepository.findbyId("1");
        MemberEntity memberEntity = result.get();
        System.out.println(memberEntity);
    }

}
