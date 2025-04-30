package com.ose.tasks.entity.material;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "mat_code")
@NamedStoredProcedureQuery(name = "MaterialCodingSearch", procedureName = "material_coding_search", parameters = {
    @StoredProcedureParameter(mode = ParameterMode.IN, name = "input_code", type = String.class),
    @StoredProcedureParameter(mode = ParameterMode.OUT, name = "out_code", type = String.class)})
public class MaterialCode extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //材料编码模板id 4P/12:13:14:15:16:17:18-4
    @Column(name = "template_detail_id")
    private String templateDetailCode;

    //代码
    private String code;

    //描述
    private String description;

    //是否是默认值
    private String defaultValue;

    //是否是BIZ LIB
    private String displineLib;

    //备注
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDisplineLib() {
        return displineLib;
    }

    public void setDisplineLib(String displineLib) {
        this.displineLib = displineLib;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTemplateDetailCode() {
        return templateDetailCode;
    }

    public void setTemplateDetailCode(String templateDetailCode) {
        this.templateDetailCode = templateDetailCode;
    }
}
