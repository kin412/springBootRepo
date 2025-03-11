package com.kin.springbootproject1.note.controller;

import com.kin.springbootproject1.note.dto.NoteDto;
import com.kin.springbootproject1.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    //post
    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDto noteDto) {
        log.info("----------------register-------------------");
        log.info(noteDto.toString());

        Long num = noteService.register(noteDto);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //get
    @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDto> read(@PathVariable("num") Long num) {
        log.info("-------------read----------------------");
        log.info(num.toString());
        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    //get
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDto>> getList(String id) {
        log.info("-------------getList----------------------");
        log.info(id);
        return new ResponseEntity<>(noteService.getAllWithWriter(id), HttpStatus.OK);
    }

    //delete
    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num) {

        log.info("-------------remove---------------------");
        log.info(num.toString());

        noteService.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    //put
    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDto noteDto) {
        log.info("-------------modify---------------------");
        log.info(noteDto.toString());

        noteService.modify(noteDto);

        return new ResponseEntity<>("modified", HttpStatus.OK);
    }

}
