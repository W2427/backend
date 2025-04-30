package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;

/**
 * CABLE-PULLING-PACKAGE 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CablePullingPackageEntryInsertDTO extends BaseDTO {

    private static final long serialVersionUID = 4403555166206456501L;
}
