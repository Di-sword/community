package com.diswordacg.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<PostDTO> postDTOList;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalPage, int page) {
        this.totalPage = totalPage;
        this.page = page;
        pages.add(page);
        for (int i = 1;i<=3;i++){
            if (page-i>0){
                pages.add(0,page-i);
            }
            if (page+i <= totalPage){
                pages.add(page+i);
            }
        }

//        是否展示“上一页”
        if (page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
//        是否展示“下一页”
        if (page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }
//        是否展示“第一页”
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
//        是否展示“最后一页”
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }


    }
}
