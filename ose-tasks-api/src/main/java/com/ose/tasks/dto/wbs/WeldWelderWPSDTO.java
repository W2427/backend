package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WeldWelderWPSDTO extends BaseDTO {
    private static final long serialVersionUID = -5146750981801573035L;

    @Schema(description = "焊口实体Ids")
    private List<Long> weldIds;

    @Schema(description = "焊工登陆Ids")
    private List<Long> welderIds;

    @Schema(description = "pageSize 一次查询条数")
    private Integer pageSize = 20;

    @Schema(description = "pageNo 查询的页码")
    private Integer pageNo = 1;

    public List<Long> getWeldIds() {
        return weldIds;
    }

    public void setWeldIds(List<Long> weldIds) {
        this.weldIds = weldIds;
    }

    public List<Long> getWelderIds() {
        return welderIds;
    }

    public void setWelderIds(List<Long> welderIds) {
        this.welderIds = welderIds;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
