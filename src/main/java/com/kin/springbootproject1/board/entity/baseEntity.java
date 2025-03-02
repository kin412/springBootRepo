package com.kin.springbootproject1.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class baseEntity { // 시간
    @CreationTimestamp //생성될때 시간정보
    @Column(updatable = false) //수정 시에는 이컬럼은 관여안하게
    private LocalDateTime createdTime;

    @UpdateTimestamp //업데이트시 시간정보
    @Column(insertable = false) //생성 시에는 이 컬럼은 관여안하게
    private LocalDateTime updatedTime;

}
