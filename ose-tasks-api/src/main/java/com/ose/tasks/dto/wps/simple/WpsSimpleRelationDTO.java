package com.ose.tasks.dto.wps.simple;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WpsSimpleRelationDTO {

    private static final long serialVersionUID = -4831573614893973701L;

    @Schema(description = "焊工等级编号")
    private List<Long> welderGradeIds;

    @Schema(description = "焊口ids")
    private List<Long> weldIds;

    public List<Long> getWelderGradeIds() {
        return welderGradeIds;
    }

    public void setWelderGradeIds(List<Long> welderGradeIds) {
        this.welderGradeIds = welderGradeIds;
    }

    public List<Long> getWeldIds() {
        return weldIds;
    }

    public void setWeldIds(List<Long> weldIds) {
        this.weldIds = weldIds;
    }
}
