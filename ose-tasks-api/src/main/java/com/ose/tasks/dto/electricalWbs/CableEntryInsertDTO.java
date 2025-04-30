package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;

/**
 * CABLE 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CableEntryInsertDTO extends BaseDTO {

    private static final long serialVersionUID = 3352790381911582948L;
}
