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
    private String video_thumbnail;

    @Enumerated(EnumType.STRING)
    private VideoType videoType = VideoType.NONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Recommend> recommendList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardComment> boardCommentList = new ArrayList<>();
}
