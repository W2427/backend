package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 结构Panel实体数据实体基类。 WP3分类
 */
@Entity
@Table(name = "entity_wp03",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted")
    })
public class Wp03Entity extends Wp03EntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = 713699864945997011L;

    @JsonCreator
    public Wp03Entity() {
        this(null);
    }

    public Wp03Entity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }


    @Override
    public String getName() {
        return "Wp03Entity";
    }

    @Override
    public String getVariableName() {
        return "WP03";
    }


}
