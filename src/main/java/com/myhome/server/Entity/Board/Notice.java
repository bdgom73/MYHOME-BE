package com.myhome.server.Entity.Board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notice {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private Long id;

    private String title;
    @Column(columnDefinition = "Text")
    private String description;
    private int views;
    private LocalDateTime created;
    private LocalDateTime updated;

    private String video_url;
    private String original_url;
    private String video_thumbnail;

    @Enumerated(EnumType.STRING)
    private VideoType videoType = VideoType.NONE;
}
