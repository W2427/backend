package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/28
 */
public class StageVersionDTO extends BaseDTO {
    private static final long serialVersionUID = 6991048121033058639L;

    @Schema(description = "版本代码")
    private String versionCode;

    @Schema(description = "阶段")
    private String stage;

    @Schema(description = "阶段Id")
    private String stageId;

    @Schema(description = "备注")
    private String remark;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
