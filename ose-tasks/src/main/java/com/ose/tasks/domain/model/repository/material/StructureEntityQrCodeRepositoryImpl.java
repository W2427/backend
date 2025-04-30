package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 结构实体二维码查询接口
 */
public class StructureEntityQrCodeRepositoryImpl extends BaseRepository implements StructureEntityQrCodeRepositoryCustom {

    @Override
    public Page<StructureEntityQrCode> search(Long orgId, Long projectId, StructureEntityQrCodeCriteriaDTO criteriaDTO) {

        SQLQueryBuilder<StructureEntityQrCode> builder = getSQLQueryBuilder(StructureEntityQrCode.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        if (criteriaDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("qrCode", operator);
            keywordCriteria.put("entityNo", operator);
            keywordCriteria.put("cuttingNo", operator);
            keywordCriteria.put("programNo", operator);
            keywordCriteria.put("heatNo", operator);
            keywordCriteria.put("ident", operator);
            keywordCriteria.put("tagNumber", operator);
            builder.or(keywordCriteria);
        }

        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        return builder.paginate(criteriaDTO.toPageable())
            .exec()
            .page();

    }

}
