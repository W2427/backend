package com.ose.tasks.dto.drawing;

import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;

import java.util.List;

/**
 * 实体管理 数据传输对象
 */
public class DrawingPackageReturnDTO extends DrawingFileDTO {


    private static final long serialVersionUID = 7964443459314747764L;

    // 图纸详情
    private DrawingDetail drawingDetail;

    // 图纸信息
    private Drawing drawing;

    // 任务文档
    private BpmEntityDocsMaterials bpmDoc;

    private List<DrawingHistory> drawingHisList;

    public DrawingDetail getDrawingDetail() {
        return drawingDetail;
    }

    public void setDrawingDetail(DrawingDetail drawingDetail) {
        this.drawingDetail = drawingDetail;
    }

    public BpmEntityDocsMaterials getBpmDoc() {
        return bpmDoc;
    }

    public void setBpmDoc(BpmEntityDocsMaterials bpmDoc) {
        this.bpmDoc = bpmDoc;
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public List<DrawingHistory> getDrawingHisList() {
        return drawingHisList;
    }

    public void setDrawingHisList(List<DrawingHistory> drawingHisList) {
        this.drawingHisList = drawingHisList;
    }
}
