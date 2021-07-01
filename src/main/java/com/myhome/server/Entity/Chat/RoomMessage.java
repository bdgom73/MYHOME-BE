package com.myhome.server.Entity.Chat;

import com.myhome.server.Entity.Member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RoomMessage {

    @Id @GeneratedValue
    @Column(name = "room_message_id",length = 255)
    private Long id;

    private String content;

    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Member member;

}
