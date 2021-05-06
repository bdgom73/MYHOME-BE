package com.myhome.server.DTO;

import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarRangeDTO {

    private String title;
    private String content;
    private Member member;
    private LocalDate start_date;
    private LocalDate end_date;

}
