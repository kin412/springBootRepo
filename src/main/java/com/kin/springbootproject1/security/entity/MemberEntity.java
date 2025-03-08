package com.kin.springbootproject1.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MemberEntity extends BaseEntity {

    @Id
    private String id;

    private String password;

    private String name;

    private String email;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    //private Set<MemberRole> roles = Set.of(MemberRole.USER, MemberRole.ADMIN);
    private Set<MemberRole> roles = new HashSet<>();

    public void addMemberRole(MemberRole memberRole) {
        roles.add(memberRole);
    }

}
