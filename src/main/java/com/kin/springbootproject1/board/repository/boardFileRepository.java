package com.kin.springbootproject1.board.repository;

import com.kin.springbootproject1.board.entity.boardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface boardFileRepository extends JpaRepository<boardFileEntity, Long> {
}
