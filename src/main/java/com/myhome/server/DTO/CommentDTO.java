package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Entity.Member.MemberRank;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    private String description;

    private LocalDateTime created;
    private LocalDateTime updated;

    private Long member_id;
    private String name;
    private String avatar_url;
    private Long board_id;
    private MemberRank rank;
    private String nickname;
    private CategoryList categoryList;
    public CommentDTO(BoardComment comment) {
        this.id = comment.getId();
        this.description = comment.getDescription();
        this.created = comment.getCreated();
        this.updated = comment.getUpdated();
        this.member_id = comment.getMember().getId();
        this.name = comment.getMember().getName();
        this.avatar_url = comment.getMember().getAvatar_url();
        this.board_id = comment.getBoard().getId();
        this.rank = comment.getMember().getRank();
        this.nickname = comment.getMember().getNickname();
        this.categoryList = comment.getBoard().getCategory().getName();
    }
}
