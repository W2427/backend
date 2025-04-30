package com.ose.materialspm.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.ose.dto.BaseDTO;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "M_WAREHOUSES")
@NamedQuery(name = "MWareHouseEntity.findAll", query = "SELECT a FROM MWareHouseEntity a")
public class MWareHouseEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "WH_ID")
    private Long whId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "WH_CODE")
    private String whCode;

    @Column(name = "DP_ID")
    private Long dpId;

    @Column(name = "USR_ID")
    private String usrId;

    @Column(name = "LMOD")
    private Date lmod;

    @Column(name = "INT_REV")
    private Long intRev;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    @Column(name = "MULTI_WH_IND")
    private String multiWhInd;

    @Column(name = "DE_CH_IS_WH_IND")
    private String deChIsWhInd;

    @Column(name = "SCRAP_WH_IND")
    private String scrapWhInd;

    @Column(name = "NESTED_PLATE_IND")
    private String nestedPlateInd;

    @Column(name = "CORPORATE_WH_IND")
    private String corporateWhInd;

    @Column(name = "FREE_MATERIAL_WH_IND")
    private String freeMaterialWhInd;

    @Column(name = "SHORT_DESC")
    private String shortDesc;

    @Column(name = "DESCRIPTION")
    private String description;

    public MWareHouseEntity() {

    }

    public Long getWhId() {
        return whId;
    }

    public void setWhId(Long whId) {
        this.whId = whId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public Long getDpId() {
        return dpId;
    }

    public void setDpId(Long dpId) {
        this.dpId = dpId;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Long getIntRev() {
        return intRev;
    }

    public void setIntRev(Long intRev) {
        this.intRev = intRev;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getMultiWhInd() {
        return multiWhInd;
    }

    public void setMultiWhInd(String multiWhInd) {
        this.multiWhInd = multiWhInd;
    }

    public String getDeChIsWhInd() {
        return deChIsWhInd;
    }

    public void setDeChIsWhInd(String deChIsWhInd) {
        this.deChIsWhInd = deChIsWhInd;
    }

    public String getScrapWhInd() {
        return scrapWhInd;
    }

    public void setScrapWhInd(String scrapWhInd) {
        this.scrapWhInd = scrapWhInd;
    }

    public String getNestedPlateInd() {
        return nestedPlateInd;
    }

    public void setNestedPlateInd(String nestedPlateInd) {
        this.nestedPlateInd = nestedPlateInd;
    }

    public String getCorporateWhInd() {
        return corporateWhInd;
    }

    public void setCorporateWhInd(String corporateWhInd) {
        this.corporateWhInd = corporateWhInd;
    }

    public String getFreeMaterialWhInd() {
        return freeMaterialWhInd;
    }

    public void setFreeMaterialWhInd(String freeMaterialWhInd) {
        this.freeMaterialWhInd = freeMaterialWhInd;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
