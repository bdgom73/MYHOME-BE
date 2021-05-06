package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class CalendarRange {

    @Id @GeneratedValue
    @Column(name = "calendar_range_id")
    private Long id;

    @Column(name = "start_date")
    private LocalDate start_date;
    private LocalDate end_date;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
}
