package com.ose.tasks.controller.bpm.drawing;

import com.ose.tasks.domain.model.service.drawing.DrawingZipInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "获取图纸打包历史记录接口")
@RestController
@RequestMapping("/orgs/projects/zip")
public class DrawingZipHistoryController {

    private final DrawingZipInterface drawingZipHistoryService;

    /**
     * 构造方法。
     *
     * @param drawingZipHistoryService
     */
    public DrawingZipHistoryController(DrawingZipInterface drawingZipHistoryService) {
        this.drawingZipHistoryService = drawingZipHistoryService;
    }












}
