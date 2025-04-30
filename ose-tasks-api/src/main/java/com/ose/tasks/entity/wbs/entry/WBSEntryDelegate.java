package com.ose.tasks.entity.wbs.entry;

import com.ose.auth.vo.UserPrivilege;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * WBS 任务分配记录。
 */
@Entity
@Table(
    name = "wbs_entry_delegate",
    indexes = {
        @Index(columnList = "wbsEntryId,privilege", unique = true)
    }
)
public class WBSEntryDelegate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6566576688700743857L;

    @Schema(description = "WBS 条目 ID")
    @Column
    private Long wbsEntryId;

    @Schema(description = "权限")
    @Column
    @Enumerated(EnumType.STRING)
    private UserPrivilege privilege;

    @Schema(description = "班组 ID")
    @Column
    private Long teamId;

    @Schema(description = "用户 ID")
    @Column
    private Long userId;

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

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

}
