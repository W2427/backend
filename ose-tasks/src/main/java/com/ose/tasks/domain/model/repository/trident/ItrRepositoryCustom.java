package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.dto.completion.ItrStatisticDTO;
import com.ose.tasks.dto.trident.ItrCriteriaDTO;
import com.ose.tasks.entity.trident.Itr;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItrRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId         项目ID
     * @param itrCriteriaDTO 查询条件
     * @return 列表
     */
    Page<Itr> search(Long projectId, ItrCriteriaDTO itrCriteriaDTO);


    List<ItrStatisticDTO> getItrStatistic(Long projectId);

}
