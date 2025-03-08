package com.kin.springbootproject1.security.repository;

import com.kin.springbootproject1.security.entity.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    //@EntityGraph 로 MemberRole 엔티티 left outer join 처리
    /*
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from MemberEntity m where m.fromSocial = :social and " +
            "m.email =:email")
    Optional<MemberEntity> findbyEmail(String email, boolean social);
    */

    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from MemberEntity m where m.id =:id")
    Optional<MemberEntity> findbyId(String id);
}
