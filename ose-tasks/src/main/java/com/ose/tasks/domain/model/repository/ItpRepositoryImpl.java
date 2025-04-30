package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.entity.Itp;
import org.springframework.data.domain.Page;

public class ItpRepositoryImpl extends BaseRepository implements ItpRepositoryCustom {

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    @Override
    public Page<Itp> search(Long orgId, Long projectId, ItpCriteriaDTO itpCriteriaDTO) {

        SQLQueryBuilder<Itp> sqlQueryBuilder = getSQLQueryBuilder(Itp.class);

        sqlQueryBuilder.is("orgId", orgId).is("projectId", projectId).is("deleted", false);


        if (itpCriteriaDTO.getProcessId() != null) {
            sqlQueryBuilder.is("processId", itpCriteriaDTO.getProcessId());
        }


        if (itpCriteriaDTO.getProcessStageId() != null) {
            sqlQueryBuilder.is("processStageId", itpCriteriaDTO.getProcessStageId());
        }


        if (itpCriteriaDTO.getEntityType() != null) {
            sqlQueryBuilder.is("webEntityType", itpCriteriaDTO.getEntityType());
        }

        return sqlQueryBuilder
            .paginate(itpCriteriaDTO.toPageable())
            .exec()
            .page();
    }
}
