package com.myhome.server.Controller.Board;

import com.myhome.server.DTO.CalendarDTO;
import com.myhome.server.DTO.CalendarRangeDTO;
import com.myhome.server.Entity.Board.Calendar;
import com.myhome.server.Entity.Board.CalendarRange;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Board.CalendarRangeRepository;
import com.myhome.server.Repository.Board.CalendarRepository;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.sql.SQLDataException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final MemberDetailRepository memberDetailRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarRangeRepository calendarRangeRepository;
    private final CalendarService calendarService;
    public CalendarController(MemberDetailRepository memberDetailRepository, CalendarRepository calendarRepository, CalendarRangeRepository calendarRangeRepository, CalendarService calendarService) {
        this.memberDetailRepository = memberDetailRepository;
        this.calendarRepository = calendarRepository;
        this.calendarRangeRepository = calendarRangeRepository;
        this.calendarService = calendarService;
    }

    @GetMapping("/schedule")
    public HashMap<String, Object> sendScheduleData(
            @RequestHeader("Authorization") String UID,
            @PathParam("start_date") String start_date,
            @PathParam("end_date") String end_date,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        System.out.println(start_date);
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(UID);
        if(findMember.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"존재하지 않는 회원입니다.");
        }
        MemberDetail member = findMember.get();

        List<CalendarRange> findCalendarRange = calendarRangeRepository.findDateRangeByMember(member, LocalDate.parse(start_date) , LocalDate.parse(end_date));
        List<Calendar> findCalendar = calendarRepository.findDateRangeByMember(member, LocalDate.parse(start_date) , LocalDate.parse(end_date));
        ArrayList<CalendarRangeDTO> cr = new ArrayList<>();
        for (CalendarRange calendarRange : findCalendarRange) {
            CalendarRangeDTO crd = new CalendarRangeDTO();
            crd.setId(calendarRange.getId());
            crd.setContent(calendarRange.getContent());
            crd.setEnd_date(calendarRange.getEnd_date());
            crd.setStart_date(calendarRange.getStart_date());
            crd.setTitle(calendarRange.getTitle());
            cr.add(crd);
        }
        ArrayList<CalendarDTO> c = new ArrayList<>();
        for (Calendar calendar : findCalendar) {
            CalendarDTO cd = new CalendarDTO();
            cd.setId(calendar.getId());
            cd.setContent(calendar.getContent());
            cd.setDate(calendar.getDate());
            cd.setTitle(calendar.getTitle());
            c.add(cd);
        }
        HashMap<String, Object> value = new HashMap<>();
        value.put("range", cr);
        value.put("single", c);
        return value;

    }

    @PostMapping("/add")
    public void addSchedule(
            @RequestHeader("Authorization") String UID,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "content") String content,
            @RequestParam(name = "date", required = false ) LocalDate date,
            @RequestParam(name = "start_date", required = false ) LocalDate start_date,
            @RequestParam(name = "end_date",required = false ) LocalDate end_date,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(UID);
        if(findMember.isEmpty()){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"존재하지 않는 회원입니다.");
        }
        Member member = findMember.get();

        if(date.toString().isEmpty()){
            CalendarRange calendar = new CalendarRange();
            calendar.setTitle(title);
            calendar.setContent(content);
            calendar.setStart_date(start_date);
            calendar.setEnd_date(end_date);
            calendar.setMember(member);
            calendarRangeRepository.save(calendar);
        }else{
            Calendar calendar = new Calendar();
            calendar.setTitle(title);
            calendar.setContent(content);
            calendar.setDate(date);
            calendar.setMember(member);
            calendarRepository.save(calendar);
        }
    }

    @DeleteMapping("/delete")
    public void deleteSchedule(
            @RequestHeader("Authorization") String UID,
            @PathParam("type") String type,
            @PathParam("cid") Long cid,
            HttpServletResponse httpServletResponse
    ) throws IOException, SQLDataException {
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(UID);
        if(findMember.isEmpty()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "존재하지 않는 회원입니다.");
        }
        if(type.equals("range")) calendarService.removeRangeSchedule(cid);
        else{
            calendarService.removeSchedule(cid);
        }

    }

    @PutMapping("/update")
    public void updateSchedule(
            @RequestHeader("Authorization") String UID,
            @PathParam("type") String type,
            @RequestParam("cid") Long cid,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "content") String content,
            @RequestParam(name = "date", required = false ) LocalDate date,
            @RequestParam(name = "start_date", required = false ) LocalDate start_date,
            @RequestParam(name = "end_date",required = false ) LocalDate end_date,
            HttpServletResponse httpServletResponse
    ) throws IOException, SQLDataException {
        Optional<MemberDetail> findMember = memberDetailRepository.findBySessionUID(UID);
        if(findMember.isEmpty()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "존재하지 않는 회원입니다.");
        }
        if(type.equals("range")){
            Optional<CalendarRange> findCalendar = calendarRangeRepository.findById(cid);
            if(findCalendar.isEmpty()) throw new SQLDataException("일정이 존재하지 않습니다");
            CalendarRange calendarRange = findCalendar.get();
            calendarRange.setContent(content);
            calendarRange.setTitle(title);
            if(!start_date.toString().isEmpty()) calendarRange.setStart_date(start_date);
            if(!end_date.toString().isEmpty()) calendarRange.setEnd_date(end_date);
        }else{
            Optional<Calendar> findCalendar = calendarRepository.findById(cid);
            if(findCalendar.isEmpty()) throw new SQLDataException("일정이 존재하지 않습니다");
            Calendar calendar = findCalendar.get();
            calendar.setContent(content);
            calendar.setTitle(title);
            if(!date.toString().isEmpty()) calendar.setDate(start_date);
        }
    }
}

