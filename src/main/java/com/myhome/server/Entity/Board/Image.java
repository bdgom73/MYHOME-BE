package com.myhome.server.Entity.Board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String image_url;
    private String original_url;
    private Boolean state = true;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public void addImageToBoard(Board board){
        this.board = board;
        board.getImageList().add(this);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
