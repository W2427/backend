package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;


public class ExInspInfoDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 3799468155069076241L;

    // 外检报告列表
    private List<ExInspDocDTO> externalInspectionDocList = new ArrayList<>();

    @Schema(description = "工序中文名称-工序阶段中文名称")
    private List<String> processNameStageNamePairList;

    public List<ExInspDocDTO> getExternalInspectionDocList() {
        return externalInspectionDocList;
    }

    public void setExternalInspectionDocList(List<ExInspDocDTO> externalInspectionDocList) {
        this.externalInspectionDocList = externalInspectionDocList;
    }

    public List<String> getProcessNameStageNamePairList() {
        return processNameStageNamePairList;
    }

    public void setProcessNameStageNamePairList(List<String> processNameStageNamePairList) {
        this.processNameStageNamePairList = processNameStageNamePairList;
    }
}
