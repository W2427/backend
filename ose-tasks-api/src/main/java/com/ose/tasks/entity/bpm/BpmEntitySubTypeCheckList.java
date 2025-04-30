package com.ose.tasks.entity.bpm;


import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 工序实体类。
 */
@Entity
@Table(
    name = "bpm_entity_sub_type_check_list"
)
public class BpmEntitySubTypeCheckList extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //实体类型id
    private Long entitySubTypeId;

    //文件名
    private String fileName;

    //文件id
    private Long fileId;


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

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }
}
