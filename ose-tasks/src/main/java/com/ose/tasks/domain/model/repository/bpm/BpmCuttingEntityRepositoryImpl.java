package com.ose.tasks.domain.model.repository.bpm;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.CuttingEntityCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmCutting;
import com.ose.tasks.entity.bpm.BpmCuttingEntity;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务查询。
 */
public class BpmCuttingEntityRepositoryImpl extends BaseRepository implements BpmCuttingEntityRepositoryCustom {

    @Override
    public Page<BpmCuttingEntity> getEntityList(Long orgId, Long projectId, CuttingEntityCriteriaDTO criteriaDTO, Pageable pageable) {

        SQLQueryBuilder<BpmCuttingEntity> builder = getSQLQueryBuilder(BpmCuttingEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE)
            .isNull("cuttingId");

        if (criteriaDTO.getEntityNo() != null
            && !criteriaDTO.getEntityNo().equals("")) {
            builder.like("pipePieceEntityNo", criteriaDTO.getEntityNo());
        }

        if (criteriaDTO.getTaskPackageName() != null
            && !criteriaDTO.getTaskPackageName().equals("")) {
            builder.like("taskPackageName", criteriaDTO.getTaskPackageName());
        }

        if (criteriaDTO.getMatIssueCode() != null) {
            if (!criteriaDTO.getMatIssueCode().equals("")) {
                builder.is("matIssueCode", criteriaDTO.getMatIssueCode());
            } else {
                builder.isNull("matIssueCode");
            }
        }
        if (criteriaDTO.getMatSurplusReceiptsNo() != null) {
            if (!criteriaDTO.getMatSurplusReceiptsNo().equals("")) {
                builder.is("matSurplusReceiptsNo", criteriaDTO.getMatSurplusReceiptsNo());
            } else {
                builder.isNull("matSurplusReceiptsNo");
            }
        }

        if (criteriaDTO.getNested() != null
            && !criteriaDTO.getNested().equals(false)) {
            builder.is("isNested", criteriaDTO.getNested());
        }

        if (criteriaDTO.getMaterialCode() != null
            && !criteriaDTO.getMaterialCode().equals("")) {
            String[] strArr = criteriaDTO.getMaterialCode().split(";");
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            for (int i = 0; i < strArr.length; i++) {
                operator.put(new String("$like"), strArr[i]);
            }
            keywordCriteria.put("materialCode", operator);
            builder.or(keywordCriteria);
        }

        if (criteriaDTO.getNps() != null
            && !criteriaDTO.getNps().isEmpty()) {
            builder.in("nps", criteriaDTO.getNps());
        }

        if (criteriaDTO.getTagNumber() != null) {
            if (!criteriaDTO.getTagNumber().isEmpty()) {
                builder.in("tagNumber", criteriaDTO.getTagNumber());
            } else {
                builder.isNull("tagNumber");
            }
        }

        return builder.paginate(pageable).exec().page();
    }

    @Override
    public Page<BpmCutting> getCuttingList(Long orgId, Long projectId, Pageable pageable, String keyword, List<Long> cuttingIds) {

        SQLQueryBuilder<BpmCutting> builder = getSQLQueryBuilder(BpmCutting.class)
            .is("orgId", orgId)
            .is("projectId", projectId);

        if (cuttingIds != null) {
            if (cuttingIds.isEmpty()) {
                cuttingIds.add(0L);
            }
            builder.in("id", cuttingIds);
        }

        if (keyword != null
            && !keyword.equals("")) {
            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
            Map<String, Object> operator = new IdentityHashMap<>();
            operator.put("$like", keyword);
            keywordCriteria.put(new String("no"), operator);
            keywordCriteria.put(new String("name"), operator);
            builder.orObj(keywordCriteria);
        }

        return builder.paginate(pageable).exec().page();
    }

    @Override
    public List<BpmCuttingEntity> getCuttingEntities(Long orgId, Long projectId, Long cuttingId, String keyword) {
        SQLQueryBuilder<BpmCuttingEntity> builder = getSQLQueryBuilder(BpmCuttingEntity.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("cuttingId", cuttingId)
            .is("status", EntityStatus.ACTIVE);

        if (keyword != null
            && !keyword.equals("")) {
            builder.like("pipePieceEntityNo", keyword);
        }

        builder.sort(new Sort.Order(Sort.Direction.ASC, "cuttingflag"));
        builder.sort(new Sort.Order(Sort.Direction.ASC, "lastModifiedAt"));

        return builder.limit(Integer.MAX_VALUE).exec().list();
    }

}
