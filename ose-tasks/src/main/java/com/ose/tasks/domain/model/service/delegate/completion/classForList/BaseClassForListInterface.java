package com.ose.tasks.domain.model.service.delegate.completion.classForList;

import java.util.List;

/**
 * 取得 tagno List 代理接口
 */
public interface BaseClassForListInterface {

    List<List> getItemLists(Long projectId, Long superEntityId);

}
