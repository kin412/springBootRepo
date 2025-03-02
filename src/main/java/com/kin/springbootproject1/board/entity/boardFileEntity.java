package com.kin.springbootproject1.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class boardFileEntity extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFilename;

    @Column
    private String storedFileName;

    //fk 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private boardEntity boardEntity; // 반드시 부모 엔티티 타입으로

    public static boardFileEntity toBoardFileEntity(boardEntity boardEntity, String originalFilename, String storedFileName) {
        boardFileEntity boardFileEntity = new boardFileEntity();
        boardFileEntity.setOriginalFilename(originalFilename);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }

}
