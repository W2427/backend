package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.entity.wbs.entry.WBSEntryPredecessor;
import com.ose.tasks.entity.wbs.entry.WBSEntryRelationBasic;
import com.ose.tasks.entity.wbs.entry.WBSEntrySuccessor;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.RelationType;

/**
 * 项目 WBS 任务关系。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSRelationDTO {

    @Schema(description = "关系目标条目 ID")
    private Long id;

    @Schema(description = "关系类型")
    private RelationType type;

    @JsonIgnore
    private Boolean valid = false;

    public WBSRelationDTO() {
    }

    public WBSRelationDTO(WBSEntryRelationBasic relation) {

        this.type = relation.getRelationType();

        if (relation.getDeleted()) {
            return;
        }

        if (relation instanceof WBSEntryPredecessor) {

            WBSEntryPredecessor predecessor = (WBSEntryPredecessor) relation;

            if (predecessor.getPredecessor() == null
                || predecessor.getPredecessor().getDeleted()) {
                return;
            }

            this.id = predecessor.getPredecessor().getId();

        } else if (relation instanceof WBSEntrySuccessor) {

            WBSEntrySuccessor successor = (WBSEntrySuccessor) relation;

            if (successor.getSuccessor() == null
                || successor.getSuccessor().getDeleted()) {
                return;
            }

            this.id = successor.getSuccessor().getId();

        }

        this.valid = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

}
