package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class WeldWPSMatchResultDTO extends BaseDTO {

    List<WeldMatchDTO> weldMatchDTOList = new ArrayList<>();

    /**
     * WPS匹配成功件数
     */
    int successCount;

    /**
     * WPS匹配跳过件数
     */
    int skipCount;

    /**
     * WPS匹配失败件数
     */
    int failureCount;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public List<WeldMatchDTO> getWeldMatchDTOList() {
        return weldMatchDTOList;
    }

    public void setWeldMatchDTOList(List<WeldMatchDTO> weldMatchDTOList) {
        this.weldMatchDTOList = weldMatchDTOList;
    }
}
