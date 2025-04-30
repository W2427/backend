package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 结构Structure_Component实体数据实体基类。WP4分类
 */
@Entity
@Table(name = "entity_wp04",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted"),
        @Index(columnList = "projectId,id,deleted")
    })
public class Wp04Entity extends Wp04EntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = -1597944681752260224L;

    @JsonCreator
    public Wp04Entity() {
        this(null);
    }

    public Wp04Entity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }


    @Override
    public String getName() {
        return "Wp04Entity";
    }

    @Override
    public String getVariableName() {
        return getEntityType();
    }


}
