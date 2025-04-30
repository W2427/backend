package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 任务包-实体创建数据传输对象。
 */
public class TaskPackageEntityRelationCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -6972432854901465775L;

    @Schema(description = "实体列表")
//    @NotNull
//    @Size(min = 1)
//    @Valid
    private EntityDTO[] entities;

    public EntityDTO[] getEntities() {
        return entities;
    }

    public void setEntities(EntityDTO[] entities) {
        this.entities = entities;
    }

    /**
     * 实体信息数据传输对象。
     */
    public static class EntityDTO implements Serializable {

        private static final long serialVersionUID = -1977738267579243125L;

        @Schema(description = "实体类型")
        @NotNull
        private String entityType;

        @Schema(description = "实体 ID")
        private Long entityId;

        @Schema(description = "专业")
        @NotBlank
        private String discipline;


        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }

        public String getDiscipline() {
            return discipline;
        }

        public void setDiscipline(String discipline) {
            this.discipline = discipline;
        }
    }

}
