package com.kin.springbootproject1.board.repository;

import com.kin.springbootproject1.board.entity.boardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//기본적인 crud에 대한 쿼리는 jpa가 기본으로 생성해주지만 결국 외에건 짜야함.
public interface boardRepository extends JpaRepository<boardEntity, Long> { //entity 클래스만 받아줌
    //조회수 update
    //nativeQuery -> 실제 db의 쿼리 사용 가능
    //@Query(value = "update boardEntity b set b.boardHits=b.boardHits+1 where b.id=:id", nativeQuery = true)

    @Modifying //update나 delete 시 필수로 붙여야함
    //entity기준 쿼리
    @Query(value = "update boardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
