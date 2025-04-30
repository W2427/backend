package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 实体二维码数据传输对象。
 */
public class EntityQrCodeCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "打印Flag")
    private Boolean printFlg;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体ids")
    private List<Long> entityIds;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getPrintFlg() {
        return printFlg;
    }

    public void setPrintFlg(Boolean printFlg) {
        this.printFlg = printFlg;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }
}
