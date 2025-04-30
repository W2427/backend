package com.ose.auth.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 用户资料数据实体类。
 */
@Entity
@Table(name = "team")
public class Team extends BaseVersionedBizEntity {
    private static final long serialVersionUID = 6866017519734210230L;
    @Schema(description = "名称")
    @Column
    private String name;
    @Schema(description = "父级Team Id")
    @Column
    private Long parentTeamId;
    @Schema(description = "父级Team Name")
    @Column
    private String parentTeamName;

    public Long getParentTeamId() {
        return parentTeamId;
    }

    public void setParentTeamId(Long parentTeamId) {
        this.parentTeamId = parentTeamId;
    }

    public String getParentTeamName() {
        return parentTeamName;
    }

    public void setParentTeamName(String parentTeamName) {
        this.parentTeamName = parentTeamName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
