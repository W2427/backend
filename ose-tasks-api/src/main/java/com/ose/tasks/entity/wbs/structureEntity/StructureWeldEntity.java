package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * structure weld 实体数据实体基类。 STRUCT_WELD_JOINT 分类
 */
@Entity
@Table(name = "entity_structure_weld",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted"),
        @Index(columnList = "projectId,id,deleted"),
        @Index(columnList = "part1_id"),
        @Index(columnList = "part2_id")
    })
public class StructureWeldEntity extends StructureWeldEntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = -7767296594521623707L;

    @JsonCreator
    public StructureWeldEntity() {
        this(null);
    }

    public StructureWeldEntity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

    @Override
    public String getName() {
        return "StructureWeldEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    @JsonIgnore
    public String getVariableName() {
        return getEntityType();
    }

    /**
     * 设置工作流参数字段。
     */
}
