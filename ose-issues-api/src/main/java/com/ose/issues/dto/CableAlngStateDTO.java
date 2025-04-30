package com.ose.issues.dto;

import com.ose.dto.PageDTO;

public class CableAlngStateDTO extends PageDTO {


    private static final long serialVersionUID = 6017575263550510106L;
    private Long id;

    private String statusName;

    private String statusState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusState() {
        return statusState;
    }

    public void setStatusState(String statusState) {
        this.statusState = statusState;
    }
}
