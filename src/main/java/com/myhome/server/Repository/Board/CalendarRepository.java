package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Calendar;
import com.myhome.server.Entity.Board.CalendarRange;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {

    List<Calendar> findByMember(Member member);

    @Query("select c from Calendar c where c.member = :member and c.date between :startDate and :endDate")
    List<Calendar> findDateRangeByMember(
            @Param("member") Member member , @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
