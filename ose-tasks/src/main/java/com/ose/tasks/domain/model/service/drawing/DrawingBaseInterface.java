package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TaskPrivilegeDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawing;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * service接口
 */
public interface DrawingBaseInterface {

    /**
     * 获取清单条目详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return drawing
     */
    Drawing getDetailedDrawing(Long orgId, Long projectId, Long id);

    /**
     * 获取清单条目详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return drawing
     */
    Drawing getDetailedDrawingUncheck(Long orgId, Long projectId, Long id);

    /**
     * 获取清单条目详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return drawing
     */
    ExternalDrawing getDetailedExternalDrawing(Long orgId, Long projectId, Long id);

    /**
     * 获取清单条目详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return drawing
     */
    DrawingAmendment getDetailedAmendmentDrawing(Long orgId, Long projectId, Long id);

    /**
     * 获取变量
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸ID
     * @return 子图纸设置 清单
     */
    List<SubDrawingConfig> getVariables(Long orgId, Long projectId, Long id);

    /**
     * 上传图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param userid
     * @param uploadDTO
     * @return
     */
    boolean uploadPdf(Long orgId, Long projectId, Long id, OperatorDTO user, DrawingUploadDTO uploadDTO);

    /**
     * 上传图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param user
     * @param uploadDTO
     * @return
     */
    boolean uploadPdfExternal(Long orgId, Long projectId, Long id, OperatorDTO user, DrawingUploadDTO uploadDTO) throws IOException;

