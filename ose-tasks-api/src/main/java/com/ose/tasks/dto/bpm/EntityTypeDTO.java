package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.bpm.CategoryRuleType;

/**
 * 实体分类类型 数据传输对象
 */
public class EntityTypeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    // 实体规则分类
    private CategoryRuleType categoryRuleType;

    private Boolean fixedLevel;

    private String descestorEntityTypes;

    private String discipline;

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

    public CategoryRuleType getCategoryRuleType() {
        return categoryRuleType;
    }

    public void setCategoryRuleType(CategoryRuleType categoryRuleType) {
        this.categoryRuleType = categoryRuleType;
    }

    public Boolean getFixedLevel() {
        return fixedLevel;
    }

    public void setFixedLevel(Boolean fixedLevel) {
        this.fixedLevel = fixedLevel;
    }

    public String getDescestorEntityTypes() {
        return descestorEntityTypes;
    }

    public void setDescestorEntityTypes(String descestorEntityTypes) {
        this.descestorEntityTypes = descestorEntityTypes;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
