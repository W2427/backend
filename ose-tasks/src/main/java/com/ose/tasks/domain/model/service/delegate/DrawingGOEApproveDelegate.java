package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.MailUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 检查 节点 代理。
 */
@Component
public class DrawingGOEApproveDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(DrawingGOEApproveDelegate.class);

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final DrawingBaseInterface drawingBaseService;

    private final ServerConfig serverConfig;

    private final GOEMailHistoryRepository goeMailHistoryRepository;

    private final GOEMailConfigRepository goeMailConfigRepository;

    private final BpmHiTaskinstRepository bpmHiTaskinstRepository;

    private final DrawingRepository drawingRepository;
    /**
     * 构造方法。
     */
    @Autowired
    public DrawingGOEApproveDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                     BpmRuTaskRepository ruTaskRepository,
                                     StringRedisTemplate stringRedisTemplate,
                                     BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                     DrawingBaseInterface drawingBaseService,
                                     ServerConfig serverConfig,
                                     GOEMailHistoryRepository goeMailHistoryRepository,
                                     GOEMailConfigRepository goeMailConfigRepository,
                                     BpmHiTaskinstRepository bpmHiTaskinstRepository,
                                     DrawingRepository drawingRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingBaseService = drawingBaseService;
        this.serverConfig = serverConfig;
        this.goeMailHistoryRepository = goeMailHistoryRepository;
        this.goeMailConfigRepository = goeMailConfigRepository;
        this.bpmHiTaskinstRepository = bpmHiTaskinstRepository;
        this.drawingRepository = drawingRepository;
    }

    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) throws AddressException {

        if(execResult.getVariables() == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            if(!execResult.isExecResult()) return execResult;
        }


        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        Drawing dwg = (Drawing) execResult.getVariables().get("drawing");

        if (dwg == null) {
            dwg = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, execResult.getActInst().getEntityId());
        }

        Date sendTime = new Date();


        BpmHiTaskinst bpmHiTaskinst = bpmHiTaskinstRepository.findByTaskId(Long.valueOf(execResult.getRuTask().getId()));
        if (bpmHiTaskinst != null) {
            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null) {
                bpmHiTaskinst.setCode(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).toString());
                bpmHiTaskinstRepository.save(bpmHiTaskinst);
            }
        }

        List<GOEMailConfig> mails = goeMailConfigRepository.findByOrgIdAndProjectIdAndTaskDefKey(orgId, projectId, execResult.getRuTask().getTaskDefKey());

        List<String> mailListFromConfig = mails.stream()
            .map(GOEMailConfig::getToMail)
            .collect(Collectors.toList());

        // todo 查询当前流程已经走过的节点，将相应的处理人名单拿出来
        List<String> emails = bpmHiTaskinstRepository.findEmailByHiAssignee(Long.valueOf(execResult.getRuTask().getActInstId()));

        // 合并两个列表 + 去重 + 拼接为逗号分隔字符串
        String mailPath = Stream.concat(mailListFromConfig.stream(), emails.stream())
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .distinct()
            .collect(Collectors.joining(","));


        if (mailPath.length() > 0) {
            GOEMailHistory mailSendHis = new GOEMailHistory();
            mailSendHis.setCcMail(null);
            mailSendHis.setCreatedAt();

            String main = "";

            if (execResult.getRuTask().getTaskDefKey().equals("usertask-USER-QC-CHECK")) {
                mailSendHis.setSubject(dwg.getDwgNo() + " was reviewed after [QC CHECK] with result " + execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).toString());
                main = dwg.getDwgNo() + ", drawing name is " + dwg.getDocumentTitle() + ", latest version is " + dwg.getLatestRev()
                    + ". This file was just reviewed after task node [QC CHECK] by " + operatorDTO.getName() + " and feedback with a result of " + execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).toString();
            } else {
                mailSendHis.setSubject(dwg.getDwgNo() + " was reviewed by GOE with result " + execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).toString());
                main = dwg.getDwgNo() + ", drawing name is " + dwg.getDocumentTitle() + ", latest version is " + dwg.getLatestRev()
                    + ". This file was just reviewed by " + operatorDTO.getName() + " and feedback with a result of " + execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).toString();
            }

            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getComment() != null && !execResult.getTodoTaskDTO().getComment().equals("")) {
                main = main + ". The remark from " + operatorDTO.getName() + " is (" + execResult.getTodoTaskDTO().getComment() + ").";
            } else {
                main = main + ". The remark from " + operatorDTO.getName() + " is (void).";
            }

            String signature = "\n\n\n<b>OceanSTAR IDE</b>｜Intelligent & Digital Engineering\n" +
                "\n" +
                "OceanSTAR Elite Group of Companies\n" +
                "HQ: 2 Venture Drive, #16-15,08,17,19,23,30,31 Vision Exchange,\n" +
                "Singapore 608526\n" +
                "WEB: www.oceanstar.com.sg\n" +
                "This message contains confidential information and is intended only for the individual(s) addressed in the message. If you are not the named addressee, you should not disseminate, distribute, or copy this email. If you are not the intended recipient, you are notified that disclosing, distributing, or copying this email is strictly prohibited.";

            String mainContent = (main + signature).replace("\n", "<br>");


            mailSendHis.setMainContent(mainContent);

            mailSendHis.setOperator(operatorDTO.getId());
            mailSendHis.setOrgId(orgId);
            mailSendHis.setProjectId(projectId);
            mailSendHis.setSendTime(sendTime);
            mailSendHis.setStatus(EntityStatus.ACTIVE);
            mailSendHis.setToMail(mailPath);
            mailSendHis.setServerUrl(serverConfig.getUrl());

            goeMailHistoryRepository.save(mailSendHis);


            String toAddress = mailSendHis.getToMail().replaceAll(";", ",");
            String[] toAddressarr = toAddress.split(",");
            InternetAddress[] tos = new InternetAddress[toAddressarr.length];
            for (int i = 0; i < toAddressarr.length; i++) {
                logger.info(toAddressarr[i] + " |||| " + toAddress);
                tos[i] = new InternetAddress(toAddressarr[i]);
            }
            String ccAddress = mailSendHis.getToMail().replaceAll(";", ",");
            String[] ccAddressarr = ccAddress.split(",");
            InternetAddress[] ccs = new InternetAddress[ccAddressarr.length];
            for (int i = 0; i < ccAddressarr.length; i++) {
                ccs[i] = new InternetAddress(ccAddressarr[i]);
            }
            List<String> pathLists = new ArrayList<String>();

            String strlog = "call MailUtils.send start " + mailSendHis.getId() + "**";
            System.out.println(strlog);
            logger.info(strlog);

            try {
                MailUtils.send(
                    new InternetAddress(mailFromAddress),
                    tos,
                    ccs,
                    mailSendHis.getSubject(),
                    mailSendHis.getMainContent(),
                    "text/html; charset=utf-8",
                    pathLists
                );
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }





        return execResult;
    }

}
