package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HierarchyNodeWithErrorDTO extends HierarchyNodePutDTO {

    private static final long serialVersionUID = 2486734981135591524L;

    @Schema(description = "错误数")
    private Integer errorCount = null;

    @Schema(description = "错误描述")
    private List<String> errors = null;

    public HierarchyNodeWithErrorDTO() {
        super();
    }

    public HierarchyNodeWithErrorDTO(HierarchyNodePutDTO dto) {
        super();
        BeanUtils.copyProperties(dto, this);
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount() {
        errorCount = (errors == null || errors.size() == 0)
            ? null
            : errors.size();
    }

    @JsonSetter
    public void setErrorCount(Integer errorCount) {

        if (errorCount == null || errorCount == 0) {
            errorCount = null;
        }

        this.errorCount = errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
        setErrorCount();
    }

    public void addError(String error) {

        if (StringUtils.isEmpty(error)) {
            return;
        }

        if (errors == null) {
            errors = new ArrayList<>();
        }

        errors.add(error);
        setErrorCount();

        addErrorMessage(error);
    }

}
