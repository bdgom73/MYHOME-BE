package com.myhome.server.Entity.Chat;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Room {

    @Id @GeneratedValue
    @Column(name = "room_id",length = 255)
    private Long id;
    private String title;
    private LocalDateTime created;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Member leader;
    private RoomType type;

}
