package com.ose.docs.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;

/**
 * 文档 ES 数据实体基类。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseES extends BaseDTO {

    private static final long serialVersionUID = 1671200971085929825L;

    @Schema(description = "文档 ID")
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Schema(description = "公司 ID")
    @Field(type = FieldType.Keyword)
    private String companyId;

    @Schema(description = "组织 ID")
    @Field(type = FieldType.Keyword)
    private String orgId;

    public BaseES() {
        setId(BaseEntity.generateId().toString());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
