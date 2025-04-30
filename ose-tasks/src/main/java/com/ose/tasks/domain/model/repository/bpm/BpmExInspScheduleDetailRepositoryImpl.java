package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;

/**
 * 根据二维码查询得到外检申请的聚合数据。
 */
public class BpmExInspScheduleDetailRepositoryImpl extends BaseRepository
    implements BpmExInspScheduleDetailRepositoryCustom {
/*
    @Override
    public BpmExInspScheduleDetail findMaxVersionData(Long orgId, Long projectId) {

        SQLQueryBuilder<BpmExInspScheduleDetail> builder = getSQLQueryBuilder(
                BpmExInspScheduleDetail.class).is("orgId", orgId).is("projectId", projectId);

        builder.sort(new Sort.Order(Sort.Direction.DESC, "seriesNo"));

        PageDTO page = new PageDTO();
        page.getPage().setNo(1);
        List<BpmExInspScheduleDetail> details = builder.paginate(page.toPageable()).exec().list();

        if (details != null && details.size() > 0) {
            return details.get(0);
        } else {
            return null;
        }
    }
*/
}
