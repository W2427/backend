package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExperienceCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -2406955560729500391L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "专业")
    private List<String> disciplines;

    @Schema(description = "创建者 ID")
    private Long creatorId;

    @Schema(description = "创建开始日期")
    private Date createdAtStartTime;

    @Schema(description = "创建结束日期")
    private Date createdAtEndTime;

    @Schema(description = "自定义字段查询条件（属性 ID:值）")
    private String props = null;


    @Schema(name = "自定义字段查询条件", hidden = true)
    private List<PropertyDTO> properties = null;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedAtStartTime() {
        return createdAtStartTime;
    }

    public void setCreatedAtStartTime(Date createdAtStartTime) {
        this.createdAtStartTime = createdAtStartTime;
    }

    public Date getCreatedAtEndTime() {
        return createdAtEndTime;
    }

    public void setCreatedAtEndTime(Date createdAtEndTime) {
        this.createdAtEndTime = createdAtEndTime;
    }


    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public List<PropertyDTO> getProperties() {

        if (properties == null && !StringUtils.isEmpty(props)) {

            properties = new ArrayList<>();

            for (String kvp : props.split(",")) {
                String[] kv = kvp.split(":");
                PropertyDTO dto = new PropertyDTO();
                dto.setPropertyDefinitionId(LongUtils.parseLong(kv[0]));
                dto.setValues(Collections.singletonList(kv.length == 1 ? "" : kv[1]));
                properties.add(dto);
            }

        }

        return properties;
    }

    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    public List<String> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<String> disciplines) {
        this.disciplines = disciplines;
    }
}

