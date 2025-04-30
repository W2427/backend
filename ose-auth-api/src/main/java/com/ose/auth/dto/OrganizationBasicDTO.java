package com.ose.auth.dto;

import com.ose.auth.vo.OrgType;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 组织基本信息。
 */
public class OrganizationBasicDTO extends BaseDTO {

    private static final long serialVersionUID = -5683868523935140608L;

    @Schema(description = "组织 ID")
    private Long id;

    @Schema(description = "组织类型")
    private String type;

    @Schema(description = "组织名称")
    private String name;

    /**
     * 构造方法。
     */
    public OrganizationBasicDTO() {
    }

    /**
     * 构造方法。
     */
    public OrganizationBasicDTO(Long id, OrgType type, String name) {
        this.id = id;
        this.type = type.name();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
