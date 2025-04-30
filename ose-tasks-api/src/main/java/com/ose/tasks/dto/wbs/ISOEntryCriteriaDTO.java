package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

import java.util.List;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ISOEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    private List<Long> EntityIds;

    private String userCode;

    @JsonCreator
    public ISOEntryCriteriaDTO() {
    }

    public List<Long> getEntityIds() {
        return EntityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        EntityIds = entityIds;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
