package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.drawing.DrawingZipHistoryRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingZipInterface;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class DrawingZipHistoryService implements DrawingZipInterface {

    final private DrawingZipHistoryRepository drawingZipHistoryRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingZipHistoryService(DrawingZipHistoryRepository drawingZipHistoryRepository) {
        this.drawingZipHistoryRepository = drawingZipHistoryRepository;
    }


    /**
     * 获取分页打包历史表。
     *
     */
    @Override
    public Page<DrawingZipDetail> search(PageDTO pageDTO){
        return drawingZipHistoryRepository.findAll(pageDTO.toPageable());
    }


}
