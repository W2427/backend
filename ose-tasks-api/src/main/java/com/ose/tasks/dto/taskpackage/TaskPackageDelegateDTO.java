package com.ose.tasks.dto.taskpackage;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 任务包更新数据传输对象。
 */
public class TaskPackageDelegateDTO extends BaseDTO {

    private static final long serialVersionUID = -6187876063423491831L;

    @Valid
    private List<DelegateDTO> delegates = null;

    public List<DelegateDTO> getDelegates() {
        return delegates;
    }

    public void setDelegates(List<DelegateDTO> delegates) {
        this.delegates = delegates;
    }

    public static class DelegateDTO implements Serializable {

        private static final long serialVersionUID = -7627342371637750711L;

        @Schema(description = "工序 ID 或【工序阶段名称/工序名称】")
        private Long processId;

        @Schema(description = "权限")
        @NotNull
        private UserPrivilege privilege;

        @Schema(description = "工作组 ID")
        private Long teamId;

        @Schema(description = "用户 ID")
        private Long userId;

        public Long getProcessId() {
            return processId;
        }

        public void setProcessId(Long processId) {
            this.processId = processId;
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
}
