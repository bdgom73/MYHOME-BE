package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.Comment.BoardComment;
import com.myhome.server.Entity.Member.Member;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board {

    @Id
    @GeneratedValue @Column(name = "board_id")
    private Long id;
    private String title;
    @Column(columnDefinition = "Text")
    private String description;
    private int views;
    private LocalDateTime created;
    private LocalDateTime updated;

    private String video_url;
    private String original_url;

    @Enumerated(EnumType.STRING)
    private VideoType videoType = VideoType.NONE;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board")
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Recommend> recommendList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardComment> boardCommentList = new ArrayList<>();
}
