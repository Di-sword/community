package com.diswordacg.model;

import lombok.Data;

import java.util.Date;

@Data
public class Post {
    private Integer id;
    private String title;
    private String  content;
    private Integer zone;
    private String block;
    private String img;
    private Integer creator_id;
    private String creator_name;
    private Date release_time;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;

}
