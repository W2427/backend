package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 子系统实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubSystemEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = -6776783885036392226L;

    @JsonCreator
    public SubSystemEntryCriteriaDTO() {
    }

}
