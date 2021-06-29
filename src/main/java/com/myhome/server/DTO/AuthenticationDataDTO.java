package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Entity.Member.MemberRank;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AuthenticationDataDTO {

    private Long id;
    private String name;
    private String email;
    private MemberRank rank;
    private String avatar_url;
    private String nickname;
    private String zipcode;
    private String address;
    private String detail_address;
    private String self_introduction;

    public AuthenticationDataDTO(MemberDetail memberDetail) {
        this.id = memberDetail.getId();
        this.name = memberDetail.getName();
        this.email = memberDetail.getEmail();
        this.rank = memberDetail.getRank();
        this.avatar_url = memberDetail.getAvatar_url();
        this.nickname = memberDetail.getNickname();
        this.zipcode = memberDetail.getZipcode();
        this.address = memberDetail.getAddress();
        this.detail_address = memberDetail.getDetail_address();
        this.self_introduction = memberDetail.getSelf_introduction();

    }


}
