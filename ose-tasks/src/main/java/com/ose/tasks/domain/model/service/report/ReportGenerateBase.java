package com.ose.tasks.domain.model.service.report;


import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.exception.BusinessError;
import com.ose.report.api.ApplicationForInspectionExternalAPI;
import com.ose.report.dto.*;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmExInspScheduleRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskBaseInterface;
import com.ose.tasks.entity.bpm.BpmActivityInstanceReport;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.util.CryptoUtils;
import com.ose.util.PdfUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * reportMetaData
 * date
 * applyDate
 * location
 * printAll 放在 map中
 */
abstract public class ReportGenerateBase implements ReportGenerateInterface {
    private final static Logger logger = LoggerFactory.getLogger(ReportGenerateBase.class);


    @Value("${application.files.temporary}")
    protected String temporaryDir;

    @Value("${application.files.protected}")
    protected String protectedDir;


    protected final String ACC = "ACC";
    protected final String NA = "N/A";
    protected final String REJECT = "REJECT";


    protected Long contractorLogoFileId;


    protected Long clientLogoFileId;

    protected String projectVersion;

    protected String projectName;

    protected String projectCode;

    protected String projectClient;

    private final ProjectInterface projectService;

    private final ApplicationForInspectionExternalAPI applicationForInspectionExternalAPI;

    private final UploadFeignAPI uploadFeignAPI;

    private final QCReportRepository qcReportRepository;

    private final InspectionReportNoInterface inspectionReportNoService;

    private final ExInspTaskBaseInterface externalInspectionTaskBaseService;

    private final BpmExInspScheduleRepository externalInspectionScheduleRepository;


    protected ReportGenerateBase(ProjectInterface projectService,
                                 ApplicationForInspectionExternalAPI applicationForInspectionExternalAPI,
                                 UploadFeignAPI uploadFeignAPI, QCReportRepository qcReportRepository,
                                 InspectionReportNoInterface inspectionReportNoService,
                                 ExInspTaskBaseInterface externalInspectionTaskBaseService,
                                 BpmExInspScheduleRepository externalInspectionScheduleRepository) {
        this.projectService = projectService;
        this.applicationForInspectionExternalAPI = applicationForInspectionExternalAPI;
        this.uploadFeignAPI = uploadFeignAPI;
        this.qcReportRepository = qcReportRepository;
        this.inspectionReportNoService = inspectionReportNoService;
        this.externalInspectionTaskBaseService = externalInspectionTaskBaseService;
        this.externalInspectionScheduleRepository = externalInspectionScheduleRepository;
    }


    protected void initReportParameters(Long orgId, Long projectId) {
        Project project = projectService.get(orgId, projectId);
        this.contractorLogoFileId = project.getContractorLogoFileId();
        this.clientLogoFileId = project.getClientLogoFileId();
        this.projectVersion = project.getVersion().toString();
        this.projectName = project.getName();
        this.projectCode = project.getCode();
        this.projectClient = project.getClient();


    }


