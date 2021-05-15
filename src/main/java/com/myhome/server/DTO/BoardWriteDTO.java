package com.myhome.server.DTO;

import com.myhome.server.Entity.Board.CategoryList;
import com.myhome.server.Entity.Board.VideoType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardWriteDTO {

    private String title;
    private String description;
    private CategoryList categoryList;
    private String video_url;
    private VideoType videoType;

    private List<ImageDTO> imageList = new ArrayList<>();
}
