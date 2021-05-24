package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Entity.Member.MemberRank;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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

}
