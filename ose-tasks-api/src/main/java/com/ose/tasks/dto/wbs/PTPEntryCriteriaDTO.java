package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PTPEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = -6776783885036392226L;

    @JsonCreator
    public PTPEntryCriteriaDTO() {
    }

}
