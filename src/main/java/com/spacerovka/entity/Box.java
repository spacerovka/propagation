package com.spacerovka.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Svetotulichka on 14.12.2017.
 */

@Entity
@Data
public class Box {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String content;

    public Box() {
    }

    public Box(String name, String content) {
        this.name = name;
        this.content = content;
    }


}
