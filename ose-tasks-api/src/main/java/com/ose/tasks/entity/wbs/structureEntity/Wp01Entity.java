package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WorkflowProcessVariable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * WP01 Module实体数据实体基类。 WP1 分类
 */
@Entity
@Table(name = "entity_wp01",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId"),
        @Index(columnList = "no,projectId,deleted")
    })
public class Wp01Entity extends Wp01EntityBase implements WorkflowProcessVariable {


    private static final long serialVersionUID = 5565759854375473308L;

    @JsonCreator
    public Wp01Entity() {
        this(null);
    }

    public Wp01Entity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

    @Override
    public String getName() {
        return "Wp01Entity";
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


}
