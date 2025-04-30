package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.util.*;
import com.ose.auth.api.UserFeignAPI;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class ExInspReportService extends StringRedisService implements ExInspReportInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspReportService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final UserFeignAPI userFeignAPI;

    private final ActivityTaskInterface activityTaskService;

    private final BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspConfirmRepository bpmExInspConfirmRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final BpmActivityInstanceRepository actInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspReportService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                               ActivityTaskInterface activityTaskService, BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository,
                               QCReportRepository qcReportRepository,
                               BpmExInspConfirmRepository bpmExInspConfirmRepository,
                               StringRedisTemplate stringRedisTemplate,
                               BpmExInspScheduleRepository bpmExInspScheduleRepository,
                               ExInspActInstRelationRepository exInspActInstRelationRepository, BpmActivityInstanceRepository actInstRepository, BpmRuTaskRepository ruTaskRepository) {
            super(stringRedisTemplate);
        this.userFeignAPI = userFeignAPI;
        this.activityTaskService = activityTaskService;
        this.bpmExInspUploadHistoryRepository = bpmExInspUploadHistoryRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspConfirmRepository = bpmExInspConfirmRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.actInstRepository = actInstRepository;
        this.ruTaskRepository = ruTaskRepository;
    }


    /**
     * 获取外检报告上传历史记录
     */
    @Override
    public Page<BpmExInspUploadHistory> externalInspectionUploadHistories(Long orgId, Long projectId,
                                                                          ExInspUploadHistorySearchDTO pageDTO, Long operatorId) {
        return bpmExInspUploadHistoryRepository.externalInspectionUploadHistories(orgId, projectId,
            pageDTO, operatorId);
    }


    /**
     * 查询报检记录
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    @Override
    public Page<ExInspReportsDTO> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO,
                                                            ExInspReportCriteriaDTO criteriaDTO) {

        Set<ReportStatus> doneStatus = new HashSet<ReportStatus>(){{
            add(ReportStatus.DONE);
            add(ReportStatus.REHANDLE_DONE);
        }};
        List<ExInspReportsDTO> exInspReportDTOs = new ArrayList<>();

        List<ReportStatus> reportStatuses = new ArrayList<>();
        reportStatuses.add(ReportStatus.UPLOADED);
        reportStatuses.add(ReportStatus.PENDING);
        reportStatuses.add(ReportStatus.REHANDLE_UPLOADED);

        Page<QCReport> qcReports;
        if (criteriaDTO.getKeyword() != null) {
            qcReports = qcReportRepository.findByProjectIdAndReportNoAndReportStatusIn(projectId, criteriaDTO.getKeyword(), reportStatuses, criteriaDTO.getOperator(), pageDTO.toPageable());
        } else {
            qcReports = qcReportRepository.findByProjectIdAndReportStatusIn(projectId, reportStatuses, criteriaDTO.getOperator(), pageDTO.toPageable());
        }

        Map<Long, String> userNameMap = new HashMap<>();
        for (QCReport qcReport : qcReports.getContent()) {

            ExInspReportsDTO exInspReportDTO = new ExInspReportsDTO();

            ActReportDTO actReportDTO = new ActReportDTO();
            actReportDTO.setSeriesNo(qcReport.getSeriesNo());
            actReportDTO.setFileId(qcReport.getUploadFileId());
            actReportDTO.setReportQrCode(qcReport.getQrcode());
            qcReport.setActReport(actReportDTO);

            exInspReportDTO.setQcReport(qcReport);


            BpmExInspSchedule exInspSchedule = bpmExInspScheduleRepository.
                findByIdAndStatus(qcReport.getScheduleId(), EntityStatus.ACTIVE);

            if (exInspSchedule != null && !doneStatus.contains(qcReport.getReportStatus()) && exInspSchedule.getRunningStatus() != null && !exInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                Long scheduleId = exInspSchedule.getId();
                List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
                    orgId, projectId, scheduleId
                );
                if(!CollectionUtils.isEmpty(taskProcIds)) {
                    Long taskId = taskProcIds.get(0).getTaskId();
                    Long actInstId = taskProcIds.get(0).getActInstId();
                    BpmActivityInstanceBase actInst = actInstRepository.findById(actInstId).orElse(null);
                    if(actInst == null) {
                        throw new NotFoundError();
                    }
                    BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
                    if(ruTask == null) {
                        continue;
                    }
                    List<TaskGatewayDTO> gateWayDTOs = activityTaskService.getTaskGateway(projectId, actInst.getProcessId(),
                        actInst.getBpmnVersion(), ruTask.getTaskDefKey());
                    exInspReportDTO.setGateway(gateWayDTOs);
                }
            }


            Long userid = exInspReportDTO.getQcReport().getOperator();
            if (userid != null) {
                if (userNameMap.containsKey(userid)) {
                    exInspReportDTO.getQcReport().setOperatorName(userNameMap.get(userid));
                } else {
                    String userName = getRedisKey(String.format(RedisKey.USER.getDisplayName(),userid), 60*60*8);
                    if(StringUtils.isEmpty(userName)) {
                        userName = userFeignAPI.get(userid).getData().getName();
                        setRedisKey(String.format(RedisKey.USER.getDisplayName(),userid), userName);
                    }


                    userNameMap.put(userid, userName);
                    exInspReportDTO.getQcReport().setOperatorName(userName);
                }
            }

            exInspReportDTOs.add(exInspReportDTO);
        }


        Page<ExInspReportsDTO> page = new PageImpl<>(exInspReportDTOs, qcReports.getPageable(), qcReports.getTotalElements());
        return page;
    }


    @Override
    public Page<BpmExInspConfirm> uploadFileConfirmDetails(Long orgId, Long projectId,
                                                           Long id, PageDTO pageDTO) {
        return bpmExInspConfirmRepository.findByProjectIdAndUploadHistoryIdOrderByCreatedAtAsc(projectId, id, pageDTO.toPageable());
    }


    /**
     * 处理上传的报告，返回二维码 对应 页数编号列表 的map
     *
     * @param pdfPerPages 上传文件的全称列表
     * @return 返回二维码 对应 页数编号列表 的map
     */
    @Override
    public Map<String, List<String>> handleUploadedReport(Long orgId, Long projectId, List<String> pdfPerPages) {

        Map<String, String> perPageQrcodeMap = new HashMap<>();
        for (String pdfPerPage : pdfPerPages) {
            System.out.println("Blur parse");
            try {
                List<String> imageFiles = PdfUtils.parsePdfImage(pdfPerPage, temporaryDir);
                String qrcode = null;

                String imageBlur = "";
                for (int i = 1; i < 3; i++) {
                    for (String imageFile : imageFiles) {
                        imageBlur = temporaryDir + CryptoUtils.uniqueId() + ".png";
                        boolean convertFlag = PdfUtils.convertFileByTerminal(imageFile, imageBlur, " -blur " + i + "x" + i + " -density 200 ");
                        if (convertFlag && qrcode == null) {
                            qrcode = QRCodeUtils.decodeQrcodeFromImage(imageBlur);
                            if (qrcode != null) {
                                QCReport report = qcReportRepository.findByQrcode(qrcode);
                                if (report != null) {
                                    break;
                                } else {
                                    qrcode = null;
                                }
                            }
                        }
                        try {
                            FileUtils.remove(imageBlur);
                        } catch (Exception e) {
                        }
                    }
                    try {
                        FileUtils.remove(imageBlur);
                    } catch (Exception e) {
                    }
                    if (qrcode != null) {
                        break;
                    }
                }

                System.out.println(" parse img");

                for (String imageFile : imageFiles) {
                    if (qrcode == null) {

                        PdfUtils.convertPdfByTerminal(imageFile, imageFile);
                        qrcode = QRCodeUtils.decodeQrcodeFromImage(imageFile);
                        if (qrcode != null) {
                            QCReport report = qcReportRepository.findByQrcode(qrcode);
                            if (report != null) {
                                break;
                            } else {
                                qrcode = null;
                            }
                        }
                    }
                }

                for (String imageFile : imageFiles) {
                    try {
                        FileUtils.remove(imageFile);
                    } catch (Exception e) {
                    }
                }
                perPageQrcodeMap.put(pdfPerPage, qrcode);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        String qrcode = null;
        Map<String, List<String>> qrcodePdfs = new HashMap<>();
        List<String> pdfs = new ArrayList<>();
        for (int i = 0; i < pdfPerPages.size(); i++) {
            String currentPage = pdfPerPages.get(i);
            qrcode = perPageQrcodeMap.get(currentPage);
            pdfs = qrcodePdfs.get(qrcode);
            if (pdfs == null) {
                pdfs = new ArrayList<>();
            }
            if (qrcode != null) {
                pdfs.add(currentPage);
                while (true) {
                    i++;
                    if (i < pdfPerPages.size()) {
                        String nextPage = pdfPerPages.get(i);
                        String qrcodeNext = perPageQrcodeMap.get(nextPage);
                        if (qrcodeNext == null || qrcodeNext.equals(qrcode)) {
                            pdfs.add(nextPage);
                        } else {
                            i--;
                            break;
                        }
                    } else {
                        i--;
                        break;
                    }
                }
                qrcodePdfs.put(qrcode, pdfs);
            } else {
                if (i == 0) {
                    pdfs.add(currentPage);
                    while (true) {
                        i++;
                        if (i < pdfPerPages.size()) {
                            String nextPage = pdfPerPages.get(i);
                            String qrcodeNext = perPageQrcodeMap.get(nextPage);
                            if (qrcodeNext == null) {
                                pdfs.add(nextPage);
                            } else {
                                i--;
                                break;
                            }
                        } else {
                            i--;
                            break;
                        }
                    }
                    qrcodePdfs.put("NOT_IDENTIFIED", pdfs);

                }
            }
        }

        return qrcodePdfs;
    }

}
