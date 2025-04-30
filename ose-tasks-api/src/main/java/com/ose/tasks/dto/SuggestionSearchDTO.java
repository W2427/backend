package com.ose.tasks.dto;

import com.ose.dto.PageDTO;


public class SuggestionSearchDTO extends PageDTO {

    private static final long serialVersionUID = 8809892885765193794L;
    private String summary;

    private String proposer;

    private String status;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
