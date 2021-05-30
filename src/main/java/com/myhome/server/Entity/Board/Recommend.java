package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Recommend {

    @Id @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private Boolean state;

    private String category;

    public void addBoard(Board board){
        this.board = board;
        board.getRecommendList().add(this);
    }
}
