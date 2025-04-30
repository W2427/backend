package com.ose.tasks.dto;

import com.ose.dto.PageDTO;


public class SacsUploadHistorySearchDTO extends PageDTO {


    private static final long serialVersionUID = 1126704405013108617L;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
