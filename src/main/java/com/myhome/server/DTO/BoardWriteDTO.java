package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.Category;
import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Entity.Board.Image;
import com.myhome.server.Entity.Board.VideoType;
import com.myhome.server.Entity.Member.Member;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardWriteDTO {

    private String title;
    private String description;
    private String memberUUID;
    private CategoryList categoryList;
    private String video_url;
    private VideoType videoType;

    private List<String> imageList = new ArrayList<>();
}
