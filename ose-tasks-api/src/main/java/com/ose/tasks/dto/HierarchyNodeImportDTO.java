package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目条目导入数据传输对象。
 */
public class HierarchyNodeImportDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String filename;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "专业")
    private String discipline;

    private String hierarchyType;

    private Boolean updateByNo;

    public Boolean getUpdateByNo() {
        return updateByNo;
    }

    public void setUpdateByNo(Boolean updateByNo) {
        this.updateByNo = updateByNo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }
}
