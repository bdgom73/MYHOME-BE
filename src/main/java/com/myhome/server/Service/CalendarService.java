package com.myhome.server.Service;

import com.myhome.server.DTO.CalendarDTO;
import com.myhome.server.Entity.Board.Calendar;
import com.myhome.server.Repository.Board.CalendarRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public void addSchedule(CalendarDTO calendarDTO){
        Calendar calendar = new Calendar();
        calendar.setTitle(calendarDTO.getTitle());
        calendar.setContent(calendarDTO.getContent());
        calendar.setMember(calendarDTO.getMember());

        calendarRepository.save(calendar);
    }

    public void removeSchedule(Long calendar_id) throws SQLDataException {
        Optional<Calendar> findSchedule = calendarRepository.findById(calendar_id);
        if(findSchedule.isEmpty()) {
            throw new SQLDataException("존재하지 않는 일정입니다.");
        }
        calendarRepository.delete(findSchedule.get());
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
}
