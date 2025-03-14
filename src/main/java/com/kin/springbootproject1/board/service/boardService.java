package com.kin.springbootproject1.board.service;

import com.kin.springbootproject1.board.dto.boardDto;
import com.kin.springbootproject1.board.entity.boardEntity;
import com.kin.springbootproject1.board.entity.boardFileEntity;
import com.kin.springbootproject1.board.repository.boardFileRepository;
import com.kin.springbootproject1.board.repository.boardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    dto -> entity (entity class)
    entity -> dto (dto class)
    변환작업을 service에서
    legacy로 치면 mapper 사용하는 것. 달라질건 없음.
 */

@Service
@RequiredArgsConstructor
public class boardService {

    private final boardRepository boardRepository;
    private final boardFileRepository boardFileRepository;

    // dto -> entity
    public void save(boardDto boardDto) throws IOException {
        //파일 첨부 여부에 따라 로직 분리
        if(boardDto.getBoardFile().isEmpty()) {
            // 첨부 파일 없음
            boardEntity saveBoardEntity = boardEntity.toSaveEntity(boardDto);
            boardRepository.save(saveBoardEntity); //entity를 받고 entity를 리턴
        }else{
            //첨부 파일 있음
            /*
              1. dto에 담긴 파일을 꺼냄
              2. 파일의 이름 가져옴
              3. 서버 저장용 이름 추가 => 내사진.jpg = 13758934759_내사진.jpg - uuid, currentTimeMillis
              4. 저장 경로 설정
              5. 해당 경로에 파일 저장
              6. board_table에 해당 데이터 save 처리
              7. board_file_table에 해당 데이터 save 처리
             */
            boardEntity saveBoardEntity = boardEntity.toSaveFileEntity(boardDto);
            Long saveId = boardRepository.save(saveBoardEntity).getId();
            boardEntity board = boardRepository.findById(saveId).get();

            for(MultipartFile boardFile : boardDto.getBoardFile()) {
                //MultipartFile boardFile = boardDto.getBoardFile(); // 1
                String originalFilename = boardFile.getOriginalFilename(); // 2
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3
                String savePath = "C:/intellij_workspace/springBootProject1/images/" + storedFileName; // 4
                boardFile.transferTo(new File(savePath)); // 5. savePath로 파일을 넘긴다. - 파일저장

                /*
                boardEntity saveBoardEntity = boardEntity.toSaveFileEntity(boardDto);
                Long saveId = boardRepository.save(saveBoardEntity).getId();
                boardEntity board = boardRepository.findById(saveId).get();
                 */
                boardFileEntity boardFileEntity1 = boardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity1);
            }

        }


    }

    // entity -> dto
    @Transactional //85라인 boardDto.toDto(); 에서 부모엔티티가 자식엔티티에 접근하기 때문에 붙여줘야함
    public List<boardDto> findAll() {
        List<boardEntity> boardEntityList = boardRepository.findAll();
        List<boardDto> boardDtoList = new ArrayList<>();
        for (boardEntity boardEntity : boardEntityList) {
            boardDtoList.add(boardDto.toDto(boardEntity));
        }
        return boardDtoList;

    }

    //기본적인 crud에 대한 쿼리는 jpa가 기본으로 생성해주지만 결국 외에건 짜야함.
    @Transactional // 기본적으로 jpa가 제공하는 crud외에 쿼리를 짤경우는 필수로 붙여줘야함. 안그럼 에러
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional //102라인 boardDto.toDto(); 에서 부모엔티티가 자식엔티티에 접근하기 때문에 붙여줘야함
    public boardDto findById(Long id) {
        Optional<boardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            return boardDto.toDto(optionalBoardEntity.get());
        }else{
            return null;
        }
    }

    public boardDto update(boardDto boardDto) {
        /*jpa에는 update를 따로 해주는 메서드는 없음
          save() 메서드를 가지고 insert, update 둘다 가능.
          구분하는 기준은 id값이 있는지(update) 없는지(insert).
         */
        boardEntity saveBoardEntity = boardEntity.toUpdateEntity(boardDto);
        boardRepository.save(saveBoardEntity);
        return findById(boardDto.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<boardDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        int pageLimit = 3;
        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작 -> 사용자 입장에서 1페이지가 얘한테는 0이라는거 -> 이게 getNumber()
        Page<boardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록 : id, writer, title, hits, createdTime
        Page<boardDto> boardDtos = boardEntities.map(board ->
                new boardDto(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

        return boardDtos;
    }
}