    /**
     * 生成报告封面
     *
     * @param orgId
     * @param projectId
     * @param inspectionContents
     * @param listReportDTO
     * @param reportMetaData
     * @param actInstList
     * @param <T                 extends BaseListReportDTO>
     * @return
     */
    @Override
    public <T extends BaseListReportDTO> ReportHistory generateReportCover(Long orgId,
                                                                           Long projectId,
                                                                           String inspectionContents,
                                                                           T listReportDTO,
                                                                           Map<String, Object> reportMetaData,
                                                                           List<BpmActivityInstanceReport> actInstList) {
        initReportParameters(orgId, projectId);

        if (actInstList != null && !actInstList.isEmpty()) {
            String delimit = ", ";
            Set<String> entities = new HashSet<>();

            inspectionContents += "\r\nItems: ";
            for (BpmActivityInstanceReport instance : actInstList) {
                String entityNo = instance.getEntityNo();
                entities.add(entityNo);
                if (entities.contains(entityNo)) continue;
                inspectionContents += entityNo + delimit;
            }


            if (inspectionContents.endsWith(delimit)) {
                inspectionContents = inspectionContents.substring(0, inspectionContents.length() - delimit.length());
            }
        }

        String contractorDir = null;
        String clientDir = null;
        if (contractorLogoFileId != null) {
            contractorDir = protectedDir + projectId + "/" + projectVersion;
        }
        if (clientLogoFileId != null) {
            clientDir = protectedDir + projectId + "/" + projectVersion;
        }

        ApplicationForInspectionExternalDTO applicationForInspectionExternalDTO = new ApplicationForInspectionExternalDTO();
        applicationForInspectionExternalDTO.setProjectNamePrefix(projectName);
        applicationForInspectionExternalDTO.setClientLogoDir(clientDir);
        applicationForInspectionExternalDTO.setContractorLogoDir(contractorDir);
        applicationForInspectionExternalDTO.setExportType(listReportDTO.getExportType());
        applicationForInspectionExternalDTO.setReportQrCode(listReportDTO.getReportQrCode());
        applicationForInspectionExternalDTO.setOrgId(orgId);
        applicationForInspectionExternalDTO.setProjectId(projectId);
        applicationForInspectionExternalDTO.setInspectionDate((Date) reportMetaData.get("date"));
        applicationForInspectionExternalDTO.setApplyDate((Date) reportMetaData.get("applyDate"));
        applicationForInspectionExternalDTO.setInspectionContents(inspectionContents);
        applicationForInspectionExternalDTO.setInspectionLocation((String) reportMetaData.get("location"));
        applicationForInspectionExternalDTO.setReportNo(listReportDTO.getReportNo());
        applicationForInspectionExternalDTO.setReportName(listReportDTO.getReportName());
        applicationForInspectionExternalDTO.setProjectName(projectCode);
        applicationForInspectionExternalDTO.setSerialNo(listReportDTO.getSerialNo());
        applicationForInspectionExternalDTO.setInspectionItem1(projectClient);
        JsonObjectResponseBody<ReportHistory> responseCover = applicationForInspectionExternalAPI
            .generateApplicationForInspectionExternal(orgId, projectId, applicationForInspectionExternalDTO);
        ReportHistory reportHistoryCover = responseCover.getData();
        return reportHistoryCover;
    }

    protected static String[] getPieceNoFromEntityNo(String entityNo) {
        String[] data = new String[2];
        String regEx = "-W([A-Z0-9.]+)$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(entityNo);
        MatchResult ms = null;
        while (matcher.find()) {
            ms = matcher.toMatchResult();







            data[0] = ms.group(1);
            data[1] = entityNo.replace(ms.group(), "");
        }
        return data;
    }

