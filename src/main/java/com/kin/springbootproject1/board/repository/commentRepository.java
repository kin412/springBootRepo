package com.kin.springbootproject1.board.repository;

import com.kin.springbootproject1.board.entity.boardEntity;
import com.kin.springbootproject1.board.entity.commentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface commentRepository extends JpaRepository<commentEntity, Long> {
    // select * from comment_table where board_id=? order by id desc;
    List<commentEntity> findAllByBoardEntityOrderByIdDesc(boardEntity boardEntity);
}
