package com.diswordacg.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    private List<PostDTO> postDTOS;
    private boolean hasPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages;

}
