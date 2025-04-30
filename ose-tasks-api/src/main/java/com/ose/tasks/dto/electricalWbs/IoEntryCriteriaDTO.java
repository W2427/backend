package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * Io 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IoEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -6215397895980219800L;

    @JsonCreator
    public IoEntryCriteriaDTO() {
    }

}
