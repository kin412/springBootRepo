package com.kin.springbootproject1.board.entity;

import com.kin.springbootproject1.board.dto.boardDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//db의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "board_table")
@SequenceGenerator( // 시퀀스 설정
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class boardEntity extends baseEntity {
    @Id // pk 컬럼 지정. 필수
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment - mysql
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BOARD_SEQ_GENERATOR") // oracle
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // default - 크기 255, null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 1 or 0

    //mappedBy = "boardEntity" 의 boardEntity는 boardFileEntity.java 내의 외래키 변수명
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<boardFileEntity> boardFileEnitityList = new ArrayList<boardFileEntity>();


    //dto에 담긴 값들을 entity 객체로 옮겨 닮는 함수
    public static boardEntity toSaveEntity(boardDto boardDto){
        boardEntity boardEntity = new boardEntity();
        boardEntity.setBoardWriter(boardDto.getBoardWriter());
        boardEntity.setBoardPass(boardDto.getBoardPass());
        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContents(boardDto.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); //파일 없음
        return boardEntity;
    }

    public static boardEntity toUpdateEntity(boardDto boardDto) {
        boardEntity boardEntity = new boardEntity();
        boardEntity.setId(boardDto.getId());
        boardEntity.setBoardWriter(boardDto.getBoardWriter());
        boardEntity.setBoardPass(boardDto.getBoardPass());
        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContents(boardDto.getBoardContents());
        boardEntity.setBoardHits(boardDto.getBoardHits());
        return boardEntity;
    }

    public static boardEntity toSaveFileEntity(boardDto boardDto){
        boardEntity boardEntity = new boardEntity();
        boardEntity.setBoardWriter(boardDto.getBoardWriter());
        boardEntity.setBoardPass(boardDto.getBoardPass());
        boardEntity.setBoardTitle(boardDto.getBoardTitle());
        boardEntity.setBoardContents(boardDto.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); //파일 없음
        return boardEntity;
    }

}
