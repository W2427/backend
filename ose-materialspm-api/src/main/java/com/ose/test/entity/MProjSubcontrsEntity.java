package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "M_PROJ_SUBCONTRS")
@NamedQuery(name = "MProjSubcontrsEntity.findAll", query = "SELECT a FROM MProjSubcontrsEntity a")
public class MProjSubcontrsEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "PSU_ID")
    private Long psuId;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "COMPANY_ID")
    private Long companyId;

    public MProjSubcontrsEntity() {

    }

    public Long getPsuId() {
        return psuId;
    }

    public void setPsuId(Long psuId) {
        this.psuId = psuId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
