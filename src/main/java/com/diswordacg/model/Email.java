package com.diswordacg.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {
    private int id;
    private String email;
    private String code;
    private int type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;


}
