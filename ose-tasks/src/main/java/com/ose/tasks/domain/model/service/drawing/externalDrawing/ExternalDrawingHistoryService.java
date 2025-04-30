package com.ose.tasks.domain.model.service.drawing.externalDrawing;

import com.ose.tasks.domain.model.repository.drawing.externalDrawing.ExternalDrawingHistoryRepository;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawingHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class ExternalDrawingHistoryService implements ExternalDrawingHistoryInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExternalDrawingHistoryService.class);

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 受保护文件路径
    @Value("${application.files.protected}")
    private String protectedDir;

    // 公开文件路径
    @Value("${application.files.public}")
    private String publicDir;

    private final ExternalDrawingHistoryRepository externaldrawingHistoryRepository;


    /**
     * 构造方法
     */
    @Autowired
    public ExternalDrawingHistoryService(
        ExternalDrawingHistoryRepository externaldrawingHistoryRepository) {
        this.externaldrawingHistoryRepository = externaldrawingHistoryRepository;
    }


    /**
     * 保存图纸文件历史记录
     */
    @Override
    public ExternalDrawingHistory save(ExternalDrawingHistory his) {
        return externaldrawingHistoryRepository.save(his);
    }

    /**
     * 根据图纸id获取图纸历史记录最近的一条记录
     */
    @Override
    public ExternalDrawingHistory findDrawingPipingHistoryByDrawingId(Long id) {
        List<ExternalDrawingHistory> his = externaldrawingHistoryRepository.findByDrawingIdOrderByCreatedAtDesc(id);
        if (!his.isEmpty()) {
            return his.get(0);
        }
        return null;
    }

    /**
     * 根据图纸id获取图纸历史记录
     */
    @Override
    public List<ExternalDrawingHistory> getHistory(Long orgId, Long projectId, Long id) {
        return externaldrawingHistoryRepository.findByDrawingIdOrderByCreatedAtDesc(id);
    }



}
