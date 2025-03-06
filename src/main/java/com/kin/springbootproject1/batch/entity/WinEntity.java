package com.kin.springbootproject1.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Long win;
    private Boolean reward; //oracle은 boolean형이 제공되지 않고 1,0으로 표시함. 최근 oracle23엔 boolean형이 탑재되었다고 함.
}
