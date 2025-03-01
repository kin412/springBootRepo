package com.kin.springbootproject1.board.dto;

import com.kin.springbootproject1.board.entity.boardEntity;
import lombok.Data;

import java.time.LocalDateTime;

//dto(data transfer object), vo, bean, entity
@Data
public class boardDto {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    public boardDto(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public boardDto() {}

    //entity 에 담긴 값을 dto로 옮겨담는 함수
    public static boardDto toDto(boardEntity boardEntity) {
        boardDto dto = new boardDto();
        dto.setId(boardEntity.getId());
        dto.setBoardWriter(boardEntity.getBoardWriter());
        dto.setBoardPass(boardEntity.getBoardPass());
        dto.setBoardTitle(boardEntity.getBoardTitle());
        dto.setBoardContents(boardEntity.getBoardContents());
        dto.setBoardHits(boardEntity.getBoardHits());
        dto.setBoardCreatedTime(boardEntity.getCreatedTime());
        dto.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        return dto;

    }

}
