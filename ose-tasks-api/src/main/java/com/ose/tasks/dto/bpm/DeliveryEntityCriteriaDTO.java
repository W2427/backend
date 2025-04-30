package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeliveryEntityCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体类型Id")
    private Long entityCategoryId;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "模块id")
    private Long moduleProjectNodeId;

    @Schema(description = "涂装代码")
    private String paintingCode;

    @Schema(description = "试压包")
    private String pressureTestPackage;

    @Schema(description = "任务包")
    private String taskPackageName;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    public Long getEntityCategoryId() {
        return entityCategoryId;
    }

    public void setEntityCategoryId(Long entityCategoryId) {
        this.entityCategoryId = entityCategoryId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getModuleProjectNodeId() {
        return moduleProjectNodeId;
    }

    public void setModuleProjectNodeId(Long moduleProjectNodeId) {
        this.moduleProjectNodeId = moduleProjectNodeId;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
