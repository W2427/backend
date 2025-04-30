package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * Cable 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CableEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -7355411470608059420L;

    @JsonCreator
    public CableEntryCriteriaDTO() {
    }

}
