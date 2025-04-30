package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 焊口实体数据实体。
 */
@Entity
@Table(name = "entity_weld",
    indexes = {
        @Index(columnList = "projectId,parentEntityId"),
        @Index(columnList = "projectId,no,deleted")
    })
public class WeldEntity extends WeldEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = 1981545669792243059L;

    @JsonCreator
    public WeldEntity() {
        this(null);
    }

    public WeldEntity(Project project) {
        super(project);
        setEntityType("WELD_JOINT");
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    public String getVariableName() {
        return "WELD_JOINT";
    }

    @Override
    public String getName() {
        return "WeldEntity";
    }

    /**
     * 设置工作流参数字段。
     */
    @Override
    public void setVariableFields() {
        setPmiRatio(getPmiRatio() == null ? 0 : getPmiRatio());
        setNdeRatio(getNdeRatio() == null ? 0 : getNdeRatio());
    }

}
