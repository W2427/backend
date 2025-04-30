package com.ose.tasks.dto.bpm;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class ProcessHierarchyDTO extends BaseDTO {

    private static final long serialVersionUID = -8231282655407852000L;

    @Schema(description = "工序阶段 - 工序")
    private String label;

    @Schema(description = "工序阶段 - 工序")
    private Long value;

    @Schema(description = "工序阶段 - 工序")
    private List<ProcessHierarchyDTO> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public List<ProcessHierarchyDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ProcessHierarchyDTO> children) {
        this.children = children;
    }
}
