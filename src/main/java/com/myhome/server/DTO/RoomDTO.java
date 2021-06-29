package com.myhome.server.DTO;

import com.myhome.server.Entity.Chat.Room;
import com.myhome.server.Entity.Chat.RoomType;
import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class RoomDTO {

    private Long id;
    private String title;
    private LocalDateTime created;
    private String password;
    private Long member_id;
    private String nickname;
    private RoomType type;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.title = room.getTitle();
        this.created = room.getCreated();
        this.password = room.getPassword();
        this.member_id = room.getLeader().getId();
        this.nickname = room.getLeader().getNickname();
        this.type = room.getType();
    }
}
