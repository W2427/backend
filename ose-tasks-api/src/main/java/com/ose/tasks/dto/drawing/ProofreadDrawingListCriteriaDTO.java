package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class ProofreadDrawingListCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "查询的列表类型")
    private ProofreadType type;

    private boolean subDrawingFlg = false;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ProofreadType getType() {
        return type;
    }

    public void setType(ProofreadType type) {
        this.type = type;
    }

    public enum ProofreadType {
        MODIFY,
        CHECK,
        REVIEW
    }

    public boolean isSubDrawingFlg() {
        return subDrawingFlg;
    }

    public void setSubDrawingFlg(boolean subDrawingFlg) {
        this.subDrawingFlg = subDrawingFlg;
    }
}
