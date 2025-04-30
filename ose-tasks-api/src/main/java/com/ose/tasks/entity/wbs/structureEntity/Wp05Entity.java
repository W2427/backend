package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 结构Part实体数据实体基类。 WP5分类
 */
@Entity
@Table(name = "entity_wp05",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted"),
        @Index(columnList = "projectId,no,deleted")
    })
public class Wp05Entity extends Wp05EntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = -8241546979449410235L;

    @JsonCreator
    public Wp05Entity() {
        this(null);
    }

    public Wp05Entity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }


    @Override
    public String getName() {
        return "Wp05Entity";
    }

    @Override
    public String getVariableName() {
        return getEntityType();
    }


}
