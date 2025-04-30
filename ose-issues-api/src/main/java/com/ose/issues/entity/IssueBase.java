package com.ose.issues.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.issues.vo.IssueType;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;

@MappedSuperclass
public abstract class IssueBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6875076709044454940L;

    @Schema(description = "组织 ID")
    @Column(length = 20)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(length = 20)
    private Long projectId;

    @Schema(description = "问题类型")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private IssueType type;

    @Schema(description = "遗留问题编号")
    @Column
    private String no;

    @Schema(description = "意见编号")
    @Column
    private String opinionNo;

    @Schema(description = "流水号")
    @Column
    private Integer seriesNo;

    @Schema(description = "标题")
    @Column
    private String title;

    @Schema(description = "描述")
    @Column(length = 4096)
    private String description;

    @Schema(description = "中文描述")
    @Column(columnDefinition = "text", length = 4096)
    private String descriptionCn;

    @Schema(description = "运行状态")
    @Column(length = 4096)
    private String suspend;

    @Schema(description = "经验教训主ID")
    @Column
    private Long parentId;

    @Schema(description = "当前经验教训标记")
    @Column
    private Boolean currentExperience;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(Boolean currentExperience) {
        this.currentExperience = currentExperience;
    }

    @Schema(description = "附件 ID")
    private String attachment;

    @Schema(description = "自定义属性列表")
    @Column(length = 4096)
    private String props;

    @Schema(description = "自定义属性")
    @Transient
    private List<IssueProperty> properties;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = StringUtils.trim(no, null);
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

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public List<IssueProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<IssueProperty> properties) {
        this.properties = properties;
    }

    public String getSuspend() {
        return suspend;
    }

    public void setSuspend(String suspend) {
        this.suspend = suspend;
    }

    public Integer getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(Integer seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getOpinionNo() {
        return opinionNo;
    }

    public void setOpinionNo(String opinionNo) {
        this.opinionNo = opinionNo;
    }

    public String getDescriptionCn() {
        return descriptionCn;
    }

    public void setDescriptionCn(String descriptionCn) {
        this.descriptionCn = descriptionCn;
    }
}
