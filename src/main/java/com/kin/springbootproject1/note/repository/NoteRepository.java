package com.kin.springbootproject1.note.repository;

import com.kin.springbootproject1.note.entity.NoteEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    @EntityGraph(attributePaths = "writer", type=EntityGraph.EntityGraphType.LOAD)
    @Query("select n from NoteEntity n where n.num = :num")
    Optional<NoteEntity> getwithwriter(Long num);

    @EntityGraph(attributePaths = {"writer"}, type=EntityGraph.EntityGraphType.LOAD)
    @Query("select n from NoteEntity n where n.writer.id = :id")
    List<NoteEntity> getList(String id);
}
