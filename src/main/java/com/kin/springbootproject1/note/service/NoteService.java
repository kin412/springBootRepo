package com.kin.springbootproject1.note.service;

import com.kin.springbootproject1.note.dto.NoteDto;
import com.kin.springbootproject1.note.entity.NoteEntity;
import com.kin.springbootproject1.security.entity.MemberEntity;

import java.util.List;

public interface NoteService {

    Long register(NoteDto noteDto);

    NoteDto get(long num);

    void modify(NoteDto noteDto);

    void remove(long num);

    List<NoteDto> getAllWithWriter(String writerId);

    default NoteEntity dtoToEntity(NoteDto noteDto) {
        NoteEntity noteEntity = NoteEntity.builder()
                .num(noteDto.getNum())
                .title(noteDto.getTitle())
                .content(noteDto.getContent())
                .writer(
                        MemberEntity.builder()
                                .id(noteDto.getWriterId())
                                .build()
                )
                .build();

        return noteEntity;
    }

    default NoteDto entityToToDto(NoteEntity noteEntity) {
        NoteDto noteDto = NoteDto.builder()
                .num(noteEntity.getNum())
                .title(noteEntity.getTitle())
                .writerId(noteEntity.getWriter().getId())
                .regDate(noteEntity.getRegDate())
                .modDate(noteEntity.getModDate())
                .build();
        return noteDto;
    }

}
