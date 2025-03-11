package com.kin.springbootproject1.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private Long num;

    private String title;

    private String content;

    private String writerId; //연관 관계 없이

    private LocalDateTime regDate, modDate;

}
