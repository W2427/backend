package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * CableTray 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CableTrayEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = 343606932636113914L;

    @JsonCreator
    public CableTrayEntryCriteriaDTO() {
    }

}
