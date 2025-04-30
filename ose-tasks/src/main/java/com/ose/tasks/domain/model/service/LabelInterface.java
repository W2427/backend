package com.ose.tasks.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Label;
import org.springframework.data.domain.Page;

public interface LabelInterface {

    /**
     * 删除标签
     *
     * @param id        标签id
     * @return 操作是否成功
     */
    void delete(Long id, OperatorDTO operatorDTO);

    /**
     * 查询标签信息
     *
     * @param id
     * @return
     */
    Label get(Long id);

    /**
     * 查询标签
     *
     * @param page      分页信息
     */
    Page<Label> getList(LabelCriteriaDTO page);

    /**
     * 创建标签
     *
     * @param labelDTO 标签信息
     */
    Label create(OperatorDTO operatorDTO, LabelDTO labelDTO);

    /**
     * 编辑标签
     *
     * @param id             标签id
     * @return 编辑后的标签
     */
    Label modify(OperatorDTO operatorDTO, Long id, LabelDTO labelDTO);
}
