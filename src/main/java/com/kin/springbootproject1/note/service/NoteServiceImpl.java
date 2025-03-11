package com.kin.springbootproject1.note.service;

import com.kin.springbootproject1.note.dto.NoteDto;
import com.kin.springbootproject1.note.entity.NoteEntity;
import com.kin.springbootproject1.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteDto noteDto) {

        NoteEntity noteEntity = dtoToEntity(noteDto);

        log.info("---------------------------------");
        log.info(noteEntity.toString());

        noteRepository.save(noteEntity);

        return noteEntity.getNum();
    }

    @Override
    public NoteDto get(long num) {

        Optional<NoteEntity> result = noteRepository.getwithwriter(num);

        if(result.isPresent()) {
            return entityToToDto(result.get());
        }

        return null;
    }

    @Override
    public void modify(NoteDto noteDto) {

        Long num = noteDto.getNum();
        Optional<NoteEntity> result = noteRepository.findById(num); // findById entity에서 id로 지정한 id를 말하는듯

        if(result.isPresent()) {
            NoteEntity noteEntity = result.get();
            noteEntity.changeTitle(noteDto.getTitle());
            noteEntity.changeContent(noteDto.getContent());
            noteRepository.save(noteEntity);
        }

    }

    @Override
    public void remove(long num) {
        noteRepository.deleteById(num);
    }

    @Override
    public List<NoteDto> getAllWithWriter(String writerId) {
        List<NoteEntity> noteList = noteRepository.getList(writerId);
        return noteList.stream().map(noteEntity -> entityToToDto(noteEntity)).collect(Collectors.toList());
    }
}
