package com.kin.springbootproject1.board.controller;

import com.kin.springbootproject1.board.dto.boardDto;
import com.kin.springbootproject1.board.service.boardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class boardController {

    private final boardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    //@RequestParam("boardWriter") String boardWriter
    public String save(@ModelAttribute boardDto boardDto) {
        System.out.println("boardDto = " + boardDto);
        boardService.save(boardDto);

        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        // db에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
       List<boardDto> boardDtoList = boardService.findAll();
       model.addAttribute("boardDtoList", boardDtoList);
       return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
         */

        boardService.updateHits(id);
        boardDto boardDto = boardService.findById(id);
        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        boardDto boardDto = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDto);
        return "update";
    }

    @PostMapping("update")
    public String update(@ModelAttribute boardDto boardDto, Model model) {
        boardDto board = boardService.update(boardDto);
        model.addAttribute("board", board);
        return "detail";
        //return "redirect:/board/"+boardDto.getId();
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model) {
        //pageable.getPageNumber();
        Page<boardDto> boardList = boardService.paging(pageable);

        // page 갯수 20개
        //보여지는 페이지 갯수가 3개면
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }

}
