package com.myhome.server.Repository.Chat;

import com.myhome.server.Entity.Chat.JoinRoom;
import com.myhome.server.Entity.Chat.Room;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JoinRoomRepository extends JpaRepository<JoinRoom, Long> {

    List<JoinRoom> findByMember(Member member);

    List<JoinRoom> findByRoom(Room room);

    List<JoinRoom> findByRoomAndJoinState(Room room, Boolean joinState);

    Optional<JoinRoom> findByRoomAndJoinStateAndMember(Room room, Boolean joinState, Member member);

    @Query("SELECT r FROM JoinRoom r WHERE r.room = :room And r.joinState = :joinState And r.connectionStatus = :connectionStatus")
    List<JoinRoom> findRoomCurrentMember(Room room, Boolean joinState, Boolean connectionStatus);
}
