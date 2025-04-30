package com.ose.auth.dto;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


/**
 * 角色数据传输对象。
 */
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 5862174337666356487L;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编号")
    private String no;

    @Schema(description = "权限")
    @Enumerated(EnumType.STRING)
    protected List<UserPrivilege> privileges;

    @Schema(description = "排序目标角色ID（top：置顶，ID：排在目标ID后面）")
    private Long afterId;

    @Schema(description = "是否为角色模板")
    private boolean template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<UserPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {

        this.privileges = UserPrivilege.getByCodes(privileges);
    }

    public Long getAfterId() {
        return afterId;
    }

    public void setAfterId(Long afterId) {
        this.afterId = afterId;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }
}
