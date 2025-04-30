package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ExperienceCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 2835876689498481709L;

//    @Schema(description = "编号")
//    private String no;

    @Schema(description = "项目名")
    private String projectName;

    @Schema(description = "经验教训标题")
    private String title;

    @Schema(description = "经验教训描述")
    private String description;

    @Schema(description = "附件")
    private String attachment;

    @Schema(description = "自定义属性列表")
    private List<PropertyDTO> properties;

    @Schema(description = "经验教训母Id")
    private Long parentId;

//    public String getNo() {
//        return no;
//    }
//
//    public void setNo(String no) {
//        this.no = no;
//    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
