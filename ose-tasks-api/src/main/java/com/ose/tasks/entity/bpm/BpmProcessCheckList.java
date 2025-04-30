package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 工序实体类。
 */
@Entity
@Table(
    name = "bpm_process_check_list"
)
public class BpmProcessCheckList extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //工序id
    private Long processId;

    //文件名
    private String fileName;

    //文件id
    private Long fileId;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
