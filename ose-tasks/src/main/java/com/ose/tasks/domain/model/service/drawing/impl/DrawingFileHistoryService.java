package com.ose.tasks.domain.model.service.drawing.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingFileHistoryInterface;
import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistoryCreateDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.entity.bpm.BpmActTask;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import com.ose.tasks.entity.wbs.entry.WBSEntityEntryDate;
import com.ose.tasks.util.easyExcel.EntityStatusConverter;
import com.ose.tasks.util.easyExcel.LocalDateConverter;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component

public class DrawingFileHistoryService implements DrawingFileHistoryInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingFileHistoryService.class);

    @Value("${application.files.protected}")
    private String protectedDir;

    private final DrawingFileHistoryRepository drawingFileHistoryRepository;
    private final DrawingFileRepository drawingFileRepository;
    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;
    private final BpmRuTaskRepository bpmRuTaskRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingFileHistoryService(
        DrawingFileHistoryRepository drawingFileHistoryRepository,
        DrawingFileRepository drawingFileRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        BpmRuTaskRepository bpmRuTaskRepository

    ) {
        this.drawingFileHistoryRepository = drawingFileHistoryRepository;
        this.drawingFileRepository = drawingFileRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
    }

    /**
     * 图纸文件历史详情
     */
    @Override
    public DrawingFileHistory detail(
        Long orgId,
        Long projectId,
        Long drawingFileHistoryId
    ) {
        DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, drawingFileHistoryId, EntityStatus.ACTIVE);
        if (null == drawingFileHistory) {
            throw new BusinessError("DrawingFileHistory is not exited");
        }

