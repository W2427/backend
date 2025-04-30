package com.ose.tasks.entity.material;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "mat_coding_template")
public class MaterialCodingTemplate extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //材料编码模板id
    @Column(name = "template_id")
    private String templateCode;

    private String name;

    /*    private String path;*/

    private String description;

    private String codeGeoDelimiter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeGeoDelimiter() {
        return codeGeoDelimiter;
    }

    public void setCodeGeoDelimiter(String codeGeoDelimiter) {
        this.codeGeoDelimiter = codeGeoDelimiter;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
