package com.ose.tasks.domain.model.repository.bpm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.ose.repository.BaseRepository;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.vo.bpm.ActInstDocType;

/**
 * 根据二维码查询得到外检申请的聚合数据。
 */
public class BpmEntityDocsMaterialsRepositoryImpl extends BaseRepository
    implements BpmEntityDocsMaterialsRepositoryCustom {

    @Override
    public List<BpmEntityDocsMaterials> searchDocsByProcessIdInAndEntityId(List<BpmProcess> processes,
                                                                           Long entityId) {

        List<Long> processIds = new ArrayList<>();
        for (BpmProcess process : processes) {
            processIds.add(process.getId());
        }

        SQLQueryBuilder<BpmEntityDocsMaterials> builder = getSQLQueryBuilder(BpmEntityDocsMaterials.class)
            .in("processId", processIds)
            .is("entityId", entityId)
            .is("type", ActInstDocType.EXTERNAL_INSPECTION);

        builder.sort(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        return builder.limit(Integer.MAX_VALUE)
            .exec().list();
    }


}
