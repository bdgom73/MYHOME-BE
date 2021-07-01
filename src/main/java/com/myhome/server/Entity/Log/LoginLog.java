package com.myhome.server.Entity.Log;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class LoginLog {

    @Column(name = "login_log_id",length = 255)
    @Id @GeneratedValue
    private Long id;

    private LocalDateTime loginDate;
    private String ip;
    private String countryCode;
    private String ipv;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
}
