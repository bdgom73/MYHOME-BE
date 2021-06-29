package com.myhome.server.Repository.Chat;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Chat.Room;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(nativeQuery=true, value="SELECT * FROM Room ORDER BY random() LIMIT 20")
    List<Room> findByRandomData();

    Slice<Room> findByTitleContaining(String title);

    Slice<Room> findByLeader(Member member);


}
