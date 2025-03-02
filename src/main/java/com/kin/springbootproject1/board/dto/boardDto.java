package com.kin.springbootproject1.board.dto;

import com.kin.springbootproject1.board.entity.boardEntity;
import com.kin.springbootproject1.board.entity.boardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//dto(data transfer object), vo, bean, entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class boardDto {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // save.html -> controller 시 파일을 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    //boolean 타입으로 해도 되는데, entity 조작 시 손이 많이감. 일단은 쉽게 int 타입으로
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    public boardDto(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    //public boardDto() {}

    //entity 에 담긴 값을 dto로 옮겨담는 함수
    public static boardDto toDto(boardEntity boardEntity) {
        boardDto dto = new boardDto();
        dto.setId(boardEntity.getId());
        dto.setBoardWriter(boardEntity.getBoardWriter());
        dto.setBoardPass(boardEntity.getBoardPass());
        dto.setBoardTitle(boardEntity.getBoardTitle());
        dto.setBoardContents(boardEntity.getBoardContents());
        dto.setBoardHits(boardEntity.getBoardHits());
        dto.setBoardCreatedTime(boardEntity.getCreatedTime());
        dto.setBoardUpdatedTime(boardEntity.getUpdatedTime());

        dto.setFileAttached(boardEntity.getFileAttached());

        if(boardEntity.getFileAttached() != 0){
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            // 파일 이름을 가져가야함
            // originalFileName, storedFileName은  board_file_talbe(boardFileEntity) 에 담겨있음. 하지만 이함수는 boardEntity만을 받는다.
            // 하지만 boardEntity와 boardFileEntity는 @OneToMany @ManyToOne 로 관계를 지어놨기 때문에 아래와 같이 사용이 가능하다.
            // 요때 lazy로딩이 되는듯
            for(boardFileEntity boardFileEntity : boardEntity.getBoardFileEnitityList()) {
                originalFileNameList.add(boardFileEntity.getOriginalFilename());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            dto.setOriginalFileName(originalFileNameList);
            dto.setStoredFileName(storedFileNameList);
        }

        return dto;

    }

}
