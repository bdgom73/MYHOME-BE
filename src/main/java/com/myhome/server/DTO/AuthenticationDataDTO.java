package com.myhome.server.DTO;

import com.myhome.server.Entity.Member.MemberRank;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class AuthenticationDataDTO {

    private String name;
    private String email;
    private MemberRank rank;
    private String avatar_url;

}
