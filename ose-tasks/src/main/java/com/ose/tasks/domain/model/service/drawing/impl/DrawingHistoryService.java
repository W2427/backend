package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.tasks.domain.model.repository.drawing.DrawingHistoryRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingHistoryInterface;
import com.ose.tasks.entity.drawing.DrawingHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class DrawingHistoryService implements DrawingHistoryInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingHistoryService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    private final DrawingHistoryRepository drawingHistoryRepository;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingHistoryService(
        DrawingHistoryRepository drawingHistoryRepository) {
        this.drawingHistoryRepository = drawingHistoryRepository;
    }


    /**
     * 保存图纸文件历史记录
     */
    @Override
    public DrawingHistory save(DrawingHistory his) {
        return drawingHistoryRepository.save(his);
    }

    /**
     * 根据图纸id获取图纸历史记录最近的一条记录
     */
    @Override
    public DrawingHistory findDrawingPipingHistoryByDrawingId(Long id) {
        List<DrawingHistory> his = drawingHistoryRepository.findByDrawingIdOrderByCreatedAtDesc(id);
        if (!his.isEmpty()) {
            return his.get(0);
        }
        return null;
    }

    /**
     * 根据图纸id获取图纸历史记录
     */
    @Override
    public List<DrawingHistory> getHistory(Long orgId, Long projectId, Long id) {
        return drawingHistoryRepository.findByDrawingIdOrderByCreatedAtDesc(id);
    }



}
