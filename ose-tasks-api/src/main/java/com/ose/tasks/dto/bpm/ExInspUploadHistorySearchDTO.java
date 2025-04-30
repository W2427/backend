package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;


public class ExInspUploadHistorySearchDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 3799468155069076241L;

    private String keyword;

    private Boolean confirmed;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
