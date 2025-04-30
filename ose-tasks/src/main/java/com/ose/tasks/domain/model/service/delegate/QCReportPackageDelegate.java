package com.ose.tasks.domain.model.service.delegate;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.BatchTaskDrawingRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingZipHistoryRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 图纸 工序 代理。
 */
@Component
public class QCReportPackageDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BatchTaskDrawingRepository batchTaskDrawingRepository;

    private final QCReportRepository qcReportRepository;

    private final BatchTaskInterface batchTaskService;

    private final ProjectInterface projectService;

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private final UploadFeignAPI uploadFeignAPI;

    private final DrawingZipHistoryRepository drawingZipHistoryRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public QCReportPackageDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                   BpmRuTaskRepository ruTaskRepository,
                                   StringRedisTemplate stringRedisTemplate,
                                   BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                   BatchTaskDrawingRepository batchTaskDrawingRepository,
                                   QCReportRepository qcReportRepository,
                                   BatchTaskInterface batchTaskService,
                                   ProjectInterface projectService,
                                   UploadFeignAPI uploadFeignAPI,
                                   DrawingZipHistoryRepository drawingZipHistoryRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.batchTaskDrawingRepository = batchTaskDrawingRepository;
        this.qcReportRepository = qcReportRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingZipHistoryRepository = drawingZipHistoryRepository;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");

        String parentNo = execResult.getActInst().getEntityNo();
        List<String> processes = new ArrayList<>();
        processes.add("FITUP");
        processes.add("WELD");
        List<QCReport> qcReports = qcReportRepository.findByProjectIdAndProcessInAndReportStatusNotAndEntityNosLike(projectId, processes, ReportStatus.CANCEL, "%" + parentNo + "%");
        if (qcReports.size() > 0) {

            DrawingZipDetail drawingZipDetail = new DrawingZipDetail();
            drawingZipDetail.setOrgId(orgId);
            drawingZipDetail.setProjectId(projectId);
            drawingZipDetail.setOperateName(contextDTO.getOperator().getName());
            drawingZipDetail.setOperateBy(contextDTO.getOperator().getId());
            drawingZipDetail.setPackageAt(new Date());
            drawingZipDetail.setCreatedAt(new Date());
            drawingZipDetail.setLastModifiedAt(new Date());
            drawingZipDetail.setStatus(EntityStatus.ACTIVE);
            drawingZipDetail.setPackageStatus(EntityStatus.ACTIVE);

            Long drawingFileId = drawingZipDetail.getId();

            Project project = projectService.get(orgId, projectId);
            batchTaskService.runDrawingPackage(
                contextDTO,
                project,
                BatchTaskCode.DRAWING,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                batchTask -> {
                    String drawingNo = "drawing.zip";

                    drawingNo = parentNo + ".zip";
                    PageDTO pageDTO = new PageDTO();
                    pageDTO.setFetchAll(true);

                    List<QCReport> subFiles = qcReports;


                    if (subFiles == null || subFiles.size() == 0) {
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
                        for (QCReport subFile : subFiles) {
                            String subDrawingPath = subFile.getPdfReportFilePath();
                            File subDrawingFile = new File(protectedDir, subDrawingPath);

                            if (subDrawingFile.exists()) {
                                zipSource = new FileInputStream(subDrawingFile);
                                ZipEntry zipEntry = new ZipEntry(subFile.getPdfReportFileName());
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
                        }
                    }
                    DrawingZipDetail drawinghistory = drawingZipHistoryRepository.findById(drawingFileId).get();

                    drawinghistory.setDrawingNo(parentNo);
                    drawinghistory.setDrawingVersion("");
                    drawinghistory.setStatus(EntityStatus.ACTIVE);
                    try {
                        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                            MediaType.APPLICATION_PDF_VALUE, true, zipFile.getName());

                        IOUtils.copy(new FileInputStream(zipFile), fileItem.getOutputStream());
                        MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                            APPLICATION_PDF_VALUE, fileItem.getInputStream());

                        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                            .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                            tempFileResBody.getData().getName(), new FilePostDTO());

                        if (fileESResBody != null) {
                            drawinghistory.setFileName(drawingNo);
                            drawinghistory.setFileId(fileESResBody.getData().getId());
                            drawinghistory.setFilePath(fileESResBody.getData().getPath());

                            drawinghistory.setPackageStatus(EntityStatus.FINISHED);

                        }
                    } catch (Exception e) {
                        drawinghistory.setErrorMsg(e.getMessage());
                    } finally {
                        drawingZipHistoryRepository.save(drawinghistory);
                    }
                    return new BatchResultDTO();
                });
        }
        return execResult;
    }

}
