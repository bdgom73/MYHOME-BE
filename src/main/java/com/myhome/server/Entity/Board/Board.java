package com.myhome.server.Entity.Board;

import com.myhome.server.Entity.BaseEntity;
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
    @GeneratedValue @Column(name = "board_id",length = 255)
    private Long id;
    private String title;
    @Column(columnDefinition = "Text",length = 255)
    private String description;
    private int views;

    private Boolean notice = false;

    private LocalDateTime created;
    private LocalDateTime updated;

    private String video_url;
    private String original_url;
    private String video_thumbnail;

    private String keywords ;

    @Enumerated(EnumType.STRING)
    private VideoType videoType = VideoType.NONE;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Recommend> recommendList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardComment> boardCommentList = new ArrayList<>();

    public void addMember(Member member){
        this.member = member;
        member.getBoardList().add(this);
    }
}
