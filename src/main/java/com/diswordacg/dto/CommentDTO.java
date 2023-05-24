package com.diswordacg.dto;

import com.diswordacg.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Long id;
    private Long parent_id;
    private Integer type;
    private Integer commentator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmt_create;
    private String content;
    private User user;
}
