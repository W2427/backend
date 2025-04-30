package com.ose.tasks.domain.model.repository.wps;

import com.ose.tasks.entity.wps.Wps;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WpsRepository extends PagingAndSortingRepository<Wps, Long>, WpsRepositoryCustom {

    /**
     * 获取wps详情。
     *
     * @param wpsId 焊接工艺规程ID
     * @return wps详情
     */
    Wps findByIdAndDeletedIsFalse(Long wpsId);

    /**
     * 通过ID获取wps信息。
     *
     * @param wpsIds    焊接工艺规程ID
     * @param orgId     项目ID
     * @param projectId 项目ID
     * @return wps详情
     */
    List<Wps> findByIdInAndOrgIdAndProjectIdAndDeletedIsFalse(List<Long> wpsIds, Long orgId, Long projectId);

    /**
     * 通过WpsNo获取wps信息。
     *
     * @param codeList  焊接工艺规程ID
     * @param orgId     项目ID
     * @param projectId 项目ID
     * @return wps详情
     */
    List<Wps> findByCodeInAndOrgIdAndProjectIdAndDeletedIsFalse(List<String> codeList, Long orgId, Long projectId);

    /**
     * 获取wps列表。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param minDiaRange  最小直径
     * @param maxDiaRange  最大直径
     * @param minThickness 最小厚度
     * @param maxThickness 最大厚度
     * @return wps列表
     */
    @Query(""
        + "SELECT wps "
        + "FROM Wps wps "
        + "LEFT JOIN WpsDetail wpsDetail "
        + "ON "
        + "(wps.id = wpsDetail.wpsId "
        + "AND ("
        + "(wpsDetail.minDiaRange <= ?3 AND wpsDetail.maxDiaRange >= ?3) "
        + "OR "
        + "(wpsDetail.minDiaRange <= ?4 AND wpsDetail.maxDiaRange >= ?4) "
        + "OR "
        + "(wpsDetail.minDiaRange >= ?3 AND wpsDetail.maxDiaRange <= ?4) "
        + ") "
        + "AND ("
        + "(wpsDetail.minThickness <= ?5 AND wpsDetail.maxThickness >= ?5) "
        + "OR "
        + "(wpsDetail.minThickness <= ?6 AND wpsDetail.maxThickness >= ?6) "
        + "OR "
        + "(wpsDetail.minThickness >= ?5 AND wpsDetail.maxThickness <= ?6) "
        + ") "
        + "AND wpsDetail.deleted = FALSE) "
        + "WHERE "
        + "wps.orgId = ?1 "
        + "AND wps.projectId = ?2 "
        + "AND wps.deleted = FALSE "
    )
    List<Wps> search(
        Long orgId,
        Long projectId,
        double minDiaRange,
        double maxDiaRange,
        double minThickness,
        double maxThickness
    );

    /**
     * 获取wps列表。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param keyword      关键字
     * @param minDiaRange  最小直径
     * @param maxDiaRange  最大直径
     * @param minThickness 最小厚度
     * @param maxThickness 最大厚度
     * @return wps列表
     */
    @Query(""
        + "SELECT wps "
        + "FROM Wps wps "
        + "LEFT JOIN WpsDetail wpsDetail "
        + "ON "
        + "(wps.id = wpsDetail.wpsId "
        + "AND ("
        + "(wpsDetail.minDiaRange <= ?3 AND wpsDetail.maxDiaRange >= ?3) "
        + "OR "
        + "(wpsDetail.minDiaRange <= ?4 AND wpsDetail.maxDiaRange >= ?4) "
        + "OR "
        + "(wpsDetail.minDiaRange >= ?3 AND wpsDetail.maxDiaRange <= ?4) "
        + ") "
        + "AND ("
        + "(wpsDetail.minThickness <= ?5 AND wpsDetail.maxThickness >= ?5) "
        + "OR "
        + "(wpsDetail.minThickness <= ?6 AND wpsDetail.maxThickness >= ?6) "
        + "OR "
        + "(wpsDetail.minThickness >= ?5 AND wpsDetail.maxThickness <= ?6) "
        + ") "
        + "AND wpsDetail.deleted = FALSE) "
        + "WHERE "
        + "wps.orgId = ?1 "
        + "AND wps.projectId = ?2 "
        + "AND wps.deleted = FALSE "
        + "AND wps.code LIKE %?7% "
    )
    List<Wps> search(
        Long orgId,
        Long projectId,
        double minDiaRange,
        double maxDiaRange,
        double minThickness,
        double maxThickness,
        String keyword
    );

    List<Wps> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);


    Wps findByOrgIdAndProjectIdAndCodeAndDeletedIsFalse(Long orgId, Long projectId, String wpsNo);

}
