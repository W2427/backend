package com.ose.tasks.entity.drawing;

import com.ose.auth.vo.UserPrivilege;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 图纸 流程人员 权限分配表
 */
@Entity
@Table(name = "drawing_entry_delegate")
public class DrawingEntryDelegate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6566576688700743857L;

    @Schema(description = "图纸id")
    @Column
    private Long drawingId;

    @Schema(description = "用户权限")
    @Column
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege;

    @Schema(description = "班组 ID")
    @Column
    private Long teamId;

    @Schema(description = "用户 ID")
    @Column
    private Long userId;


    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDrawingId() {
        return drawingId;
    }

    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
    }

}
