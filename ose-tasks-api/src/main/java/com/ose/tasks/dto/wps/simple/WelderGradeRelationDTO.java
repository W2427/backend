package com.ose.tasks.dto.wps.simple;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WelderGradeRelationDTO {

    private static final long serialVersionUID = -6488800737800672044L;

    @Schema(description = "WPS编号")
    private List<Long> wpsSimplifyIds;

    @Schema(description = "焊口ids")
    private List<Long> weldIds;

    public List<Long> getWpsSimplifyIds() {
        return wpsSimplifyIds;
    }

    public void setWpsSimplifyIds(List<Long> wpsSimplifyIds) {
        this.wpsSimplifyIds = wpsSimplifyIds;
    }

    public List<Long> getWeldIds() {
        return weldIds;
    }

    public void setWeldIds(List<Long> weldIds) {
        this.weldIds = weldIds;
    }
}
