package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 任务包图纸数据传输对象。
 */
public class TaskPackageDrawingIdDTO extends BaseDTO {

    private static final long serialVersionUID = 3509325385524123064L;

    @Schema(description = "图纸 ID 集合")
    private Set<Long> drawingIDs;

    public TaskPackageDrawingIdDTO() {
    }

    public TaskPackageDrawingIdDTO(Collection<Long> drawingIDs) {
        setDrawingIDs(new HashSet<>(drawingIDs));
    }

    public Set<Long> getDrawingIDs() {
        return drawingIDs;
    }

    public void setDrawingIDs(Set<Long> drawingIDs) {
        this.drawingIDs = drawingIDs;
    }
}
