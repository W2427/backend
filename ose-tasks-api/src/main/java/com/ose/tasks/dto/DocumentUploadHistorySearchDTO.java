package com.ose.tasks.dto;

import com.ose.dto.PageDTO;


public class DocumentUploadHistorySearchDTO extends PageDTO {
    private static final long serialVersionUID = 4614794060925349091L;
    private String keyword;

    private String label;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
