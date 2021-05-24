package com.myhome.server.Entity.Word;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FilterText {

    @Id @GeneratedValue
    @Column(name = "filter_id")
    private Long id;
    private String text;
    private String kinds;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        kinds = kinds;
    }
}
