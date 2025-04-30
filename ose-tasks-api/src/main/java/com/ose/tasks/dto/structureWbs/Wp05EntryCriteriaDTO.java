package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * PART 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wp05EntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -2438077660619628637L;

    @JsonCreator
    public Wp05EntryCriteriaDTO() {
    }

}
