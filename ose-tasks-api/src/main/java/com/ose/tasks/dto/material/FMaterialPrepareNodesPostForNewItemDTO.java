package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 添加材料准备单list DTO
 */
public class FMaterialPrepareNodesPostForNewItemDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "节点ID")
    private Long projectNodeId;

    @Schema(description = "节点NO")
    private String projectNodeNo;

    @Schema(description = "节点类型")
    private String projectNodeType;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getProjectNodeNo() {
        return projectNodeNo;
    }

    public void setProjectNodeNo(String projectNodeNo) {
        this.projectNodeNo = projectNodeNo;
    }

    public String getProjectNodeType() {
        return projectNodeType;
    }

    public void setProjectNodeType(String projectNodeType) {
        this.projectNodeType = projectNodeType;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }
}
