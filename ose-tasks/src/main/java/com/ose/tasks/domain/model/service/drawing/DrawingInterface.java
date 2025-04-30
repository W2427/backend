package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * service接口
 */
public interface DrawingInterface extends BaseWBSEntityInterface<Drawing, WBSEntryCriteriaBaseDTO> {

    /**
     * 创建生产设计图纸清单条目
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param userid     用户ID
     * @param drawingDTO 图纸DTO
     * @return DRAWING
     */
    Drawing create(Long orgId, Long projectId, Long userid, DrawingDTO drawingDTO);

    /**
     * 修改生产设计图纸清单条目
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id         图纸 id
     * @param userid     用户ID
     * @param drawingDTO 图纸DTO
     * @return Drawing
     */
    Drawing modify(Long orgId, Long projectId, Long id, Long userid, DrawingDTO drawingDTO);

    /**
     * 修改生产设计图纸最新版本号
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id         图纸 id
     * @param userid     用户ID
     * @param drawingDTO 图纸DTO
     * @return Drawing
     */
    Drawing modifyVersion(Long orgId, Long projectId, Long id, Long userid, DrawingDTO drawingDTO);

    /**
     * 获取清单列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page        分页
     * @param criteriaDTO 查询DTO
     * @return 分页 DRAWING
     */
    Page<DrawingWorkHourDTO> getList(Long orgId, Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO);

    DrawingFilterDTO getDrawingDisciplines(Long orgId, Long projectId);

    DrawingFilterDTO getDrawingDocTypes(Long orgId, Long projectId);

    /**
     * 获取筛选参数列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @return List
     */
    List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId);

    /**
     * 获取清单列表(包含deleted)
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @return
     */
    List<Drawing> getAllList(Long orgId, Long projectId);


    /**
     * 获取图纸详细信息列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId 图纸ID
     * @return 图纸详情   LIST
     */
    List<DrawingDetail> getList(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO);

    List<DrawingDetail> getListFiles(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO);

    List<DrawingDetail> getAllListFiles(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO);


    /**
     * 删除图纸清单条目
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return boolean
     */
    boolean delete(Long orgId, Long projectId, Long id, DrawingDeleteDTO drawingDeleteDTO);

    /**
     * 删除图纸清单条目
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return boolean
     */
    boolean physicalDelete(Long orgId, Long projectId, Long id);



    /**
     * 根据图号 版本号获取图纸清单条目
     *
     * @param dwgNo     图纸编号
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return DRAWING
     */
    Drawing findByDwgNo(Long orgId, Long projectId, String dwgNo);

    /**
     * 保存图纸条目
     *
     * @param d 图纸
     * @return DRAWING
     */
    Drawing save(Drawing d);

    /**
     * 保存图纸文件历史记录
     *
     * @param his 图纸历史
     * @return 图纸历史
     */
    DrawingHistory save(DrawingHistory his);

    /**
     * 图纸审核
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param id
     * @param operatorDTO
     * @param proofreadDTO
     * @return
     */
    boolean check(ContextDTO contextDTO, Long orgId, Long projectId, Long id, OperatorDTO operatorDTO, DrawingProofreadDTO proofreadDTO);

    /**
     * redmark 图纸审核
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param id
     * @param operatorDTO
     * @param proofreadDTO
     * @return
     */
    boolean redMarkCheck(ContextDTO contextDTO, Long orgId, Long projectId, Long id, OperatorDTO operatorDTO, DrawingProofreadDTO proofreadDTO);

    /**
     * 生成上报图纸文件
     *
     * @param orgId
     * @param projectId
     * @param dl
     * @param project
     * @param userid
     * @param printCoverQRCode
     * @param printDwgQRCode
     * @return
     */
    DrawingFileDTO generateReportPipe(
        Long orgId, Long projectId, Drawing dl, Project project, Long userid,
        boolean printCoverQRCode, boolean printDwgQRCode);



    DrawingFileDTO generateReportPipeSupport(Long orgId, Long projectId, Drawing dl, Project project,
                                             Long userid, boolean printCoverQRCode, boolean printDwgQRCode);


    File saveDownloadFile(Long orgId, Long projectId, DrawingCriteriaDTO criteriaDTO, Long operatorId);

    DrawingDetail getLatestDetail(Long projectId, Long id);

    /**
     * 删除上传的图纸文件，标记删除
     */
    boolean deleteDrawingFile(Long orgId, Long projectId, Long drawingFileId,DrawingDeleteDTO drawingDeleteDTO,Long operatorId);

    DrawingDelegateDTO getDrawingDelegate(Long orgId, Long projectId, Long drawingId);

    /**
     * @param orgId
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getDisciplineTreeData(Long orgId, Long projectId);

    List<Map<String, Object>> getTreeXData(Long orgId, Long projectId);

    List<Map<String, Object>> getTreeYData(Long orgId, Long projectId, String discipline, String level);
}
