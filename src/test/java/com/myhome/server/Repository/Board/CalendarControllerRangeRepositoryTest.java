package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.CalendarRange;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class CalendarControllerRangeRepositoryTest {

    @Autowired CalendarRangeRepository calendarRangeRepository;
    @Autowired
    MemberDetailRepository memberDetailRepository;
    @Test
    public void CalendarRangeTest(){
        MemberDetail member = new MemberDetail();
        member.setEmail("Test Email");
        member.setName("Test");
        MemberDetail member2 = memberDetailRepository.save(member);

        CalendarRange calendarRange = new CalendarRange();
        calendarRange.setContent("Test Content");
        calendarRange.setTitle("Test Title");
        calendarRange.setMember(member2);
        calendarRange.setStart_date(LocalDate.parse("2021-06-05"));
        calendarRange.setEnd_date(LocalDate.parse("2021-06-10"));

        CalendarRange save = calendarRangeRepository.save(calendarRange);

        List<CalendarRange> dateRangeByMember = calendarRangeRepository.findDateRangeByMember(member, LocalDate.parse("2021-04-25"), LocalDate.parse("2021-06-05"));
        System.out.println("dateRangeByMember = " + dateRangeByMember.get(0).getTitle());


    }
}