package com.ose.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.dto.BaseDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 * 数据批量获取请求数据传输对象。
 */
public class BatchGetDTO extends BaseDTO {

    private static final long serialVersionUID = -1182906729331144378L;

    @Schema(description = "数据实体 ID 集合")
    private String entityIDs = "";

    /**
     * 构造方法。
     */
    @JsonCreator
    public BatchGetDTO() {
        super();
    }

    /**
     * 构造方法。
     */
    public BatchGetDTO(Collection<Long> entityIDs) {
        this.entityIDs = StringUtils.toJSON(entityIDs);
    }

    public Set<Long> getEntityIDs() {

        if (entityIDs != null && entityIDs != "") {
            return new HashSet<>(StringUtils.decode(entityIDs, new TypeReference<List<Long>>() {
            }));
        }
        return new HashSet<>();
    }

    @JsonSetter
    public void setEntityIDs(Set<Long> entityIDs) {
        if(entityIDs != null) {
            this.entityIDs = StringUtils.toJSON(entityIDs);
        } else {
            this.entityIDs = StringUtils.toJSON(new HashSet<String>());
        }
    }

    /**
     * 添加实体 ID。
     *
     * @param entityId 数据实体 ID
     */
    public void addEntityId(Long entityId) {

        if (entityId == null || entityId == 0L) {
            return;
        }
        List<Long> tmpIds = StringUtils.decode(entityIDs, new TypeReference<List<Long>>() {
        });
        tmpIds.add(entityId);
        entityIDs = StringUtils.toJSON(tmpIds);
    }


}
