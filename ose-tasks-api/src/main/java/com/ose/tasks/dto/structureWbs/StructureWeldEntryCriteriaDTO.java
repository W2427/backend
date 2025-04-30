package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * 结构焊口 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureWeldEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -5120402178696028805L;

    @JsonCreator
    public StructureWeldEntryCriteriaDTO() {
    }

}
