package com.ose.tasks.entity.wps.simple;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.WelderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "welder_simplified",
indexes = {
    @Index(columnList = "orgId,projectId,no,status")
})
public class WelderSimplified extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 6561481741272317827L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "焊工编号")
    @Column
    private String no;

    @Schema(description = "焊工名")
    @Column
    private String name;

    @Schema(description = "用户 ID")
    @Column
    private Long userId;

    @Schema(description = "用户头像")
    @Column
    private Long photo;

    @Schema(description = "公司 ID")
    @Column
    private Long subConId;

    @Schema(description = "考试WPS")
    @Column
    private String wpsWelded;

    @Schema(description = "考试焊接方法")
    @Column
    private String processWelded;

    @Schema(description = "身份证号")
    @Column
    private String idCard;

    @Schema(description = "备注")
    @Column(length = 5000)
    private String remark;

    @Schema(description = "证书过期时间")
    @Column
    private Date cerExpirationAt;

    @Schema(description = "证书编号")
    @Column
    private String cerId;

    @Schema(description = "焊工状态")
    @Column
    @Enumerated(EnumType.STRING)
    private WelderStatus welderStatus;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPhoto() {
        return photo;
    }

    public void setPhoto(Long photo) {
        this.photo = photo;
    }

    public Long getSubConId() {
        return subConId;
    }

    public void setSubConId(Long subConId) {
        this.subConId = subConId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWpsWelded() {
        return wpsWelded;
    }

    public void setWpsWelded(String wpsWelded) {
        this.wpsWelded = wpsWelded;
    }

    public String getProcessWelded() {
        return processWelded;
    }

    public void setProcessWelded(String processWelded) {
        this.processWelded = processWelded;
    }

    public Date getCerExpirationAt() {
        return cerExpirationAt;
    }

    public void setCerExpirationAt(Date cerExpirationAt) {
        this.cerExpirationAt = cerExpirationAt;
    }

    public String getCerId() {
        return cerId;
    }

    public void setCerId(String cerId) {
        this.cerId = cerId;
    }

    public WelderStatus getWelderStatus() {
        return welderStatus;
    }

    public void setWelderStatus(WelderStatus welderStatus) {
        this.welderStatus = welderStatus;
    }

    @JsonProperty(value = "subConId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getSubConIdReference() {
        return this.subConId == null ? null : new ReferenceData(this.subConId);
    }

    @JsonProperty(value = "userId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getUserIdReference() {
        return this.userId == null ? null : new ReferenceData(this.userId);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.userId);
        return relatedUserIDs;
    }
}