    public FileES mergePDF(Long orgId, Long projectId, ReportHistory reportHistoryCover, ReportHistory reportHistory) {
        try {
            String[] files = new String[2];
            String savepath = temporaryDir + CryptoUtils.uniqueId() + ".pdf";

            files[0] = protectedDir + reportHistoryCover.getFilePath().substring(1);
            files[1] = protectedDir + reportHistory.getFilePath().substring(1);

            PdfUtils.mergePdfFiles(files, savepath);

            File diskFile = new File(savepath);
            System.out.println(diskFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());



            logger.error("报告生成1 上传docs服务->开始");
            MockMultipartFile fileItem1 = null;
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("报告生成1 上传docs服务->结束");
            logger.error("报告生成1 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            logger.error("报告生成1 保存docs服务->结束");
            return fileESResBody.getData();

        } catch (FileNotFoundException e) {

            e.printStackTrace(System.out);
        } catch (IOException e) {

            e.printStackTrace(System.out);
        }
        return null;
    }

    private static String[] getPipelineNoFromSpoolEntityNo(String entityNo) {
        String[] data = new String[2];
        String regEx = "-([0-9.]+)$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(entityNo);
        MatchResult ms = null;
        while (matcher.find()) {
            ms = matcher.toMatchResult();







            data[0] = ms.group(1);
            data[1] = entityNo.replace(ms.group(), "");
        }
        return data;
    }

    protected QCReport generateQCReportWithNewSeriesNo(Long orgId, Long projectId, Long assignee, String assigneeName) {
        int seriesNo = 10000;
        Long maxValue = qcReportRepository.getMaxSeriesNo(projectId);
        if (maxValue != null) {
            seriesNo = maxValue.intValue() + 1;
        }

        QCReport qcReport = new QCReport();
        qcReport.setCreatedAt();
        qcReport.setStatus(EntityStatus.ACTIVE);
        qcReport.setOrgId(orgId);
        qcReport.setProjectId(projectId);
        qcReport.setSeriesNo(String.valueOf(seriesNo));
        qcReport.setOperator(assignee);
        qcReport.setOperatorName(assigneeName);
        return qcReportRepository.save(qcReport);
    }


    @Override
    public <T extends BaseListReportDTO> T generateReportDTO(Long orgId,
                                                             Long projectId,
                                                             Map<String, Object> reportMetaData,
                                                             List<BpmActivityInstanceBase> actInstList) {


        return null;
    }

    @Override
    public <T extends BaseListReportDTO> T generateReportDTO(Long orgId,
                                                             Long projectId,
                                                             Map<String, Object> reportMetaData,
                                                             List<BpmActivityInstanceBase> actInstList,
                                                             boolean inspApplication) {
        InspectionReportPostDTO inspApplicationDTO = new InspectionReportPostDTO();



        String serialNo = (String) reportMetaData.get("serialNo");
        Long scheduleId = (Long) reportMetaData.get("scheduleId");
        String process = (String) reportMetaData.get("process");


        inspApplicationDTO.setSerialNo(serialNo);

        BpmActivityInstanceBase actInst = actInstList.get(0);
        String moduleName = actInst.getEntityModuleName();




        String reportType = null;

        String reportNo = inspectionReportNoService.generateNewReportNo(orgId, projectId, process, reportType, moduleName);
        inspApplicationDTO.setReportName(reportNo);



        String workSiteName = externalInspectionTaskBaseService.getWorkSiteName(projectId, actInstList);

        String inspectionItems = "";
        String location = "";
        Date date = null;
        BpmExInspSchedule schedule = externalInspectionScheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) {
            throw new BusinessError("not found schedule" + scheduleId);
        }
        inspectionItems = schedule.getName();
        location = schedule.getLocation();
        date = schedule.getCreatedAt();
        reportMetaData.put("inspectionItems", inspectionItems);
        reportMetaData.put("location", location);
        reportMetaData.put("date", date);
        reportMetaData.put("comment", schedule.getComment());
        reportMetaData.put("workSiteName", workSiteName);

        inspApplicationDTO.setDate(schedule.getExternalInspectionTime());

        return (T) inspApplicationDTO;
    }


    /**
     * 返回报告的类型
     *
     * @param orgId
     * @param projectId
     * @param actInst
     * @return 报告类型字符串
     */
    @Override
    public String getReportType(Long orgId, Long projectId, BpmActivityInstanceBase actInst) {
        return null;
    }

    /**
     * 返回报告数据内容map。
     *
     * @param orgId
     * @param projectId
     * @param actInstList
     * @return
     */
    @Override
    public List<Map<String, Object>> exportDto(Long orgId,
                                               Long projectId,
                                               List<BpmActivityInstanceState> actInstList) {
        return null;
    }

    /**
     * 返回报告数据字段map。
     *
     * @param listReportDTO
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseReportListItemDTO> Map<String, Object> exportDtoKeys(
        T listReportDTO) {









        Map<String, Object> resultMap = new LinkedHashMap<>();
        List<Field> fieldList = new ArrayList<>();
        Field[] keys = listReportDTO.getClass().getDeclaredFields();
        for (Field f : keys) {
            if (f.getAnnotation(Schema.class) != null) {
                fieldList.add(f);
            }
        }
        fieldList.sort(Comparator.comparingInt(
            m -> m.getAnnotation(Schema.class).minLength()
        ));

        for (Field key : fieldList) {
            Schema annotation = key.getAnnotation(Schema.class);
            if (annotation != null) {
                annotation.name();
                resultMap.put(key.getName(), annotation.name());
            }
        }
        return resultMap;
    }

    /**
     * 返回报告数据字段map。
     *
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseReportListItemDTO> T getReportItemDto() {
        return null;
    }

}
