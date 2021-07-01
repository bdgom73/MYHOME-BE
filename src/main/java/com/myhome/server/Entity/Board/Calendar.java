package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.BaseEntity;
import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class Calendar extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="calendar_id")
    private Long id;

    private LocalDate date;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="member_id")
    private Member member;
}
