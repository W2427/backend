package com.ose.auth.domain.model.service;

import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 用户接口。
 */
public interface UserSettingInterface extends EntityInterface {

    Company createCompany(Long operatorId, CompanyDTO dto);
    Page<Company> searchCompany(CompanyDTO dto, PageDTO page);
    Company detailCompany(Long id);
    Company updateCompany(Long id, CompanyDTO dto, ContextDTO contextDTO );
    void deleteCompany(Long id , ContextDTO contextDTO);
    Division createDivision(Long operatorId, CompanyDTO dto);
    Page<Division> searchDivision(CompanyDTO dto, PageDTO page);
    Division detailDivision(Long id);
    Division updateDivision(Long id, CompanyDTO dto, ContextDTO contextDTO );
    void deleteDivision(Long id , ContextDTO contextDTO);
    Department createDepartment(Long operatorId, CompanyDTO dto);
    Page<Department> searchDepartment(CompanyDTO dto, PageDTO page);
    Department detailDepartment(Long id);
    Department updateDepartment(Long id, CompanyDTO dto, ContextDTO contextDTO );
    void deleteDepartment(Long id , ContextDTO contextDTO);
    Team createTeam(Long operatorId, CompanyDTO dto);
    Page<Team> searchTeam(CompanyDTO dto, PageDTO page);
    Team detailTeam(Long id);
    Team updateTeam(Long id, CompanyDTO dto, ContextDTO contextDTO );
    void deleteTeam(Long id , ContextDTO contextDTO);
    List<Company> searchAllCompany();
    List<Division> searchAllDivision();
    List<Department> searchAllDepartment();
    List<Team> searchAllTeam();
}
