package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.UserBasic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * 用户基本信息 CRUD 操作接口。
 */
public interface UserBasicRepository extends CrudRepository<UserBasic, Long> {

    /**
     * 取得用户信息列表。
     *
     * @param entityIDs 用户数据实体 ID 列表
     * @return 用户信息列表
     */
    List<UserBasic> findByIdIn(Set<Long> entityIDs);

    List<UserBasic> findByIdInAndNameLike(Set<Long> entityIDs, String name);


    @Query(value = "SELECT DISTINCT company FROM users WHERE deleted = 0 AND company IS NOT NULL AND TRIM(company) <> '' ",
        nativeQuery = true)
    List<String> findCompanyList();


    @Query(value = "SELECT DISTINCT division FROM users WHERE deleted = 0 AND division IS NOT NULL AND TRIM(division) <> '' ",
        nativeQuery = true)
    List<String> findDivisionList();

    @Query(value = "SELECT " +
        "DISTINCT division " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND division IS NOT NULL " +
        "AND TRIM( division ) <> '' " +
        "AND company = :company ",
        nativeQuery = true)
    List<String> findDivisionListByCompany(String company);

    @Query(value = "SELECT " +
        " name " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND company = :company " +
        "AND companygm = 1 ",
        nativeQuery = true)
    String findCompanyGM(String company);

    @Query(value = "SELECT " +
        "DISTINCT team " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND team IS NOT NULL " +
        "AND TRIM( team ) <> '' " +
        "AND company = :company " +
        "AND division = :division ",
        nativeQuery = true)
    List<String> findTeamListByCompanyAndDivision(String company, String division);

    @Query(value = "SELECT " +
        "DISTINCT team " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND team IS NOT NULL " +
        "AND TRIM( team ) <> '' " +
        "AND division = :division ",
        nativeQuery = true)
    List<String> findTeamListByDivision(String division);

    @Query(value = "SELECT " +
        " name " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND company = :company " +
        "AND division = :division " +
        "AND divisionvp = 1 ",
        nativeQuery = true)
    String findDivisionVPByCompanyAndDivision(String company, String division);

    @Query(value = "SELECT " +
        " name " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND division = :division " +
        "AND divisionvp = 1 ",
        nativeQuery = true)
    String findDivisionVP(String division);

    @Query(value = "SELECT " +
        " GROUP_CONCAT(name SEPARATOR ', ') " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND company = :company " +
        "AND division = :division " +
        "AND team = :team " +
        "AND team_leader = 1 ",
        nativeQuery = true)
    String findTeamLeader(String company, String division, String team);

    @Query(value = "SELECT " +
        " GROUP_CONCAT(name SEPARATOR ', ') " +
        "FROM " +
        "users " +
        "WHERE " +
        "deleted = 0 " +
        "AND division = :division " +
        "AND team = :team " +
        "AND team_leader = 1 ",
        nativeQuery = true)
    String findTeamLeaderByDivisionAndTeam(String division, String team);

}
