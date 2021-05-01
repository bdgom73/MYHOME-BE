package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Member.Member;

import javax.persistence.*;

@Entity
public class Recommend {

    @Id @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private Boolean state;

    private String category;
}
