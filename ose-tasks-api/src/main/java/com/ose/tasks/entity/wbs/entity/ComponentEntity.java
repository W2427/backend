package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 管件实体数据实体。
 */
@Entity
@Table(name = "entity_component",
    indexes = {
        @Index(columnList = "no,projectId,deleted"),
        @Index(columnList = "spoolEntityId,status")
})
public class ComponentEntity extends ComponentEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = -5497199972790434879L;

    @JsonCreator
    public ComponentEntity() {
        this(null);
    }

    public ComponentEntity(Project project) {
        super(project);
        setEntityType("COMPONENT");
    }


    @Override
    public String getName() {
        return "ComponentEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    public String getVariableName() {
        return "COMPONENT";
    }

}
