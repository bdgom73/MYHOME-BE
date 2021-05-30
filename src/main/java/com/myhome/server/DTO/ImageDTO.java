package com.myhome.server.DTO;

import lombok.Data;

@Data
public class ImageDTO {

    private Long id;
    private String image_url;
    private String original_url;
    private String filename;
}
