package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MLocationEntity;
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
public class MLocationRepositoryImpl extends BaseRepository implements MLocationRepositoryCustom {

    @Override
    public List<MLocationEntity> findByProjId(String projId) {

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT L.LOC_ID, L.PROJ_ID, L.LOC_CODE, LN.SHORT_DESC, LN.DESCRIPTION ")
            .append(" FROM M_LOCATIONS L ")
            .append(" INNER JOIN M_LOCATION_NLS LN ")
            .append(" ON L.LOC_ID = LN.LOC_ID ")
            .append(" WHERE L.PROJ_ID = :projId ")
            .append("  ORDER BY L.LOC_CODE ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", projId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        MLocationEntity rs = null;
        List<MLocationEntity> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            rs = new MLocationEntity();
            rs.setLocId(Long.valueOf(m.get("LOC_ID").toString()));
            rs.setProjId(m.get("PROJ_ID").toString());
            rs.setLocCode(m.get("LOC_CODE").toString());
            rs.setShortDesc(m.get("SHORT_DESC").toString());
            rs.setDescription(m.get("DESCRIPTION").toString());

            rsList.add(rs);
        }

        return rsList;
    }
}
