package com.myhome.server.DTO;

import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.*;

@Data
public class CalendarDTO {

    private String title;
    private String content;
    private Member member;

}