//        if (null != drawingFileHistory.getAnnotations()) {
//            drawingFileHistory.setAnnotationObjects(JSON.parse(drawingFileHistory.getAnnotations()));
//
//        }
        return drawingFileHistory;
    }

    /**
     * 图纸文件历史列表
     */
    @Override
    public Page<DrawingFileHistory> search(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingFileHistorySearchDTO criteriaDTO
    ) {

//        Page<DrawingFileHistory> drawingFileHistoryPages = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndDrawingDetailIdAndStatusOrderByCreatedAtDesc(
//            orgId,
//            projectId,
//            drawingDetailId,
//            EntityStatus.ACTIVE,
//            pageDTO.toPageable());
        Page<DrawingFileHistory> drawingFileHistoryPages = drawingFileHistoryRepository.search(orgId, projectId, drawingDetailId, criteriaDTO);

        if(!drawingFileHistoryPages.getContent().isEmpty()){
//            for (DrawingFileHistory drawingFileHistory : drawingFileHistoryPages.getContent()) {
//                if (null != drawingFileHistory.getAnnotations()) {
//                    drawingFileHistory.setAnnotationObjects(JSON.parse(drawingFileHistory.getAnnotations()));
//                }
//            }
        }
        return drawingFileHistoryPages;

    }


    public DrawingFileHistory create(
        Long orgId,
        Long projectId,
        Long drawingFileId,
        DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO,
        OperatorDTO operatorDTO
    ) {
        // 查找图纸文件信息
        DrawingFile drawingFile = drawingFileRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, drawingFileId, EntityStatus.ACTIVE);
        if (null == drawingFile) {
            throw new BusinessError("Drawing File Id is invalid.图纸文件ID不存在。");
        }

        // 查找图纸代办任务信息
        if (null == drawingFileHistoryCreateDTO.getTaskId()) {
            throw new BusinessError("task ID should not be null.任务ID不应该为空。");
        }
        BpmRuTask bpmRuTask = bpmRuTaskRepository.findById(drawingFileHistoryCreateDTO.getTaskId()).orElse(null);
        if (null == bpmRuTask) {
            throw new BusinessError("Drawing task running task is invalid.图纸任务流程不存在正在运行的任务。");
        }

        // 查找图纸任务流程信息
        BpmActivityInstanceBase bpmActivityInstanceBase = bpmActivityInstanceRepository.findByOrgIdAndProjectIdAndId(
            orgId,
            projectId,
            bpmRuTask.getActInstId()
        );
        if (null == bpmActivityInstanceBase) {
            throw new BusinessError("Drawing task activity is invalid.图纸任务流程不存在。");
        }

        DrawingFileHistory drawingFileHistory = new DrawingFileHistory();

        BeanUtils.copyProperties(drawingFile, drawingFileHistory, "id");
        drawingFileHistory.setFileId(drawingFileHistoryCreateDTO.getFileId());
        drawingFileHistory.setFileName(drawingFileHistoryCreateDTO.getFileName());
        drawingFileHistory.setFilePath(drawingFileHistoryCreateDTO.getFilePath());

        drawingFileHistory.setOrgId(orgId);
        drawingFileHistory.setProjectId(projectId);
        drawingFileHistory.setDrawingFileId(drawingFileId);
        drawingFileHistory.setDrawingDetailId(drawingFile.getDrawingDetailId());
        drawingFileHistory.setProcInstId(bpmActivityInstanceBase.getId());
        drawingFileHistory.setTaskId(drawingFileHistoryCreateDTO.getTaskId());
        drawingFileHistory.setTaskDefKey(bpmRuTask.getTaskDefKey());
        drawingFileHistory.setTaskName(bpmRuTask.getName());
        drawingFileHistory.setUser(drawingFileHistoryCreateDTO.getUser());
        drawingFileHistory.setRev(drawingFileHistoryCreateDTO.getRev());

        drawingFileHistory.setCreatedAt(new Date());
        drawingFileHistory.setCreatedBy(operatorDTO.getId());
        drawingFileHistory.setLastModifiedAt(new Date());
        drawingFileHistory.setLastModifiedBy(operatorDTO.getId());
        drawingFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingFileHistory.setLatest(true);

        return drawingFileHistoryRepository.save(drawingFileHistory);
    }

    public DrawingFileHistory update(
        Long orgId,
        Long projectId,
        Long drawingFileId,
        Long drawingFileHistoryId,
        DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO,
        OperatorDTO operatorDTO
    ) {
        // 查找图纸文件信息
        DrawingFile drawingFile = drawingFileRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, drawingFileId, EntityStatus.ACTIVE);
        if (null == drawingFile) {
            throw new BusinessError("Drawing File Id is invalid.图纸文件ID不存在。");
        }

        // 查找图纸文件历史信息
        DrawingFileHistory drawingFileHistoryFind = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndIdAndStatus(orgId, projectId, drawingFileHistoryId, EntityStatus.ACTIVE);
        if (null == drawingFileHistoryFind) {
            throw new BusinessError("Drawing File History Id is invalid.图纸文件历史ID不存在。");
        }

        if (null == drawingFileHistoryCreateDTO.getTaskId()) {
            throw new BusinessError("task ID should not be null.任务ID不应该为空。");
        }

        // 查找图纸代办任务信息
        BpmRuTask bpmRuTask = bpmRuTaskRepository.findById(drawingFileHistoryCreateDTO.getTaskId()).orElse(null);
        if (null == bpmRuTask) {
            throw new BusinessError("Drawing task running task is invalid.图纸任务流程不存在正在运行的任务。");
        }

        // 查找图纸任务流程信息
        BpmActivityInstanceBase bpmActivityInstanceBase = bpmActivityInstanceRepository.findByOrgIdAndProjectIdAndId(
            orgId,
            projectId,
            bpmRuTask.getActInstId()
        );
        if (null == bpmActivityInstanceBase) {
            throw new BusinessError("Drawing task activity is invalid.图纸任务流程不存在。");
        }

        drawingFileHistoryFind.setFileId(drawingFileHistoryCreateDTO.getFileId());
        drawingFileHistoryFind.setFileName(drawingFileHistoryCreateDTO.getFileName());
        drawingFileHistoryFind.setFilePath(drawingFileHistoryCreateDTO.getFilePath());
        drawingFileHistoryFind.setUser(drawingFileHistoryCreateDTO.getUser());
        drawingFileHistoryFind.setRev(drawingFileHistoryCreateDTO.getRev());
        drawingFileHistoryFind.setLastModifiedAt(new Date());
        drawingFileHistoryFind.setLastModifiedBy(operatorDTO.getId());
        drawingFileHistoryFind.setStatus(EntityStatus.ACTIVE);
        drawingFileHistoryFind.setLatest(true);

        return drawingFileHistoryRepository.save(drawingFileHistoryFind);
    }

    @Override
    public List<DrawingFileHistory> getDrawingFileHistory(Long orgId, Long projectId, Long drawingFileId, Long procInstId){
        return drawingFileHistoryRepository.findByProjectIdAndDrawingFileIdAndProcInstIdAndStatusAndDeletedIsFalse(
            projectId, drawingFileId, procInstId, EntityStatus.ACTIVE);
    }

    @Override
    public File saveDownloadFile(Long orgId, Long projectId, Long drawingFileHistoryId, Long operatorId) {


        DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            drawingFileHistoryId,
            EntityStatus.ACTIVE
        );
        if (drawingFileHistory != null) {
            File historyFile = new File(protectedDir, drawingFileHistory.getFilePath());
            return historyFile;
        }
        return null;
    }

}
