package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 实体类型 数据传输对象
 */
public class ExInspTaskEmailDTO extends PageDTO {

    private static final long serialVersionUID = -5818402595331484731L;

    @Schema(description = "操作人Id")
    private Long operator;

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    @JsonProperty(value = "operator", access = READ_ONLY)
    public ReferenceData getOperatorRef() {
        return this.operator == null
            ? null
            : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (this.getOperator() != null && this.getOperator() != 0L) {
            userIDs.add(this.getOperator());
        }

        return userIDs;
    }

}
