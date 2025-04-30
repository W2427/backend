package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 领料单详情
 */
@Entity
@Table(name = "m_fah_lns")
@NamedQuery(name = "FahLnsEntity.findAll", query = "SELECT a FROM FahLnsEntity a")
public class FahLnsEntity extends BaseDTO {
    private static final long serialVersionUID = -3760608562193664739L;

    @Id
    @Column(name = "fl_id")
    private Integer flId;

    @Column(name = "project_id")
    private Integer projectId;
    @Schema(description = "WORK PACK TO FAH ID")
    @Column(name = "wptf_id")
    private Integer wptfId;
    @Schema(description = "领料单 ID")
    @Column(name = "fah_id")
    private Integer fahId;
    @Schema(description = "BOM NODE ID")
    @Column(name = "ln_id")
    private Integer lnId;
    @Column(name = "usr_id")
    private Integer usrId;

    @Column(name = "lmod")
    private String lmod;

    @Column(name = "int_rev")
    private String intRev;

    public Integer getFlId() {
        return flId;
    }

    public void setFlId(Integer flId) {
        this.flId = flId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getWptfId() {
        return wptfId;
    }

    public void setWptfId(Integer wptfId) {
        this.wptfId = wptfId;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Integer getLnId() {
        return lnId;
    }

    public void setLnId(Integer lnId) {
        this.lnId = lnId;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getLmod() {
        return lmod;
    }

    public void setLmod(String lmod) {
        this.lmod = lmod;
    }

    public String getIntRev() {
        return intRev;
    }

    public void setIntRev(String intRev) {
        this.intRev = intRev;
    }
}
