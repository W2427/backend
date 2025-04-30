package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.dto.bizcode.BizCodePatchDTO;
import com.ose.tasks.vo.setting.BizCodeType;
import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务代码表。
 */
@Entity
@Table(name = "biz_code")
public class BizCode extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5571566739435551828L;

    @Schema(description = "公司 ID")
    @Column
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "业务代码分类")
    @Column
    private String type;

    @Schema(description = "业务代码分类名称")
    @Column
    private String typeName;

    @Schema(description = "业务代码")
    @Column(length = 64)
    private String code;

    @Schema(description = "名称")
    @Column
    private String name;

    @Schema(description = "描述")
    @Column
    private String description;

    @Schema(description = "排序顺序")
    @Column
    private Integer sort;

    @JsonCreator
    public BizCode() {
    }

    private BizCode(ValueObject bizCode) {
        this.code = bizCode.name();
        this.name = bizCode.getDisplayName();
        this.description = bizCode.getDescription();
    }

    /**
     * 将业务代码数组转为业务代码列表。
     *
     * @param bizCodeType 业务代码数组
     * @return 业务代码列表
     */
    public static Page<BizCode> list(BizCodeType bizCodeType, PageDTO pageDTO) {

        if (bizCodeType.getValueObjects() == null || bizCodeType.getValueObjects().length == 0) {
            return new PageImpl<>(new ArrayList<>());
        }

        pageDTO.getPage().setSize(100);

        List<BizCode> bizCodeList = new ArrayList<>();
        for (ValueObject bizCode : bizCodeType.getValueObjects()) {
            BizCode bizCodeEntity = new BizCode(bizCode);
            bizCodeEntity.setId(null);
            bizCodeEntity.setCreatedAt(null);
            bizCodeEntity.setLastModifiedAt(null);
            bizCodeEntity.setDeleted(null);
            bizCodeList.add(bizCodeEntity);
        }

        PageRequest pageRequest = PageRequest.of(
            pageDTO.toPageable().getPageNumber(),
            pageDTO.toPageable().getPageSize(),
            Sort.by(Sort.Order.asc("type"))
        );
        // Page构造
        // from index
        int from = (pageRequest.getPageNumber()) * pageRequest.getPageSize();
        // to
        int pageSizeTo = ((pageRequest.getPageNumber() + 1) * pageRequest.getPageSize());
        int to = bizCodeList.size() < pageSizeTo ? bizCodeList.size() : pageSizeTo;
        return new PageImpl<>(bizCodeList.subList(from, to),
            pageRequest, bizCodeList.size());
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 更新业务代码信息。
     *
     * @param operator        操作者信息
     * @param bizCodePatchDTO 业务代码信息
     */
    public BizCode update(OperatorDTO operator, BizCodePatchDTO bizCodePatchDTO) {

        if (!StringUtils.isEmpty(bizCodePatchDTO.getCode())) {
            setCode(bizCodePatchDTO.getCode());
        }

        if (!StringUtils.isEmpty(bizCodePatchDTO.getName())) {
            setName(bizCodePatchDTO.getName());
        }

        if (bizCodePatchDTO.getDescription() != null) {
            setDescription(bizCodePatchDTO.getDescription());
        }

        setLastModifiedAt();
        setLastModifiedBy(operator.getId());

        return this;
    }

    /**
     * 将数据实体状态更新为已删除。
     *
     * @param operator 操作者信息
     * @return 业务代码数据实体
     */
    public BizCode delete(OperatorDTO operator) {
        setDeletedAt();
        setDeletedBy(operator.getId());
        return this;
    }

}
