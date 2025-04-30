package com.ose.auth.domain.model.service;

import com.ose.exception.BusinessError;
import com.ose.util.BeanUtils;
import com.ose.auth.domain.model.repository.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.*;
import com.ose.util.*;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class UserSettingService implements UserSettingInterface {
    // 用户信息操作仓库
    private final CompanyRepository companyRepository;
    private final DivisionRepository divisionRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public UserSettingService(
        CompanyRepository companyRepository,
        DivisionRepository divisionRepository,
        DepartmentRepository departmentRepository,
        TeamRepository teamRepository
    ) {
        this.companyRepository = companyRepository;
        this.divisionRepository = divisionRepository;
        this.departmentRepository = departmentRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * 创建公司。
     *
     * @param operatorId 操作者 ID
     * @param dto        用户数据
     */
    @Override
    public Company createCompany(Long operatorId, CompanyDTO dto) {
        Company company = BeanUtils.copyProperties(dto, new Company());
        //name check
        if (company.getName() == null) {
            throw new BusinessError("company's name is required");
        }

        //country check
        if (company.getCountry() == null) {
            throw new BusinessError("company's country is required");
        }

        Company companyFind = companyRepository.findByNameAndCountryAndDeletedIsFalse(company.getName(), company.getCountry());
        if (companyFind != null) {
            throw new BusinessError("company already exists");
        }
        company.setStatus(EntityStatus.ACTIVE);
        company.setCreatedAt(new Date());
        company.setCreatedBy(operatorId);

        return companyRepository.save(company);
    }

    @Override
    public Page<Company> searchCompany(CompanyDTO dto, PageDTO page) {
        return companyRepository.search(dto, page.toPageable());
    }

    @Override
    public Company detailCompany(Long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new BusinessError("company does not exist");
        }
        return optionalCompany.get();
    }

    @Override
    public Company updateCompany(Long id, CompanyDTO dto, ContextDTO contextDTO) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new BusinessError("company does not exist");
        }

        Company companyFind = companyRepository.findByNameAndCountryAndDeletedIsFalse(dto.getName(), dto.getCountry());
        if (companyFind != null) {
            throw new BusinessError("company already exists");
        }
        Company company = optionalCompany.get();
        BeanUtils.copyProperties(dto, company);

        company.setLastModifiedAt(new Date());
        company.setLastModifiedBy(contextDTO.getOperator().getId());
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompany(Long id, ContextDTO contextDTO) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new BusinessError("company does not exist");
        }
        Company company = optionalCompany.get();
        company.setDeleted(true);
        company.setStatus(EntityStatus.DELETED);
        company.setDeletedAt(new Date());
        company.setDeletedBy(contextDTO.getOperator().getId());
        company.setLastModifiedAt(new Date());
        company.setLastModifiedBy(contextDTO.getOperator().getId());

        companyRepository.save(company);

    }

    @Override
    public Division createDivision(Long operatorId, CompanyDTO dto) {
        Division division = BeanUtils.copyProperties(dto, new Division());
        //name check
        if (division.getName() == null) {
            throw new BusinessError("division's name is required");
        }
        Division divisionFind = divisionRepository.findByNameAndDeletedIsFalse(division.getName());
        if (divisionFind != null) {
            throw new BusinessError("division already exists");
        }

        division.setStatus(EntityStatus.ACTIVE);
        division.setCreatedAt(new Date());
        division.setCreatedBy(operatorId);

        return divisionRepository.save(division);
    }

    @Override
    public Page<Division> searchDivision(CompanyDTO dto, PageDTO page) {
        return divisionRepository.search(dto, page.toPageable());
    }

    @Override
    public Division detailDivision(Long id) {
        Optional<Division> optionalDivision = divisionRepository.findById(id);
        if (!optionalDivision.isPresent()) {
            throw new BusinessError("division does not exist");
        }
        return optionalDivision.get();
    }

    @Override
    public Division updateDivision(Long id, CompanyDTO dto, ContextDTO contextDTO) {
        Optional<Division> optionalCompany = divisionRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new BusinessError("division does not exist");
        }
        Division divisionFind = divisionRepository.findByNameAndDeletedIsFalse(dto.getName());
        if (divisionFind != null) {
            throw new BusinessError("division already exists");
        }
        Division division = optionalCompany.get();
        BeanUtils.copyProperties(dto, division);

        division.setLastModifiedAt(new Date());
        division.setLastModifiedBy(contextDTO.getOperator().getId());
        return divisionRepository.save(division);
    }

    @Override
    public void deleteDivision(Long id, ContextDTO contextDTO) {
        Optional<Division> optionalCompany = divisionRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new BusinessError("division does not exist");
        }
        Division division = optionalCompany.get();
        division.setDeleted(true);
        division.setStatus(EntityStatus.DELETED);
        division.setDeletedAt(new Date());
        division.setDeletedBy(contextDTO.getOperator().getId());
        division.setLastModifiedAt(new Date());
        division.setLastModifiedBy(contextDTO.getOperator().getId());

        divisionRepository.save(division);
    }

    @Override
    public Department createDepartment(Long operatorId, CompanyDTO dto) {
        Department department = BeanUtils.copyProperties(dto, new Department());
        //name check
        if (department.getName() == null) {
            throw new BusinessError("department's name is required");
        }
        Department departmentFind = departmentRepository.findByNameAndDeletedIsFalse(department.getName());
        if (departmentFind != null) {
            throw new BusinessError("department already exists");
        }
        department.setStatus(EntityStatus.ACTIVE);
        department.setCreatedAt(new Date());
        department.setCreatedBy(operatorId);

        return departmentRepository.save(department);
    }

    @Override
    public Page<Department> searchDepartment(CompanyDTO dto, PageDTO page) {
        return departmentRepository.search(dto, page.toPageable());
    }

    @Override
    public Department detailDepartment(Long id) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Department does not exist");
        }
        return optional.get();
    }

    @Override
    public Department updateDepartment(Long id, CompanyDTO dto, ContextDTO contextDTO) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Department does not exist");
        }
        Department departmentFind = departmentRepository.findByNameAndDeletedIsFalse(dto.getName());
        if (departmentFind != null) {
            throw new BusinessError("department already exists");
        }
        Department department = optional.get();
        BeanUtils.copyProperties(dto, department);
        department.setLastModifiedAt(new Date());
        department.setLastModifiedBy(contextDTO.getOperator().getId());
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long id, ContextDTO contextDTO) {
        Optional<Department> optional = departmentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Department does not exist");
        }
        Department department = optional.get();
        department.setDeleted(true);
        department.setStatus(EntityStatus.DELETED);
        department.setDeletedAt(new Date());
        department.setDeletedBy(contextDTO.getOperator().getId());
        department.setLastModifiedAt(new Date());
        department.setLastModifiedBy(contextDTO.getOperator().getId());

        departmentRepository.save(department);

    }

    @Override
    public Team createTeam(Long operatorId, CompanyDTO dto) {
        Team team = BeanUtils.copyProperties(dto, new Team());
        //name check
        if (team.getName() == null) {
            throw new BusinessError("team's name is required");
        }
        Team teamFind = teamRepository.findByNameAndDeletedIsFalse(team.getName());
        if (teamFind != null) {
            throw new BusinessError("team already exists");
        }
        if (dto.getParentTeamName().equals(team.getName())) {
            throw new BusinessError("parent team cannot be itself");
        }
        if (team.getParentTeamName() != null && !"".equals(team.getParentTeamName())) {
            Team parent = teamRepository.findByNameAndDeletedIsFalse(team.getParentTeamName());
            team.setParentTeamId(parent.getId());
        }
        team.setStatus(EntityStatus.ACTIVE);
        team.setCreatedAt(new Date());
        team.setCreatedBy(operatorId);

        return teamRepository.save(team);
    }

    @Override
    public Page<Team> searchTeam(CompanyDTO dto, PageDTO page) {
        return teamRepository.search(dto, page.toPageable());
    }

    @Override
    public Team detailTeam(Long id) {
        Optional<Team> optional = teamRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Department does not exist");
        }
        return optional.get();
    }

    @Override
    public Team updateTeam(Long id, CompanyDTO dto, ContextDTO contextDTO) {
        Optional<Team> optional = teamRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Team does not exist");
        }
        Team team = optional.get();
        Team teamFind = teamRepository.findByNameAndDeletedIsFalse(dto.getName());
        if (teamFind != null && !team.getName().equals(dto.getName())) {
            throw new BusinessError("team already exists");
        }
        //判断子team是否存在
        List<Team> sonList = teamRepository.findByParentTeamNameAndDeletedIsFalse(team.getName());
        if (sonList.size() > 0 && !team.getName().equals(dto.getName())) {
            throw new BusinessError("This team has subordinate teams ,please delete them first!");
        }
        BeanUtils.copyProperties(dto, team);
        if (dto.getParentTeamName() != null && dto.getParentTeamName().equals(team.getName())) {
            throw new BusinessError("parent team cannot be itself");
        }
        if (team.getParentTeamName() != null && !"".equals(team.getParentTeamName())) {
            Team parent = teamRepository.findByNameAndDeletedIsFalse(team.getParentTeamName());
            team.setParentTeamId(parent.getId());
        }
        team.setLastModifiedAt(new Date());
        team.setLastModifiedBy(contextDTO.getOperator().getId());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id, ContextDTO contextDTO) {
        Optional<Team> optional = teamRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BusinessError("Team does not exist");
        }

        Team team = optional.get();
        //判断子team是否存在
        List<Team> sonList = teamRepository.findByParentTeamNameAndDeletedIsFalse(team.getName());
        if (sonList.size() > 0) {
            throw new BusinessError("This team has subordinate teams ,please delete them first!");
        }

        team.setDeleted(true);
        team.setStatus(EntityStatus.DELETED);
        team.setDeletedAt(new Date());
        team.setDeletedBy(contextDTO.getOperator().getId());
        team.setLastModifiedAt(new Date());
        team.setLastModifiedBy(contextDTO.getOperator().getId());

        teamRepository.save(team);

    }

    @Override
    public List<Company> searchAllCompany() {
        return companyRepository.findByDeletedIsFalse();
    }

    @Override
    public List<Division> searchAllDivision() {
        return divisionRepository.findByDeletedIsFalse();
    }

    @Override
    public List<Department> searchAllDepartment() {
        return departmentRepository.findByDeletedIsFalse();
    }

    @Override
    public List<Team> searchAllTeam() {
        return teamRepository.findByDeletedIsFalse();
    }

}
