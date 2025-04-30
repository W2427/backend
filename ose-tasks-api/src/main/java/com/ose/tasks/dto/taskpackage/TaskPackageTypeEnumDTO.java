package com.ose.tasks.dto.taskpackage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.setting.Discipline;
import com.ose.tasks.entity.setting.FuncPart;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务包 新建时候 需要的 实体类型 和 功能分区清单 数据传输对象。
 */
public class TaskPackageTypeEnumDTO extends BaseDTO {

    private static final long serialVersionUID = 730334367016261113L;

    @Schema(description = "专业类型")
    private String disciplines;

    @Schema(description = "功能分区清单")
    private String funcParts;

    @JsonCreator
    public TaskPackageTypeEnumDTO(@JsonProperty("disciplines") List<Discipline> disciplines,
                                  @JsonProperty("funcParts") List<FuncPart> funcParts) {
        this.disciplines = StringUtils.toJSON(disciplines);
        this.funcParts = StringUtils.toJSON(funcParts);
    }


    @JsonIgnore
    public void setJsonDisciplines(List<Discipline> disciplines) {
        if (disciplines != null) {
            this.disciplines = StringUtils.toJSON(disciplines);
        }
    }

    @JsonProperty(value = "disciplines", access = JsonProperty.Access.READ_ONLY)
    public List<Discipline> getJsonDisciplines() {
        if (disciplines != null && !"".equals(disciplines)) {
            return StringUtils.decode(disciplines, new TypeReference<List<Discipline>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonFuncParts(List<FuncPart> funcParts) {
        if (funcParts != null) {
            this.funcParts = StringUtils.toJSON(funcParts);
        }
    }

    @JsonProperty(value = "funcParts", access = JsonProperty.Access.READ_ONLY)
    public List<FuncPart> getJsonFuncParts() {
        if (funcParts != null && !"".equals(funcParts)) {
            return StringUtils.decode(funcParts, new TypeReference<List<FuncPart>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public String getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(String disciplines) {
        this.disciplines = disciplines;
    }

    public String getFuncParts() {
        return funcParts;
    }

    public void setFuncParts(String funcParts) {
        this.funcParts = funcParts;
    }
}
