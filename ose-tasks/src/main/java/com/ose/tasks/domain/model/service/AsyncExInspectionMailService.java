package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.feign.RequestWrapper;
import com.ose.tasks.domain.model.repository.bpm.BpmExInspMailHistoryRepository;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.MailUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import jakarta.mail.internet.InternetAddress;

@Component
public class AsyncExInspectionMailService implements AsyncExInspectionMailInterface {

    private final static Logger logger = LoggerFactory.getLogger(AsyncExInspectionMailService.class);

    private static boolean runningFlg = false;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    @Value("${application.files.protected}")
    private String protectedDir;

    private ServerConfig serverConfig;

    private BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    @Autowired
    public AsyncExInspectionMailService(
        ServerConfig serverConfig,
        BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService
    ) {
        this.serverConfig = serverConfig;
        this.bpmExInspMailHistoryRepository = bpmExInspMailHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    @Override
    public boolean callAsyncExInspectionMailSend(
        final ContextDTO context,
        Long orgId,
        Long projectId
    ) {

        if (runningFlg) {
            return false;
        } else {
            runningFlg = true;
        }
        String authorization = context.getAuthorization();

        if (!context.isContextSet()) {
            String userAgent = context.getUserAgent();
            final RequestAttributes attributes = new ServletRequestAttributes(

                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            context,
            batchTask -> {

                Long mailHisId = null;
                try {
                    while (true) {
                        List<BpmExInspMailHistory> mails = bpmExInspMailHistoryRepository
                            .findByOrgIdAndProjectIdAndSendStatusAndServerUrl(
                                orgId,
                                projectId,
                                MailRunningStatus.INIT,
                                serverConfig.getUrl()
                            );

                        if (mails.isEmpty()) {
                            runningFlg = false;
                            break;
                        }
                        for (BpmExInspMailHistory mailHis : mails) {
                            Set<String> pathList = new TreeSet<>();
                            for (ActReportDTO report : mailHis.getJsonCatalogue()) {

                                String path = report.getFilePath() == null ? null : protectedDir + report.getFilePath().substring(1);
                                if (path != null)
                                    pathList.add(path);
                            }
                            for (ActReportDTO report : mailHis.getJsonReports()) {
                                String path = report.getFilePath() == null ? null : protectedDir + report.getFilePath().substring(1);
                                if (path != null)
                                    pathList.add(path);
                            }
                            for (ActReportDTO report : mailHis.getJsonAttachments()) {
                                String path = report.getFilePath() == null ? null : protectedDir + report.getFilePath().substring(1);
                                if (path != null)
                                    pathList.add(path);
                            }
                            List<String> pathLists = new ArrayList<String>();
                            pathLists.addAll(pathList);

                            String toAddress = mailHis.getToMail().replaceAll(";", ",");
                            String[] toAddressarr = toAddress.split(",");
                            InternetAddress[] tos = new InternetAddress[toAddressarr.length];
                            for (int i = 0; i < toAddressarr.length; i++) {
                                logger.error(toAddressarr[i] + " |||| " + toAddress);
                                tos[i] = new InternetAddress(toAddressarr[i]);
                            }
                            String ccAddress = mailHis.getToMail().replaceAll(";", ",");
                            String[] ccAddressarr = ccAddress.split(",");
                            InternetAddress[] ccs = new InternetAddress[ccAddressarr.length];
                            for (int i = 0; i < ccAddressarr.length; i++) {
                                ccs[i] = new InternetAddress(ccAddressarr[i]);
                            }

                            String strlog = "call MailUtils.send start " + mailHis.getId() + "**";
                            System.out.println(strlog);
                            logger.info(strlog);

                            try {
                                MailUtils.send(
                                    new InternetAddress(mailFromAddress),
                                    tos,
                                    ccs,
                                    mailHis.getSubject(),
                                    mailHis.getMainContent(),
                                    "text/html; charset=utf-8",
                                    pathLists
                                );
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                            }

                            mailHisId = mailHis.getId();
                            mailHis.setSendStatus(MailRunningStatus.SENT);
                            mailHis.setLastModifiedAt();
                            mailHis.setSendTime(new Date());
                            bpmExInspMailHistoryRepository.save(mailHis);

                            strlog = "call MailUtils.send end " + mailHis.getId() + "**";


                            logger.info(strlog);
                        }
                    }

                } catch (Exception e) {
                    runningFlg = false;
                    logger.error("list MailUtils.send error:", e);
                    if (mailHisId != null) {
                        BpmExInspMailHistory mailHis = bpmExInspMailHistoryRepository
                            .findById(mailHisId)
                            .orElse(null);
                        mailHis.setMemo(e.toString());
                        bpmExInspMailHistoryRepository.save(mailHis);
                    }
                }
                return new BatchResultDTO();
            });
        return true;
    }

}
