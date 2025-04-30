package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "entity_iso",
    indexes = {
        @Index(columnList = "bomLnCode"),
    @Index(columnList = "no,projectId,deleted")
    })
public class ISOEntity extends ISOEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = 8329819402826939156L;

    @JsonCreator
    public ISOEntity() {
        this(null);
    }

    public ISOEntity(Project project) {
        super(project);
    }


    @Override
    public String getName() {
        return "ISOEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    public String getVariableName() {
        return "ISO";
    }

}
