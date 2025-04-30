package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class UserOrgChartDTO extends BaseDTO {

    private static final long serialVersionUID = 8518643261411778664L;
    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "title")
    private String title;

    private List<UserOrgChartDTO> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserOrgChartDTO> getChildren() {
        return children;
    }

    public void setChildren(List<UserOrgChartDTO> children) {
        this.children = children;
    }
}
