package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "entity_line",
    indexes = {
    @Index(columnList = "projectId,no,deleted")
    })
public class LineEntity extends LineEntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = -4446921155046603381L;

    @JsonCreator
    public LineEntity() {
        this(null);
    }

    public LineEntity(Project project) {
        super(project);
    }


    @Override
    public String getName() {
        return "LineEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    public String getVariableName() {
        return "LINE";
    }

}
