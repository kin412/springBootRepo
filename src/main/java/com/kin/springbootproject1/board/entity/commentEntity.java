package com.kin.springbootproject1.board.entity;

import com.kin.springbootproject1.board.dto.commentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class commentEntity extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable= false)
    private String commentWriter;

    @Column
    private String commentContents;

    /*board:comment 1:N*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private boardEntity boardEntity;

    public static commentEntity toSaveEntity(commentDto commentDto, boardEntity boardEntity) {
        commentEntity commentEntity = new commentEntity();
        commentEntity.setCommentWriter(commentDto.getCommentWriter());
        commentEntity.setCommentContents(commentDto.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;

    }
}
