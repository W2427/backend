package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;

/**
 * 待办任务条件 数据传输对象
 */
public class HierarchyBaseDTO extends BaseDTO {

    public HierarchyBaseDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;

    private Long id;

    private String nameCn;

    private String nameEn;

    public HierarchyBaseDTO(Long id, String nameCn, String nameEn) {
        super();
        this.id = id;
        this.nameCn = nameCn;
        this.nameEn = nameEn;
    }

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

}
