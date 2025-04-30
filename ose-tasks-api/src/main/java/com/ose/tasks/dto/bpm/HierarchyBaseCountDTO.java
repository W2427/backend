package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;

/**
 * 待办任务条件 数据传输对象
 */
public class HierarchyBaseCountDTO extends BaseDTO {

    private static final long serialVersionUID = 5023458921674290901L;
    private Long count;
    private Long id;
    private String nameCn;
    private String nameEn;
    private Long processStageId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }
}
