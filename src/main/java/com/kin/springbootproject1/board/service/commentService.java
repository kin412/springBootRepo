package com.kin.springbootproject1.board.service;

import com.kin.springbootproject1.board.dto.commentDto;
import com.kin.springbootproject1.board.entity.boardEntity;
import com.kin.springbootproject1.board.entity.commentEntity;
import com.kin.springbootproject1.board.repository.boardRepository;
import com.kin.springbootproject1.board.repository.commentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class commentService {
    private final commentRepository commentRepository;
    private final boardRepository boardRepository;

    public Long save(commentDto commentDto) {
        //부모 엔티티 조회
        Optional<boardEntity> optionalBoardEntity = boardRepository.findById(commentDto.getBoardId());
        if(optionalBoardEntity.isPresent()) {
            boardEntity boardEntity = optionalBoardEntity.get();
            commentEntity commentEntity1 = commentEntity.toSaveEntity(commentDto, boardEntity);
            return commentRepository.save(commentEntity1).getId();
        }else{
            return null;
        }
        //builder

    }

    @Transactional
    public List<commentDto> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        boardEntity boardEntity = boardRepository.findById(boardId).get(); // optional을 바로 꺼냄
        List<commentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        /* entityList -> dtoList */
        List<commentDto> commentDtoList = new ArrayList<>();
        for(commentEntity commentEntity : commentEntityList){
            commentDto commentDto1 = commentDto.toCommentDto(commentEntity);
            commentDtoList.add(commentDto1);
        }
        return commentDtoList;
    }
}
