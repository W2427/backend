package com.ose.tasks.entity;

import com.ose.dto.BaseDTO;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 项目相关信息实体
 * PROJECT ID
 * PROJECT INFO KEY
 * PROJECT INFO VALUE

 */
@Entity
@Table(name = "project_info")
public class ProjectInfo extends BaseDTO {

    private static final long serialVersionUID = -2075758396963985983L;

    @Schema(description = "实体 ID")
    @Id
    @Column
    private Long id;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;        //项目ID

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "INFO KEY")
    @Column(nullable = false)
    private String infoKey;

    @Column
    private boolean deleted = false;

    // 出外检报告时会用到
    @Schema(description = "INFO VALUE")
    @Column
    private String infoValue;

    // 出外检报告时会用到
    @Schema(description = "memo")
    @Column
    private String memo;

    /**
     * 默认构造方法。
     */
    public ProjectInfo() {
        this(generateId());
    }

    /**
     * 构造方法。
     *
     * @param id 实体 ID
     */
    public ProjectInfo(Long id) {
        this.setId(id == null ? generateId() : id);
    }

    /**
     * 生成 bigint/long ID。
     *
     * @return ID
     */
    public static Long generateId() {
        return CryptoUtils.uniqueDecId();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 取得数据实体 ID。
     *
     * @return 数据实体 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置数据实体 ID。
     *
     * @param id 数据实体 ID
     */
    public void setId(Long id) {
        this.id = id;
    }
}
