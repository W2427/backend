package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.docs.api.FileFeignAPI;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.drawing.DrawingZipHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.SplitPDFHistoryRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.domain.model.service.drawing.SplitPDFHistoryInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaPageDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import com.ose.tasks.entity.drawing.SplitPDFHistory;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.PdfUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 拆分PDF service。
 *
 * @auth DengMing
 * @date 2021/8/3 13:38
 */
@Component
public class SplitPDFHistoryService implements SplitPDFHistoryInterface {

    private final static Logger logger = LoggerFactory.getLogger(SplitPDFHistoryService.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    private final UploadFeignAPI uploadFeignAPI;

    private final FileFeignAPI fileFeignAPI;

    private final BatchTaskInterface batchTaskService;

    private final DrawingZipHistoryRepository drawingZipHistoryRepository;

    private ProjectService projectService;

    private final SplitPDFHistoryRepository splitPDFHistoryRepository;



    @Autowired
    public SplitPDFHistoryService(
        UploadFeignAPI uploadFeignAPI,
        BatchTaskInterface batchTaskService,
        DrawingZipHistoryRepository drawingZipHistoryRepository,
        ProjectService projectService,
        SplitPDFHistoryRepository splitPDFHistoryRepository,
        FileFeignAPI fileFeignAPI
    ) {
        this.uploadFeignAPI = uploadFeignAPI;
        this.fileFeignAPI = fileFeignAPI;
        this.batchTaskService = batchTaskService;
        this.drawingZipHistoryRepository = drawingZipHistoryRepository;
        this.projectService = projectService;
        this.splitPDFHistoryRepository = splitPDFHistoryRepository;
    }

    /**
     * 异步拆分图纸插件
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO  上传文件DTO
     */
    @Override
    public void splitPDF(Long orgId, Long projectId, DrawingUploadDTO uploadDTO, ContextDTO context) {
        Project project = projectService.get(orgId, projectId);

        batchTaskService.runPdfPackage(
            context,
            project,
            BatchTaskCode.DRAWING,
            batchTask -> {

                String realFileName = uploadDTO.getRealFileName();

                // 设置文件所在地址及文件输出地址
                String filePath = temporaryDir + uploadDTO.getFileName();
                String outPath = temporaryDir + CryptoUtils.uniqueId();

                // 将上传后的文件重命名
                FileUtils.rename(new File(filePath), realFileName);

                filePath =  temporaryDir + realFileName;
                // 拆分文件
                List<String> pathList = PdfUtils.partitionPdfFileToPerPage(filePath, outPath);

                // 保存拆分后的所有文件
                pathList.forEach(path -> {

                    SplitPDFHistory splitPDFHistory = new SplitPDFHistory();
                    splitPDFHistory.setFileName(path.split("/")[path.split("/").length - 1]);
                    splitPDFHistory.setDrawingNo(realFileName.split(".pdf")[0]);
                    splitPDFHistory.setOperatorName(context.getOperator().getName());

                    splitPDFHistory.setOrgId(orgId);
                    splitPDFHistory.setProjectId(projectId);
                    splitPDFHistory.setCreatedAt(new Date());
                    splitPDFHistory.setLastModifiedAt(new Date());
                    splitPDFHistory.setStatus(EntityStatus.PENDING);

                    try {
                        // 将拆分后的文件上传到系统中
                        File file = new File(path);
                        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory())
                            .createItem("file", MediaType.APPLICATION_PDF_VALUE, true, file.getName());

                        IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                        MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                            APPLICATION_PDF_VALUE, fileItem.getInputStream());
                        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                            .uploadProjectDocumentFile(orgId.toString(), fileItem1);

                        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                            tempFileResBody.getData().getName(), new FilePostDTO());

                        // 更新图纸拆分历史
                        if (fileESResBody != null) {
                            splitPDFHistory.setFileId(Long.parseLong(fileESResBody.getData().getId()));
                            splitPDFHistory.setFilePath(fileESResBody.getData().getPath());
                            splitPDFHistory.setStatus(EntityStatus.ACTIVE);


                        } else {
                            throw new BusinessError("图纸文件拆分失败");
                        }

                    } catch (Exception e) {
                        splitPDFHistory.setErrorMsg(e.getMessage());
                        splitPDFHistory.setStatus(EntityStatus.CANCEL);
                    } finally {
                        splitPDFHistoryRepository.save(splitPDFHistory);
                    }
                });
                return new BatchResultDTO();
            });

    }

    @Override
    public Page<SplitPDFHistory> search(Long orgId, Long projectId, DrawingCriteriaPageDTO criteriaDTO) {

        return criteriaDTO.getKeyword() != null ?
            splitPDFHistoryRepository.findByOrgIdAndProjectIdAndDrawingNoLikeOrFileNameLike(
            orgId,
            projectId,
            "%" + criteriaDTO.getKeyword() + "%",
            "%" + criteriaDTO.getKeyword() + "%",
            criteriaDTO.toPageable()) :
            splitPDFHistoryRepository.findByOrgIdAndProjectId(
                orgId,
                projectId,
                criteriaDTO.toPageable());
    }

    @Override
    public void splitPdfZip(Long orgId, Long projectId, DrawingCriteriaPageDTO criteriaDTO,ContextDTO context){
        Project project = projectService.get(orgId, projectId);

        criteriaDTO.setFetchAll(true);
        // TODO 创建打包历史
        DrawingZipDetail drawingZipDetail = new DrawingZipDetail();
        drawingZipDetail.setOrgId(orgId);
        drawingZipDetail.setProjectId(projectId);
        drawingZipDetail.setOperateName(context.getOperator().getName());
        drawingZipDetail.setOperateBy(context.getOperator().getId());
        drawingZipDetail.setPackageAt(new Date());
        drawingZipDetail.setCreatedAt(new Date());
        drawingZipDetail.setLastModifiedAt(new Date());
        drawingZipDetail.setStatus(EntityStatus.ACTIVE);
        drawingZipDetail.setPackageStatus(EntityStatus.ACTIVE);
        drawingZipDetail.setDrawingNo(criteriaDTO.getKeyword());

        Long zipHistoryId = drawingZipDetail.getId();
        drawingZipHistoryRepository.save(drawingZipDetail);


        // TODO 开启新线程进行打包
        batchTaskService.runPdfPackage(
            context,
            project,
            BatchTaskCode.DRAWING,
            batchTask -> {
                DrawingZipDetail drawinghistory = drawingZipHistoryRepository.findById(zipHistoryId).get();
                List<SplitPDFHistory> splitPDFHistoryList = search(orgId, projectId, criteriaDTO).getContent();

                if (splitPDFHistoryList == null || splitPDFHistoryList.size() == 0) {
                    return new BatchResultDTO();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String zipFileName = formatter.format(new Date()) + ".zip";
                String zipPath = temporaryDir + zipFileName;

                ZipOutputStream zipOutputStream = null;
                FileInputStream zipSource = null;
                BufferedInputStream bufferStream = null;
                File zipFile = new File(zipPath);


                try {
                    zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
                    for (SplitPDFHistory history : splitPDFHistoryList) {
                        String splitPDFHistoryPath = history.getFilePath();
                        File splitPdfFile = new File(protectedDir, splitPDFHistoryPath);

                        if (splitPdfFile.exists()) {
                            zipSource = new FileInputStream(splitPdfFile);
                            ZipEntry zipEntry = new ZipEntry(history.getFileName());
                            zipOutputStream.putNextEntry(zipEntry);
                            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                            int read = 0;
                            byte[] buf = new byte[1024 * 10];
                            while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                                zipOutputStream.write(buf, 0, read);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    drawinghistory.setErrorMsg(e.getMessage());
                    drawinghistory.setPackageStatus(EntityStatus.CANCEL);
                } finally {

                    try {
                        if (null != bufferStream) {
                            bufferStream.close();
                        }
                        if (null != zipOutputStream) {
                            zipOutputStream.flush();
                            zipOutputStream.close();
                        }
                        if (null != zipSource) {
                            zipSource.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }finally {

                        drawingZipHistoryRepository.save(drawinghistory);
                    }
                }

                try {


                // TODO 将打包完的文件进行上传
                    DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                        MediaType.APPLICATION_PDF_VALUE, true, zipFile.getName());

                    IOUtils.copy(new FileInputStream(zipFile), fileItem.getOutputStream());

                    logger.error("图纸基础5 上传docs服务->开始");
                    MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                        APPLICATION_PDF_VALUE, fileItem.getInputStream());
                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                        .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                    logger.error("图纸基础5 上传docs服务->结束");
                    logger.error("图纸基础5 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        tempFileResBody.getData().getName(), new FilePostDTO());
                    logger.error("图纸基础5 保存docs服务->结束");

                    if (fileESResBody != null) {
                        drawinghistory.setFileName(splitPDFHistoryList.get(0).getDrawingNo());
                        drawinghistory.setFileId(fileESResBody.getData().getId());
                        drawinghistory.setFilePath(fileESResBody.getData().getPath());

                        drawinghistory.setPackageStatus(EntityStatus.FINISHED);
                    }
                } catch (Exception e) {
                    drawinghistory.setErrorMsg(e.getMessage());
                    drawinghistory.setPackageStatus(EntityStatus.CANCEL);
                } finally {
                    drawingZipHistoryRepository.save(drawinghistory);
                }

                return new BatchResultDTO();
            });


    }
}
