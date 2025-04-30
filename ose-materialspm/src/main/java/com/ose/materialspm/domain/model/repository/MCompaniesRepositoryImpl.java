package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MCompaniesEntity;
import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询接口。
 */
public class MCompaniesRepositoryImpl extends BaseRepository implements MCompaniesRepositoryCustom {

    @Override
    public List<MCompaniesEntity> findByProjId(String projId) {

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT C.COMPANY_ID, C.COMPANY_CODE, C.COMPANY_NAME ")
            .append("  FROM M_COMPANIES C ")
            .append("  INNER JOIN M_PROJ_SUBCONTRS PS ")
            .append("  ON PS.COMPANY_ID = C.COMPANY_ID ")
            .append("  WHERE PS.PROJ_ID = :projId ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", projId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        MCompaniesEntity mCompaniesEntity = null;
        List<MCompaniesEntity> mCompaniesEntities = new ArrayList<>();
        for (Map<String, Object> m : list) {
            mCompaniesEntity = new MCompaniesEntity();
            mCompaniesEntity.setCompanyId(String.valueOf(m.get("COMPANY_ID")) );
            mCompaniesEntity.setCompanyCode(m.get("COMPANY_CODE").toString());
            mCompaniesEntity.setCompanyName(m.get("COMPANY_NAME").toString());

            mCompaniesEntities.add(mCompaniesEntity);
        }

        return mCompaniesEntities;
    }
}
