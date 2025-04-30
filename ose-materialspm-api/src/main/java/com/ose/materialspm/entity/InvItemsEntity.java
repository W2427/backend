package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 库存单
 */
@Entity
@Table(name = "m_inv_items")
@NamedQuery(name = "InvItemsEntity.findAll", query = "SELECT a FROM InvItemsEntity a")
public class InvItemsEntity extends BaseDTO {

    private static final long serialVersionUID = -879965339227934034L;
    @Id
    @Column(name = "ivi_id")
    private Integer iviId;

    @Column(name = "proj_id")
    @Schema(description = "项目ID")
    private String projId;

    @Column(name = "ident")
    @Schema(description = "唯一码")
    private Integer ident;

    @Column(name = "wh_id")
    @Schema(description = "仓库ID")
    private Integer whId;

    @Column(name = "loc_id")
    @Schema(description = "位置ID")
    private Integer locId;

    @Column(name = "smst_id")
    @Schema(description = "存储状态ID")
    private Integer smstId;

    @Column(name = "unit_id")
    @Schema(description = "单位ID")
    private Integer unitId;

    @Column(name = "dp_id")
    @Schema(description = "部门ID")
    private Integer dpId;

    @Column(name = "recv_qty")
    @Schema(description = "接收数量")
    private Integer recvQty;

    @Column(name = "trans_recv_qty")
    @Schema(description = "传输接收数量")
    private Integer transRecvQty;

    @Column(name = "pics_recv_qty")
    @Schema(description = "图片接收数量")
    private Integer picsRecvQty;

    @Column(name = "oi_recv_qty")
    @Schema(description = "OI接收数量")
    private Integer oiRecvQty;

    @Column(name = "dci_recv_qty")
    @Schema(description = "DCI接收数量")
    private Integer dciRecvQty;

    @Column(name = "subst_trans_recv_qty")
    @Schema(description = "替代传输接收数量")
    private Integer substTransRecvQty;

    @Column(name = "resv_qty")
    @Schema(description = "预留数量")
    private Integer resvQty;

    @Column(name = "man_resv_qty")
    @Schema(description = "人工预留数量")
    private Integer manResvQty;

    @Column(name = "int_resv_qty")
    @Schema(description = "内部预留数量")
    private Integer intResvQty;

    @Column(name = "issue_qty")
    @Schema(description = "出库数量")
    private Integer issueQty;

    @Column(name = "usr_id")
    @Schema(description = "用户ID")
    private String usrId;

    @Column(name = "lmod")
    @Schema(description = "最后修改时间")
    private Date lmod;

    @Column(name = "int_rev")
    @Schema(description = "内部修订")
    private Integer intRev;

    @Column(name = "ity_id")
    @Schema(description = "物品类型ID")
    private Integer ityId;

    @Column(name = "tag_number")
    @Schema(description = "标签编号")
    private String tagNumber;

    @Column(name = "sas_id")
    @Schema(description = "SAS ID")
    private Integer sasId;

    @Column(name = "heat_id")
    @Schema(description = "热处理ID")
    private Integer heatId;

    @Column(name = "ident_deviation")
    @Schema(description = "标识偏差")
    private String identDeviation;

    @Column(name = "plate_id")
    @Schema(description = "板ID")
    private Integer plateId;

    @Column(name = "rti_ret_qty")
    @Schema(description = "RTI退回数量")
    private Integer rtiRetQty;

    // Getters and Setters
    public Integer getIviId() {
        return iviId;
    }

    public void setIviId(Integer iviId) {
        this.iviId = iviId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Integer getSmstId() {
        return smstId;
    }

    public void setSmstId(Integer smstId) {
        this.smstId = smstId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getRecvQty() {
        return recvQty;
    }

    public void setRecvQty(Integer recvQty) {
        this.recvQty = recvQty;
    }

    public Integer getTransRecvQty() {
        return transRecvQty;
    }

    public void setTransRecvQty(Integer transRecvQty) {
        this.transRecvQty = transRecvQty;
    }

    public Integer getPicsRecvQty() {
        return picsRecvQty;
    }

    public void setPicsRecvQty(Integer picsRecvQty) {
        this.picsRecvQty = picsRecvQty;
    }

    public Integer getOiRecvQty() {
        return oiRecvQty;
    }

    public void setOiRecvQty(Integer oiRecvQty) {
        this.oiRecvQty = oiRecvQty;
    }

    public Integer getDciRecvQty() {
        return dciRecvQty;
    }

    public void setDciRecvQty(Integer dciRecvQty) {
        this.dciRecvQty = dciRecvQty;
    }

    public Integer getSubstTransRecvQty() {
        return substTransRecvQty;
    }

    public void setSubstTransRecvQty(Integer substTransRecvQty) {
        this.substTransRecvQty = substTransRecvQty;
    }

    public Integer getResvQty() {
        return resvQty;
    }

    public void setResvQty(Integer resvQty) {
        this.resvQty = resvQty;
    }

    public Integer getManResvQty() {
        return manResvQty;
    }

    public void setManResvQty(Integer manResvQty) {
        this.manResvQty = manResvQty;
    }

    public Integer getIntResvQty() {
        return intResvQty;
    }

    public void setIntResvQty(Integer intResvQty) {
        this.intResvQty = intResvQty;
    }

    public Integer getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Integer issueQty) {
        this.issueQty = issueQty;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Integer getIntRev() {
        return intRev;
    }

    public void setIntRev(Integer intRev) {
        this.intRev = intRev;
    }

    public Integer getItyId() {
        return ityId;
    }

    public void setItyId(Integer ityId) {
        this.ityId = ityId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Integer getSasId() {
        return sasId;
    }

    public void setSasId(Integer sasId) {
        this.sasId = sasId;
    }

    public Integer getHeatId() {
        return heatId;
    }

    public void setHeatId(Integer heatId) {
        this.heatId = heatId;
    }

    public String getIdentDeviation() {
        return identDeviation;
    }

    public void setIdentDeviation(String identDeviation) {
        this.identDeviation = identDeviation;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public Integer getRtiRetQty() {
        return rtiRetQty;
    }

    public void setRtiRetQty(Integer rtiRetQty) {
        this.rtiRetQty = rtiRetQty;
    }
}
