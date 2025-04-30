package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * BOM详情表
 */
@Entity
@Table(name = "m_list_pos")
@NamedQuery(name = "ListPosEntity.findAll", query = "SELECT a FROM ListPosEntity a")
public class ListPosEntity extends BaseDTO {

    private static final long serialVersionUID = -1348579907869080795L;
    @Id
    @Column(name = "lp_id", nullable = false)
    private Integer lpId;

    @Column(name = "ln_id", nullable = false)
    private Integer lnId;

    @Column(name = "stat_id", nullable = false)
    private Integer statId;

    @Column(name = "control_status", nullable = false)
    private Integer controlStatus = 1;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "lst_id", nullable = false)
    private Integer lstId;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "lp_pos", nullable = false)
    private String lpPos;

    @Column(name = "revision_id1", nullable = false)
    private Integer revisionId1;

    @Column(name = "revision_id2", nullable = false)
    private Integer revisionId2;

    @Column(name = "addend", nullable = false)
    private Double addend = 0.0;

    @Column(name = "factor", nullable = false)
    private Double factor = 1.0;

    @Column(name = "lst_date", nullable = false)
    private Date lstDate;

    @Column(name = "lst_user", nullable = false)
    private String lstUser;

    @Column(name = "quantity", nullable = false)
    private Double quantity = 0.0;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "control_date", nullable = false)
    private Date controlDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "requisition_ind", nullable = false)
    private String requisitionInd = "Y";

    @Column(name = "load_ind", nullable = false)
    private String loadInd = "Y";

    @Column(name = "parent_lp_id")
    private Integer parentLpId;

    @Column(name = "commodity_id")
    private Integer commodityId;

    @Column(name = "part_id")
    private Integer partId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "ident")
    private Integer ident;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "ity_id")
    private Integer ityId;

    @Column(name = "lp_sub_pos")
    private String lpSubPos;

    @Column(name = "input_1")
    private String input1 = "0";

    @Column(name = "input_2")
    private String input2 = "0";

    @Column(name = "input_3")
    private String input3 = "0";

    @Column(name = "input_4")
    private String input4 = "0";

    @Column(name = "input_5")
    private String input5 = "0";

    @Column(name = "reference_name")
    private String referenceName;

    @Column(name = "referenced_ln_id")
    private Integer referencedLnId;

    @Column(name = "recognize_id")
    private Integer recognizeId;

    @Column(name = "resv_qty", nullable = false)
    private Double resvQty = 0.0;

    @Column(name = "spec_header_id")
    private Integer specHeaderId;

    @Column(name = "tag_number")
    private String tagNumber;

    @Column(name = "option_code")
    private String optionCode;

    @Column(name = "object_code")
    private String objectCode;

    @Column(name = "fah_id")
    private Integer fahId;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "mar_error_code")
    private String marErrorCode;

    @Column(name = "mto_progress_id")
    private Integer mtoProgressId;

    @Column(name = "issue_qty", nullable = false)
    private Double issueQty = 0.0;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "weight_unit_id")
    private Integer weightUnitId;

    @Column(name = "plate_id")
    private Integer plateId;

    @Column(name = "last_lp_id")
    private Integer lastLpId;

    @Column(name = "lock_ind", nullable = false)
    private String lockInd = "N";

    @Column(name = "guid")
    private String guid;

    @Column(name = "wptf_id")
    private Integer wptfId;

    @Column(name = "fah_prio")
    private Integer fahPrio;

    public Integer getLpId() {
        return lpId;
    }

    public void setLpId(Integer lpId) {
        this.lpId = lpId;
    }

    public Integer getLnId() {
        return lnId;
    }

    public void setLnId(Integer lnId) {
        this.lnId = lnId;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(Integer controlStatus) {
        this.controlStatus = controlStatus;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getLstId() {
        return lstId;
    }

    public void setLstId(Integer lstId) {
        this.lstId = lstId;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public String getLpPos() {
        return lpPos;
    }

    public void setLpPos(String lpPos) {
        this.lpPos = lpPos;
    }

    public Integer getRevisionId1() {
        return revisionId1;
    }

    public void setRevisionId1(Integer revisionId1) {
        this.revisionId1 = revisionId1;
    }

    public Integer getRevisionId2() {
        return revisionId2;
    }

    public void setRevisionId2(Integer revisionId2) {
        this.revisionId2 = revisionId2;
    }

    public Double getAddend() {
        return addend;
    }

    public void setAddend(Double addend) {
        this.addend = addend;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Date getLstDate() {
        return lstDate;
    }

    public void setLstDate(Date lstDate) {
        this.lstDate = lstDate;
    }

    public String getLstUser() {
        return lstUser;
    }

    public void setLstUser(String lstUser) {
        this.lstUser = lstUser;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public Date getControlDate() {
        return controlDate;
    }

    public void setControlDate(Date controlDate) {
        this.controlDate = controlDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRequisitionInd() {
        return requisitionInd;
    }

    public void setRequisitionInd(String requisitionInd) {
        this.requisitionInd = requisitionInd;
    }

    public String getLoadInd() {
        return loadInd;
    }

    public void setLoadInd(String loadInd) {
        this.loadInd = loadInd;
    }

    public Integer getParentLpId() {
        return parentLpId;
    }

    public void setParentLpId(Integer parentLpId) {
        this.parentLpId = parentLpId;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getItyId() {
        return ityId;
    }

    public void setItyId(Integer ityId) {
        this.ityId = ityId;
    }

    public String getLpSubPos() {
        return lpSubPos;
    }

    public void setLpSubPos(String lpSubPos) {
        this.lpSubPos = lpSubPos;
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input1) {
        this.input1 = input1;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getInput4() {
        return input4;
    }

    public void setInput4(String input4) {
        this.input4 = input4;
    }

    public String getInput5() {
        return input5;
    }

    public void setInput5(String input5) {
        this.input5 = input5;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public Integer getReferencedLnId() {
        return referencedLnId;
    }

    public void setReferencedLnId(Integer referencedLnId) {
        this.referencedLnId = referencedLnId;
    }

    public Integer getRecognizeId() {
        return recognizeId;
    }

    public void setRecognizeId(Integer recognizeId) {
        this.recognizeId = recognizeId;
    }

    public Double getResvQty() {
        return resvQty;
    }

    public void setResvQty(Double resvQty) {
        this.resvQty = resvQty;
    }

    public Integer getSpecHeaderId() {
        return specHeaderId;
    }

    public void setSpecHeaderId(Integer specHeaderId) {
        this.specHeaderId = specHeaderId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getMarErrorCode() {
        return marErrorCode;
    }

    public void setMarErrorCode(String marErrorCode) {
        this.marErrorCode = marErrorCode;
    }

    public Integer getMtoProgressId() {
        return mtoProgressId;
    }

    public void setMtoProgressId(Integer mtoProgressId) {
        this.mtoProgressId = mtoProgressId;
    }

    public Double getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Double issueQty) {
        this.issueQty = issueQty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getWeightUnitId() {
        return weightUnitId;
    }

    public void setWeightUnitId(Integer weightUnitId) {
        this.weightUnitId = weightUnitId;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public Integer getLastLpId() {
        return lastLpId;
    }

    public void setLastLpId(Integer lastLpId) {
        this.lastLpId = lastLpId;
    }

    public String getLockInd() {
        return lockInd;
    }

    public void setLockInd(String lockInd) {
        this.lockInd = lockInd;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getWptfId() {
        return wptfId;
    }

    public void setWptfId(Integer wptfId) {
        this.wptfId = wptfId;
    }

    public Integer getFahPrio() {
        return fahPrio;
    }

    public void setFahPrio(Integer fahPrio) {
        this.fahPrio = fahPrio;
    }
}
