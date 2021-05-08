package com.myhome.server.DTO;

import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
public class CalendarDTO {

    private Long id;
    private String title;
    private String content;
    private Member member;
    private LocalDate date;

}