    /**
     * 上传图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param userid
     * @param uploadDTO
     * @return
     */
    boolean uploadPdfAmendment(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException;

    /**
     * 上传图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param userid
     * @param uploadDTO
     * @return
     */
    boolean uploadExternalPdf(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException;

    /**
     * 上传图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param userid
     * @param uploadDTO
     * @return
     */
    boolean uploadAmendmentPdf(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException;

    /**
     * 获取图纸类型列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmEntitySubType> getList(Long orgId, Long projectId);

    /**
     * 获取图纸类型列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmEntitySubType> getExternalList(Long orgId, Long projectId);


    /**
     * 查询上传zip文件历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param page
     * @return
     */
    Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page);

    /**
     * 查询上传zip文件历史记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param page
     * @return
     */
    Page<DrawingUploadZipFileHistory> zipFileHistoryExternal(Long orgId, Long projectId, Long drawingId, PageDTO page);

    /**
     * 查询文件上传zip文件历史记录明细
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param id
     * @param page
     * @return
     */
    Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId, Long drawingId,
                                                                 Long id, PageDTO page);

    /**
     * 查询文件上传zip文件历史记录明细
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param id
     * @param page
     * @return
     */
    Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetailExternal(Long orgId, Long projectId, Long drawingId,
                                                                         Long id, PageDTO page);


    /**
     * 查询qrCode
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrCode
     * @return
     */
    Object checkIssue(Long orgId, Long projectId, String qrCode);

    /**
     * 查询qrCode
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrCode
     * @return
     */
    Object checkIssueExternal(Long orgId, Long projectId, String qrCode);

    boolean checkActivity(Long orgId, Long projectId, Long drawingHisId);

    /**
     * 获取创建图纸工作流程需要的信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    DrawingCreateTaskInfoDTO getCreateTaskInfo(Long orgId, Long projectId, Long id);


    /**
     * 上传子图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param file
     * @param context
     * @return
     */
    UploadDrawingFileResultDTO uploadDrawingSubPipingZip(Long orgId, Long projectId, String file,
                                                         ContextDTO context, DrawingUploadDTO uploadDTO);

    /**
     * 上传子图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param file
     * @param context
     * @return
     */
    UploadDrawingFileResultDTO uploadDrawingSubPipingZipExternal(Long orgId, Long projectId, String file,
                                                                 Long drawingId, Long subDrawingId, ContextDTO context, DrawingUploadDTO uploadDTO) throws IOException;

    /**
     * 上传子图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param file
     * @param context
     * @return
     */
    UploadDrawingFileResultDTO uploadDrawingSubPipingZipAmendment(Long orgId, Long projectId, String file,
                                                                  Long drawingId, Long subDrawingId, ContextDTO context, DrawingUploadDTO uploadDTO) throws IOException;


    /**
     * 查询图纸权限列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    List<TaskPrivilegeDTO> getProcessPrivileges(Long orgId, Long projectId, Long id);

    /**
     * 设置图纸权限
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id
     * @param dto
     * @param operatorId
     * @return
     */
    boolean setProcessPrivileges(Long orgId, Long projectId, Long id, TaskPrivilegeDTO dto, Long operatorId);

    /**
     * 获取全部图纸分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmEntitySubType> getDrawingCategoryList(Long orgId, Long projectId);

    /**
     * 获取全部图纸分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<BpmEntitySubType> getExternalDrawingCategoryList(Long orgId, Long projectId);

    /**
     * check图纸升版版本号
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param version
     * @return
     */
    boolean checkUpVersion(Long orgId, Long projectId, Long id, String version);

    DrawingFileDTO generateDWGFileWithCover(Long orgId, Drawing dwg, Project project,
                                            Long id, boolean printQRCode);

    /**
     * 检查图纸锁定状态
     *
     * @param orgId     组织ID
     * @param id
     * @param projectId 项目ID
     * @return
     */
    boolean checkLock(Long orgId, Long projectId, Long id);


    /**
     * 保存 DrawingList 的xls文件
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */

    File saveDownloadFile(Long orgId, Long projectId, DrawingCriteriaDTO criteriaDTO, Long operatorId);

    List<SubDrawing> findSubByDrawingId(Long id);

    /**
     * 设置图纸文件信息
     *
     * @param drawing
     * @param drawingFileDTO
     * @return
     */
    boolean setDrawingFile(Drawing drawing, DrawingFileDTO drawingFileDTO);


    boolean clearParentQrCode(Long subDrawingId);

    /**
     * 查找图纸最新版本下的所有子图纸。
     *
     * @param drawingId
     * @return
     */
    List<SubDrawing> findLastSubByDrawingId(Long drawingId);

    String generateSignatureCover(
        Long orgId,
        Long projectId,
        Drawing dl,
        String coverPDF
    ) throws IOException;

    /**
     * 查找包含图纸文件的子图纸。
     *
     * @param id
     * @return
     */
    List<SubDrawing> findByDrawingIdAndStatusAndFilePathNotNull(Long id);

    String removeBeforZeroAdd(String str);

    SubDrawing getLastVersionSubDrawing(List<SubDrawing> drawingList, String subNo);

    int convertDrawVersionToOrder(String str);

    Integer handleUploadedZipFiles(String fileFolder, File diskFile, List<File> uploadFiles);

    ExecResultDTO checkRuTask(ExecResultDTO execResult);


    /**
     * 更新或修改图纸文件。
     *
     * @param orgId       组织id
     * @param project     项目
     * @param dwg         图纸信息
     * @param processId   工序信息
     * @param operatorDTO 操作人
     * @param lockFlag    锁定状态
     */



    DrawingPackageReturnDTO packSubFiles(Long orgId, Project project,
                                         Long actInstId,
                                         OperatorDTO operatorDTO, Boolean lockFlag, Drawing dwg,
                                         Long processId, DrawingDetail drawingDetail);


    DrawingPackageReturnDTO packMonoFiles(Long orgId, Project project,
                                          OperatorDTO operatorDTO, Boolean lockFlag, Drawing dwg,
                                          Long processId, Long actInstId, DrawingDetail drawingDetail);

    /**
     * 生成图纸打包文件
     *
     * @param orgId
     * @param project
     * @param dl
     * @param project
     * @param userid
     * @param printCoverQRCode
     * @param printDwgQRCode
     * @return
     */
    DrawingFileDTO generateDrawingReport(
        Long orgId,
        Long actInstId,
        Drawing dl,
        Project project,
        Long userid,
        boolean printCoverQRCode, boolean printDwgQRCode, DrawingDetail drawingDetail);

    /**
     * 取得当前校审的图纸和图纸的最新版本
     *
     * @param execResult
     * @return
     */
    ExecResultDTO getDrawingAndLatestRev(ExecResultDTO execResult);

    /**
     * 打包有效图纸文件。
     *
     * @param orgId       组织id
     * @param project     项目
     * @param dwg         图纸信息
     * @param operatorDTO 操作人
     */
    DrawingPackageReturnDTO packEffectiveSubFiles(
        Long orgId,
        Project project,
        OperatorDTO operatorDTO,
        Drawing dwg);

    /**
     * 打包带有二维码的封面。
     */
    DrawingFileDTO packageCover(
        Long orgId,
        Project project,
        Drawing dwg);

    /**
     * 给所有有效图纸生成二维码并更新子图纸，然后进行有效图纸打包
     *
     * @param orgId       组织ID
     * @param project     项目ID
     * @param operatorDTO
     * @param dl
     */
    DrawingFileDTO startBatchTask(Long orgId,
                                  Project project,
                                  OperatorDTO operatorDTO,
                                  Drawing dl
    );

    /**
     * 预览子图纸（base64）。
     *
     * @param orgId
     * @param projectId
     * @param subDrawingId
     * @return
     */
    ProofreadSubDrawingPreviewDTO getSubDrawingPreview(
        Long orgId,
        Long projectId,
        Long subDrawingId
    );

    /**
     * 子图纸zip打包。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operatorDTO 操作人员
     * @param criteriaDTO 查询条件
     */
    void startZip(Long orgId,
                  Long projectId,
                  Long drawingId,
                  OperatorDTO operatorDTO,
                  SubDrawingCriteriaDTO criteriaDTO,
                  ContextDTO contextDTO
    );

    /**
     * 获取子图纸base64文件。
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param subDrawing 操作人员
     */
    List<String> getSubDrawingImg(Long orgId,
                                  Long projectId,
                                  List<SubDrawing> subDrawing
    );

    /**
     * 获取打包历史记录
     *
     * @param pageDTO
     * @param orgId
     * @param projectId
     * @return
     */
    Page<DrawingZipDetail> search(PageDTO pageDTO, Long orgId, Long projectId);

    void startSubDrawingTask(Long orgId,
                             Project project,
                             OperatorDTO operatorDTO,
                             Drawing dl,
                             SubDrawing subDrawing
    );

    void checkSubDrawing(Long orgId, Long projectId);

    /**
     * 批量 替换 失效的子图纸二维码。
     *
     * @param projectId   项目
     */
    void patchSubDrawingQrCode(
        Long projectId, String once);

    /**
     *  给图纸加条形码
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawing
     * @return
     */
    void generateBarCode(Long orgId, Long projectId, Drawing drawing,DrawingDetail drawingDetail,Long barCodeId) throws IOException;


    /**
     * 上传子图纸文件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param file
     * @param context
     * @return
     */
    UploadDrawingFileResultDTO repairDrawing(
        Long orgId,
        Long projectId,
        String file,
        ContextDTO context,
        DrawingUploadDTO uploadDTO
    ) throws IOException;

}
