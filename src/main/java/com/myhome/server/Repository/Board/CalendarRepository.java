package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Calendar;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {

    List<Calendar> findByMember(Member member);
}
