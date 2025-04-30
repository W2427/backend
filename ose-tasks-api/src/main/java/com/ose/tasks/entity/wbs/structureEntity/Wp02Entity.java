package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 结构Section实体数据实体基类。 WP2分类
 */
@Entity
@Table(name = "entity_wp02",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted")
    })
public class Wp02Entity extends Wp02EntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = -565508081965637668L;

    @JsonCreator
    public Wp02Entity() {
        this(null);
    }

    public Wp02Entity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }


    @Override
    public String getName() {
        return "Wp02Entity";
    }

    @Override
    public String getVariableName() {
        return getEntityType();
    }

}
