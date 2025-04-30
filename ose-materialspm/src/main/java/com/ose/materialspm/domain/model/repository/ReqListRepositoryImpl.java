package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ViewMxjReqs;
import com.ose.repository.BaseRepository;

import org.springframework.data.domain.Page;

/**
 * 请购单列表查询。
 */
public class ReqListRepositoryImpl extends BaseRepository implements ReqListRepositoryCustom {

    /**
     * 查询请购单。
     *
     * @return 请购单查询结果分页数据
     */
    @Override
    public Page<ViewMxjReqs> getList(ReqListCriteriaDTO reqListDTO) {

        SQLQueryBuilder<ViewMxjReqs> sqlQueryBuilder = getSQLQueryBuilder(ViewMxjReqs.class)
            .is("projectId", reqListDTO.getSpmProjId())
            .like("reqCode", reqListDTO.getReqcode())
            .is("dpId", reqListDTO.getDpid())
            .like("buyer", reqListDTO.getBuyer())
            .desc("reqCode");

        sqlQueryBuilder = sqlQueryBuilder
            .paginate(reqListDTO.toPageable())
            .exec();

        return sqlQueryBuilder
            .page();
    }
}
