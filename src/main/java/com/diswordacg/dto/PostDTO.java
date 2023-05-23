package com.diswordacg.dto;

import com.diswordacg.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {
    private Integer id;
    private String title;
    private String  content;
    private Integer zone;
    private String block;
    private String img;
    private Integer creator_id;
    private String creator_name;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private Date release_time;
    private User user;
}
