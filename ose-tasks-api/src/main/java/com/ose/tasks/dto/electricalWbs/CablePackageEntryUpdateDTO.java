package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;

/**
 * ose-PULLING-PACKAGE 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CablePackageEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 8253189436071380926L;
}
