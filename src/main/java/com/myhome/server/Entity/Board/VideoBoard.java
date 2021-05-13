package com.myhome.server.Entity.Board;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;

@Entity
public class VideoBoard extends Board {

    private String video_url;

    @Enumerated(EnumType.STRING)
    private VideoType videoType;
}
