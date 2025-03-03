package com.kin.springbootproject1.board.controller;

import com.kin.springbootproject1.board.dto.commentDto;
import com.kin.springbootproject1.board.repository.commentRepository;
import com.kin.springbootproject1.board.service.commentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class commentController {

    private final commentRepository commentRepository;
    private final commentService commentService;

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity save(@ModelAttribute commentDto commentDto) {
        System.out.println("commentDto = " + commentDto);
        Long saveResult = commentService.save(commentDto);
        if(saveResult != null){
            //작성 성공하면 댓글목록을 가져와서 리턴
            //댓글목록: 해당 게시글의 댓글 전체
            List<commentDto> commentDtoList = commentService.findAll(commentDto.getBoardId());
            //ResponseEntity - body와 header를 함께 다룰수 있는 객체
            return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
        }else{
            //return new ResponseEntity<>(body:"해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
        }

    }
}
