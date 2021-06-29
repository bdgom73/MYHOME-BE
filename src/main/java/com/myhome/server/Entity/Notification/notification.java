package com.myhome.server.Entity.Notification;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Member.Member;
import com.sun.nio.sctp.Notification;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class notification {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private NotificationType noti_type;
    private String message;
    private String url;
    private LocalDateTime created;
    private LocalDateTime read_date;

    @OneToOne(fetch = FetchType.LAZY)
    private Member recipient;

    @OneToOne(fetch = FetchType.LAZY)
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    private Board board;
}
