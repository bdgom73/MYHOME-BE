package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Board;
import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Entity.Board.Image;
import com.myhome.server.Entity.Board.VideoType;
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
    private CategoryList categoryList;
    private String video_url;
    private VideoType videoType;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int views;
    private int recommend;
    private List<ImageDTO> imageList = new ArrayList<>();

    public BoardWriteDTO(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.writer = board.getMember().getName();
        this.writer_id = board.getMember().getId();
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
            return imageDTO;
        }).collect(Collectors.toList());
    }
}
