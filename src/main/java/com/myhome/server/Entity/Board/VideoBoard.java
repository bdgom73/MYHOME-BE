package com.myhome.server.Entity.Board;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.*;

@Entity
@Getter @Setter
public class VideoBoard extends Board {

    private String video_url;
    private String original_url;

    @Enumerated(EnumType.STRING)
    private VideoType videoType;
}
