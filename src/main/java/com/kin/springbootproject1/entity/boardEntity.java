package com.kin.springbootproject1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BOARD")
public class boardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    //이게 jpa내에 컬럼을 만드는듯. 그렇기 때문에 실제 테이블의 컬렴형과 크기를 맞춰야함.
    @Column(length = 30, nullable = false)
    private String writer;

    @Column
    private String title;

}
