package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;

/**
 * The persistent class for the mv_mxj_list_nodes database table.
 */
@Entity
@Table(name = "M_COMPANIES")
@NamedQuery(name = "MCompaniesEntity.findAll", query = "SELECT a FROM MCompaniesEntity a")
public class MCompaniesEntity extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy=GenerationType.TABLE)
    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    public MCompaniesEntity() {

    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
