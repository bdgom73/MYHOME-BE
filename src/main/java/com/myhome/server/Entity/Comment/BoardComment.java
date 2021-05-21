package com.myhome.server.Entity.Comment;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class BoardComment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "Text")
    private String description;

    private LocalDateTime created;
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void addComment(Board board){
        this.board = board;
        board.getBoardCommentList().add(this);
    }
}
