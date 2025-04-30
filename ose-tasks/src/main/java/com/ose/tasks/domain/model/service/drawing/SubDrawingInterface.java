package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.QRCodeSearchResultDrawingDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingConfig;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;

/**
 * service接口
 */
public interface SubDrawingInterface extends EntityInterface {

    /**
     * 根据图纸清单id及子图纸图号查询子图纸信息
     *
     * @param drawingId
     * @param subNo
     * @return
     */
    SubDrawing findByDrawingIdAndSubNo(Long drawingId, String subNo);

    /**
     * 创建子图纸条目
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param userid
     * @param dto
     * @return
     */
    SubDrawing create(Long orgId, Long projectId, Long drawingId, Long userid, SubDrawingDTO dto);

    /**
     * 保存子图纸条目
     *
     * @param subDrawing
     * @return
     */
    SubDrawing save(SubDrawing subDrawing);

    /**
     * 保存子图纸文件历史记录
     *
     * @param his
     * @return
     */
    SubDrawingHistory save(SubDrawingHistory his);

    /**
     * 修改ISO图纸信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param userid
     * @param dto
     * @return
     */
    SubDrawing modify(Long orgId, Long projectId,Long drawingId, Long id, Long userid, SubDrawingDTO dto);

    /**
     * 通过subDrawingId查询最近的历史
     *
     * @param id
     * @return
     */
    SubDrawingHistory findDrawingSubPipingHistoryBySubDrawingId(Long id);

    /**
     * 图纸出图上传pdf文件（单独上传pdf、批量上传pdf）。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件呢
     * @param drawingId    图纸id
     * @param subDrawingId 子图纸id
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    boolean uploadDrawingSubPipingPdf(Long orgId, Long projectId, String file, Long drawingId, Long subDrawingId, ContextDTO context, DrawingUploadDTO uploadDTO, boolean zip);

    /**
     * 上传升版pdf文件（批量上传、单独上传）。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subDrawingId 子图纸id
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    boolean uploadUpdateDrawingSubPipingPdf(Long orgId, Long projectId, String file, Long drawingId, Long subDrawingId, ContextDTO context, DrawingUploadDTO uploadDTO, boolean zip);

    UploadDrawingFileResultDTO uploadDrawingSubPipingZip(Long orgId, Long projectId, String file, Long drawingId, Long subDrawingId, ContextDTO context, DrawingUploadDTO uploadDTO);

    /**
     * 查询子图纸清单
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param drawingId
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<SubDrawing> getList(Long orgId, Long projectId, Long drawingId, PageDTO page,
                             SubDrawingCriteriaDTO criteriaDTO);

    /**
     * 查询所有子图纸清单
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<SubDrawing> getSubList(Long orgId, Long projectId, PageDTO page,
                             SubDrawingCriteriaDTO criteriaDTO);

    /**
     * 合并下载ISO中的子图纸。
     *
     * @param orgId
     * @param projectId
     * @param page
     * @param criteriaDTO
     * @return
     */
    void downSubList(
        Long orgId,
        Long projectId,
        Long drawingId,
        PageDTO page,
        SubDrawingCriteriaDTO criteriaDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    );

    /**
     * 获取ISO图纸清单详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    SubDrawing get(Long orgId, Long projectId, Long id);

    /**
     * 删除ISO图纸条目
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    boolean delete(Long orgId, Long projectId, Long id);

    /**
     * 获取子图纸变量列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @return
     */
    List<SubDrawingConfig> getVariables(Long orgId, Long projectId, Long drawingId);

    /**
     * 获取子图纸历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    List<SubDrawingHistory> getHistory(Long orgId, Long projectId, Long id);



    /**
     * 根据子图纸id获取子图纸信息
     *
     * @param subId
     * @return
     */
    SubDrawing findDrawingSubPipingBySubId(Long subId);

    /**
     * 根据历史记录id获取历史详情
     *
     * @param id
     * @return
     */
    SubDrawingHistory getHistoryById(Long id);

    /**
     * 修改其他历史记录used
     *
     * @param id
     * @param subDrawingId
     * @return
     */
    boolean updateHistoryUsedFalseExcept(Long id, Long subDrawingId);

    /**
     * 上传子图纸目录
     *
     * @param uploadDTO
     * @param drawingId
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param userid
     * @return
     */
    UploadDrawingFileResultDTO uploadDrawingSubPipingCatalog(Long orgId, Long projectId, Long drawingId, Long userid, DrawingUploadDTO uploadDTO);

    /**
     * 校验是否已经启动工作流
     *
     * @param orgId        组织ID
     * @param subDrawingId
     * @param projectId    项目ID
     * @return
     */
    boolean checkActivity(Long orgId, Long projectId, Long subDrawingId);

    Long writeExcel(Long orgId, Long projectId);

    List<SubDrawing> findBySubDrawingNo(Long orgId, Long projectId, String subDrawingNo);


    void checkSubDrawingNo(Long orgId, Long projectId, String subDrawingNo, Integer pageNo, Long drawingDetailId, Long drawingId);

    /**
     * 获取子图纸文件id
     *
     * @param id
     * @return
     */
    DrawingFileDTO downloadSubDrawing(Long id);

    ActReportDTO setQrCodeIntoPdfFile(Long orgId, Long projectId, String qrCode, String filePath, String fileName,
                                      int i, int j, int k) throws Exception;

    QRCodeSearchResultDrawingDTO getDrawingByQrcode(Long orgId, Long projectId, String qrcode);

    /**
     * 导出图纸清单。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param subDrawingDownLoadDTO
     * @param operatorDTO
     * @param project
     * @return
     */

    File exportSubDrawing(
        Long orgId,
        Long projectId,
        Long drawingId,
        SubDrawingDownLoadDTO subDrawingDownLoadDTO,
        OperatorDTO operatorDTO,
        Project project);

    /**
     * 上传替换最新有效子图纸。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
     boolean uploadActivePDF(
        Long orgId,
        Long projectId,
        String file,
        Long drawingId,
        Long subdrawingId,
        ContextDTO context,
        DrawingUploadDTO uploadDTO,
        boolean zip);

    /**
     * 上传替换最新有效子图纸。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    boolean uploadSecondActivePDF(
        Long orgId,
        Long projectId,
        String file,
        Long drawingId,
        Long subdrawingId,
        ContextDTO context,
        DrawingUploadDTO uploadDTO,
        boolean zip);
}
