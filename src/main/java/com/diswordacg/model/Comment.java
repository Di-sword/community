package com.diswordacg.model;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Long id;
    private Long parent_id;
    private Integer type;
    private Integer commentator;
    private Date gmt_create;
    private String content;
}
