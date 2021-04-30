package com.myhome.server.Entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private String avatar_url;
    @Column(updatable = false)
    private LocalDateTime created;
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    private MemberRank rank = MemberRank.GUEST;

    @Column(name="join_state")
    private boolean join_state = false;

    @Column(name = "sessionUID")
    private String SESSION_UID;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public void setRank(MemberRank rank) {
        this.rank = rank;
    }

    public void setSESSION_UID(String SESSION_UID) {
        this.SESSION_UID = SESSION_UID;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
