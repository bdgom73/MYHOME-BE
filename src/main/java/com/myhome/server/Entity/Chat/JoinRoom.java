package com.myhome.server.Entity.Chat;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class JoinRoom {

    @Id
    @GeneratedValue
    @Column(name = "join_room_id",length = 255)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    // 가입상태
    private Boolean joinState ;

    // 가입일
    private LocalDateTime joinDate;

    // 마지막 접속일
    private LocalDateTime lastConnectionDate;

    // 연결 상태
    private Boolean connectionStatus;

    public void addMember(Member member){
        this.member = member;
        member.getJoinRoomList().add(this);
    }
}
