package com.kin.springbootproject1.board.dto;

import com.kin.springbootproject1.board.entity.commentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class commentDto {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static commentDto toCommentDto(commentEntity commentEntity) {
        commentDto dto = new commentDto();
        dto.setId(commentEntity.getId());
        dto.setCommentWriter(commentEntity.getCommentWriter());
        dto.setCommentContents(commentEntity.getCommentContents());
        dto.setBoardId(commentEntity.getBoardEntity().getId()); // service메서드에 @transactional 필요
        dto.setCommentCreatedTime(commentEntity.getCreatedTime());
        return dto;
    }
}
