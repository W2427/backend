package com.ose.material.domain.model.repository;
import com.ose.material.dto.MmMaterialCertificateSearchDTO;
import com.ose.material.entity.MmMaterialCertificate;
import com.ose.repository.BaseRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.IdentityHashMap;
import java.util.Map;

@Transactional
public class MmMaterialCertificateRepositoryImpl extends BaseRepository implements MmMaterialCertificateRepositoryCustom {

    @Override
    public Page<MmMaterialCertificate> search(
        Long orgId,
        Long projectId,
        MmMaterialCertificateSearchDTO searchBaseDTO
    ) {
        SQLQueryBuilder<MmMaterialCertificate> builder = getSQLQueryBuilder(MmMaterialCertificate.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);
        //判断是否有搜索关键字
        if (searchBaseDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", searchBaseDTO.getKeyword());
            //匹配多个字段

            //证书编号
            keywordCriteria.put("no", operator);
            //证书文件名
            keywordCriteria.put("fileName", operator);

            builder.or(keywordCriteria);
        }
        return builder.paginate(searchBaseDTO.toPageable())
            .exec()
            .page();
    }
}
