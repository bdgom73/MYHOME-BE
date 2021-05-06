package com.myhome.server.Service;

import com.myhome.server.DTO.CalendarDTO;
import com.myhome.server.DTO.CalendarRangeDTO;
import com.myhome.server.Entity.Board.Calendar;
import com.myhome.server.Entity.Board.CalendarRange;
import com.myhome.server.Entity.Member.Member;
import com.myhome.server.Repository.Board.CalendarRangeRepository;
import com.myhome.server.Repository.Board.CalendarRepository;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.sql.SQLDataException;
import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarRangeRepository calendarRangeRepository;

    public CalendarService(CalendarRepository calendarRepository, CalendarRangeRepository calendarRangeRepository) {
        this.calendarRepository = calendarRepository;
        this.calendarRangeRepository = calendarRangeRepository;
    }

    public void addSchedule(CalendarDTO calendarDTO){
        Calendar calendar = new Calendar();
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setContent(calendarDTO.getContent());
        calendar.setMember(calendarDTO.getMember());
        calendar.setDate(calendarDTO.getDate());
        calendarRepository.save(calendar);
    }
    public void addSchedule(CalendarRangeDTO calendarRangeDTO){
        CalendarRange calendarRange = new CalendarRange();
        calendarRange.setTitle(calendarRangeDTO.getTitle());
        calendarRange.setContent(calendarRangeDTO.getContent());
        calendarRange.setMember(calendarRangeDTO.getMember());
        calendarRange.setStart_date(calendarRangeDTO.getStart_date());
        calendarRange.setEnd_date(calendarRangeDTO.getEnd_date());
        calendarRangeRepository.save(calendarRange);
    }

    public void removeSchedule(Long calendar_id) throws SQLDataException {
        Optional<Calendar> findSchedule = calendarRepository.findById(calendar_id);
        if(findSchedule.isEmpty()) {
            throw new SQLDataException("존재하지 않는 일정입니다.");
        }

        calendarRepository.delete(findSchedule.get());
    }
    public void removeRangeSchedule(Long calendar_id) throws SQLDataException {
        Optional<CalendarRange> findSchedule = calendarRangeRepository.findById(calendar_id);
        if(findSchedule.isEmpty()) {
            throw new SQLDataException("존재하지 않는 일정입니다.");
        }
        calendarRangeRepository.delete(findSchedule.get());
    }

    public void updateSchedule(Long calendar_id, CalendarDTO calendarDTO) throws SQLDataException {
        Optional<Calendar> findSchedule = calendarRepository.findById(calendar_id);
        if(findSchedule.isEmpty()) {
            throw new SQLDataException("존재하지 않는 일정입니다.");
        }
        Calendar calendar = findSchedule.get();
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setContent(calendarDTO.getContent());
    }
    public void updateSchedule(Long calendar_id, CalendarRangeDTO calendarRangeDTO) throws SQLDataException {
        Optional<CalendarRange> findSchedule = calendarRangeRepository.findById(calendar_id);
        if(findSchedule.isEmpty()) {
            throw new SQLDataException("존재하지 않는 일정입니다.");
        }
        CalendarRange calendar = findSchedule.get();
        calendar.setTitle(calendarRangeDTO.getTitle());
        calendar.setContent(calendarRangeDTO.getContent());
        calendar.setStart_date(calendarRangeDTO.getStart_date());
        calendar.setEnd_date(calendarRangeDTO.getEnd_date());
    }

}
