package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.feign.RequestWrapper;
import com.ose.tasks.domain.model.repository.bpm.GOEMailHistoryRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.GOEMailHistory;
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

import jakarta.mail.internet.InternetAddress;
import java.util.*;

@Component
public class AsyncGOEMailService implements AsyncGOEMailInterface {

    private final static Logger logger = LoggerFactory.getLogger(AsyncGOEMailService.class);

    private static boolean runningFlg = false;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    @Value("${application.files.protected}")
    private String protectedDir;

    private ServerConfig serverConfig;

    private GOEMailHistoryRepository goeMailHistoryRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    @Autowired
    public AsyncGOEMailService(
        ServerConfig serverConfig,
        GOEMailHistoryRepository goeMailHistoryRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService
    ) {
        this.serverConfig = serverConfig;
        this.goeMailHistoryRepository = goeMailHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    @Override
    public boolean callAsyncGOEMailSend(
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
            BatchTaskCode.GOE_APPROVE,
            false,
            context,
            batchTask -> {

                Long mailHisId = null;
                try {
                    while (true) {
                        List<GOEMailHistory> mails = goeMailHistoryRepository
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
                        for (GOEMailHistory mailHis : mails) {
                            Set<String> pathList = new TreeSet<>();

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
                            goeMailHistoryRepository.save(mailHis);

                            strlog = "call MailUtils.send end " + mailHis.getId() + "**";


                            logger.info(strlog);
                        }
                    }

                } catch (Exception e) {
                    runningFlg = false;
                    logger.error("list MailUtils.send error:", e);
                    if (mailHisId != null) {
                        GOEMailHistory mailHis = goeMailHistoryRepository
                            .findById(mailHisId)
                            .orElse(null);
                        mailHis.setMemo(e.toString());
                        goeMailHistoryRepository.save(mailHis);
                    }
                }
                return new BatchResultDTO();
            });
        return true;
    }

}
