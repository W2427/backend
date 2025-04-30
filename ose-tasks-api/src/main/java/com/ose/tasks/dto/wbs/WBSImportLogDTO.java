package com.ose.tasks.dto.wbs;


import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.wbs.WBSImportLogStatus;

import java.util.Map;

public class WBSImportLogDTO<T extends WBSEntityBase> extends BaseDTO {

    private int skipCount;

    private int errorCount;

    private int doneCount;

    private int deletedCount;

    private T wbsEntityBase;

    private WBSImportLogStatus status;

    private String errorStr;

    private Map<String, String> parentNodeNoMap;

    private String entityType;

    private String entitySubType;

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(int doneCount) {
        this.doneCount = doneCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public WBSImportLogStatus getStatus() {
        return status;
    }

    public void setStatus(WBSImportLogStatus status) {
        this.status = status;
    }

    public T getWbsEntityBase() {
        return wbsEntityBase;
    }

    public void setWbsEntityBase(T wbsEntityBase) {
        this.wbsEntityBase = wbsEntityBase;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    public Map<String, String> getParentNodeNoMap() {
        return parentNodeNoMap;
    }

    public void setParentNodeNoMap(Map<String, String> parentNodeNoMap) {
        this.parentNodeNoMap = parentNodeNoMap;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }
}
