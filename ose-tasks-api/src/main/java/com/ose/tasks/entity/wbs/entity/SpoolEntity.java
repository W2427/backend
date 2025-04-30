package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.util.StringUtils;
import com.ose.vo.QrcodePrefixType;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 单管实体数据实体。
 */
@Entity
@Table(name = "entity_spool",
    indexes = {
        @Index(columnList = "no,projectId,deleted")
    })
public class SpoolEntity extends SpoolEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = 7543496169355783210L;

    @JsonCreator
    public SpoolEntity() {
        this(null);
    }

    public SpoolEntity(Project project) {
        super(project);
        setEntityType("SPOOL");
        setQrCode(QrcodePrefixType.SPOOL.getCode() + StringUtils.generateShortUuid());
    }


    @Override
    public String getName() {
        return "SpoolEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    public String getVariableName() {
        return "SPOOL";
    }

}
