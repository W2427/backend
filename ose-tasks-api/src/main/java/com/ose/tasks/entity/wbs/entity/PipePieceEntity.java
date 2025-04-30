package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.tasks.entity.Project;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * 管段实体数据实体。
 */
@Entity
@Table(name = "entity_pipe_piece",
    indexes = {
        @Index(columnList = "isoEntityId, projectId"),
        @Index(columnList = "no,projectId,deleted")
    }
)
public class PipePieceEntity extends PipePieceEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = -1617143921943979800L;

    @Schema(description = "是否弯管")
    @Transient
    private boolean bending = false;

    @JsonCreator
    public PipePieceEntity() {
        this(null);
    }

    public PipePieceEntity(Project project) {
        super(project);
        setEntityType("PIPE_PIECE");
    }

    public boolean getBending() {
        return bending;
    }


    @Override
    public String getName() {
        return "PipePieceEntity";
    }

    /**
     * 取得工作流参数名。
     *
     * @return 工作流参数名
     */
    @Override
    @JsonIgnore
    public String getVariableName() {
        return "PIPE_PIECE";
    }

    /**
     * 设置工作流参数字段。
     */
    @Override
    @JsonIgnore
    public void setVariableFields() {
        bending = !StringUtils.isEmpty(getBendInfo());
    }

}
