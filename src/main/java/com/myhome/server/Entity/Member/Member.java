package com.myhome.server.Entity.Member;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Chat.JoinRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private String avatar_url;
    private LocalDateTime created;
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @Column(name="member_rank")
    private MemberRank rank ;

    private boolean join_state;

    @Column(name = "sessionUID")
    private String SESSION_UID;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Board> BoardList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<JoinRoom> JoinRoomList = new ArrayList<>();

}
