package com.myhome.server.Entity.Board;

import javax.persistence.*;

@Entity
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryList name;
}
