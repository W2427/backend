package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import java.util.List;

public class WBSEntriesDTO extends BaseDTO {

    private static final long serialVersionUID = 3066764683354596152L;

    private List<Long> wbsEntryIds;

    public List<Long> getWbsEntryIds() {
        return wbsEntryIds;
    }

    public void setWbsEntryIds(List<Long> wbsEntryIds) {
        this.wbsEntryIds = wbsEntryIds;
    }
}
