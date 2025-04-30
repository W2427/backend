package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.CategoryRuleType;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类型分类 实体类。
 */
@Entity
@Table(name = "bpm_entity_type")
public class BpmEntityType extends BaseBizEntity {

    private static final long serialVersionUID = -8635813266138087045L;

    @Schema(description = "项目ID")
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Schema(description = "组织ID")
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Schema(description = "中文名称")
    @Column(name = "name_cn", nullable = false, length = 128)
    private String nameCn;

    @Schema(description = "英文名称")
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    @Schema(description = "类型")
    @Column(nullable = false, length = 16)
    private String type;

    @Schema(description = "实体规则分类")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private CategoryRuleType categoryRuleType;

    @Schema(description = "子集实体类型")
    @Column(length = 511)
    private String descestorEntityTypes;

    @Schema(description = "图纸编号规则")
    @Column
    private String dwgPattern;

    @Schema(description = "是否是固定层级")
    private Boolean isFixedLevel = true;

    @Schema(description = "实体数据库读写类 Repository")
    @Column
    private String repositoryClazz;

    @Schema(description = "实体服务接口实现类 entityInterface")
    @Column
    private String entityInterfaceClazz;

    @Schema(description = "专业")
    @Column
    private String discipline;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDwgPattern() {
        return dwgPattern;
    }

    public void setDwgPattern(String dwgPattern) {
        this.dwgPattern = dwgPattern;
    }

    public CategoryRuleType getCategoryRuleType() {
        return categoryRuleType;
    }

    public void setCategoryRuleType(CategoryRuleType categoryRuleType) {
        this.categoryRuleType = categoryRuleType;
    }

    public String getDescestorEntityTypes() {
        return descestorEntityTypes;
    }

    public Boolean getFixedLevel() {
        return isFixedLevel;
    }

    public void setFixedLevel(Boolean fixedLevel) {
        isFixedLevel = fixedLevel;
    }

    public void setDescestorEntityTypes(String descestorEntityTypes) {
        this.descestorEntityTypes = descestorEntityTypes;
    }


    public List<String> getListDescestorEntityTypes() {
        List<String> deETs = new ArrayList<>();
        if(descestorEntityTypes == null) {
            return deETs;
        }
        try {
            String[] descestorETs = descestorEntityTypes.split(",");
            for(String descestorET:descestorETs) {
                deETs.add(StringUtils.trim(descestorET));
            }
        } catch (Exception e) {
            System.out.println(e.toString() + "FTJ456");
        }
        return deETs;
    }

    /**
     * 判断当前节点下是否可以添加指定类型的子节点。
     *
     * @param childType 子节点实体类型
     * @return 当前节点下是否可以添加指定类型的子节点
     */
    public boolean isParentOf(String childType) {
        List<String> descestors = getListDescestorEntityTypes();
        if(!CollectionUtils.isEmpty(descestors)) {
            return descestors.contains(childType);
        }
        return false;
    }

    public String getRepositoryClazz() {
        return repositoryClazz;
    }

    public void setRepositoryClazz(String repositoryClazz) {
        this.repositoryClazz = repositoryClazz;
    }

    public String getEntityInterfaceClazz() {
        return entityInterfaceClazz;
    }

    public void setEntityInterfaceClazz(String entityInterfaceClazz) {
        this.entityInterfaceClazz = entityInterfaceClazz;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
