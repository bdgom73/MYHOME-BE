package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Entity.Board.Image;
import com.myhome.server.Entity.Board.VideoType;
import com.myhome.server.Entity.Member.MemberRank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoardWriteDTO {

    private Long id;
    private String title;
    private String description;
    private String writer;
    private Long writer_id;
    private String nickname;
    private MemberRank rank;
    private String writer_avatar_url;
    private CategoryList categoryList;
    private String video_url;
    private VideoType videoType;
    private String video_thumbnail;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int views;
    private int recommend;
    private List<ImageDTO> imageList = new ArrayList<>();
    private List<CommentDTO> commentDTOList = new ArrayList<>();
    public BoardWriteDTO(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.writer = board.getMember().getName();
        this.writer_id = board.getMember().getId();
        this.nickname = board.getMember().getNickname();
        this.rank = board.getMember().getRank();
        this.writer_avatar_url = board.getMember().getAvatar_url();
        this.categoryList = board.getCategory().getName();
        this.video_url = board.getVideo_url();
        this.videoType = board.getVideoType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();
        this.views = board.getViews();
        this.recommend = board.getRecommendList().size();
        this.imageList = board.getImageList().stream().map(m->{
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setId(m.getId());
            imageDTO.setImage_url(m.getImage_url());
            imageDTO.setOriginal_url(m.getOriginal_url());
            imageDTO.setFilename(m.getFilename());
            return imageDTO;
        }).collect(Collectors.toList());
        this.commentDTOList = board.getBoardCommentList().stream().map(m->{
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setBoard_id(m.getBoard().getId());
            commentDTO.setId(m.getId());
            commentDTO.setCreated(m.getCreated());
            commentDTO.setDescription(m.getDescription());
            commentDTO.setUpdated(m.getUpdated());
            commentDTO.setMember_id(m.getMember().getId());
            commentDTO.setName(m.getMember().getName());
            commentDTO.setAvatar_url(m.getMember().getAvatar_url());
            commentDTO.setRank(m.getMember().getRank());
            commentDTO.setNickname(m.getMember().getNickname());
            return commentDTO;
        }).collect(Collectors.toList());

    }
}
