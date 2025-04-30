package com.ose.tasks.scheduler;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import com.ose.notifications.api.NotificationFeignAPI;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.vo.NotificationType;
import com.ose.report.api.TagWeightStatisticsAPI;
import com.ose.report.api.WeldStatisticsAPI;
import com.ose.tasks.domain.model.repository.OverTimeApplicationRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRecordRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.moduleProcess.ModuleProcessStaticsInterface;
import com.ose.tasks.domain.model.service.plan.PlanExecutionInterface;
import com.ose.tasks.domain.model.service.plan.WBSSearchInterface;
import com.ose.tasks.domain.model.service.scheduled.ScheduledTaskLogInterface;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.scheduler.annotation.CheckRunningStatus;
import com.ose.tasks.scheduler.annotation.UpdateProgress;
import com.ose.tasks.scheduler.base.BaseScheduler;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.util.MailUtils;
import com.ose.vo.EntityStatus;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.repository.holiday.WorkDayRepository;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ose.vo.EntityStatus.ACTIVE;

/**
 * 实体工序（四级计划）自动启动定时任务。
 */
@Component
public class EntityProcessStartScheduler extends BaseScheduler<EntityProcessStartScheduler, WBSEntry> {

    private final static Logger logger = LoggerFactory.getLogger(EntityProcessStartScheduler.class);

    private static final int BATCH_SIZE = 3000;


    private final ProjectInterface projectService;

    private final ModuleProcessStaticsInterface moduleProcessStaticsService;


    private final WBSSearchInterface wbsSearchService;


    private final PlanExecutionInterface planExecutionService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final OperatorDTO operator = new OperatorDTO(null);


    private Map<String, ModuleProcessDefinition> moduleProcessDefinitions = null;


    private Map<String, Long> processes = null;

    private final ServerConfig serverConfig;

    private final TagWeightStatisticsAPI tagWeightStatisticsAPI;

    private final WeldStatisticsAPI weldStatisticsAPI;

    private final UserFeignAPI userFeignAPI;

    private final DrawingRecordRepository drawingRecordRepository;

    private final NotificationFeignAPI notificationFeignAPI;

    private static HolidayDateRepository holidayDateRepository = null;

    private static WorkDayRepository workDayRepository = null;

    private final OverTimeApplicationRepository overTimeApplicationRepository;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    /**
     * 构造方法。
     */
    @Autowired
    public EntityProcessStartScheduler(
        final ScheduledTaskLogInterface scheduledTaskLogService,
        final ProjectInterface projectService,
        final WBSSearchInterface wbsSearchService,
        final PlanExecutionInterface planExecutionService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ModuleProcessStaticsInterface moduleProcessStaticsService,
        ServerConfig serverConfig,
        TagWeightStatisticsAPI tagWeightStatisticsAPI,
        WeldStatisticsAPI weldStatisticsAPI,
        UserFeignAPI userFeignAPI,
        DrawingRecordRepository drawingRecordRepository,
        NotificationFeignAPI notificationFeignAPI,
        HolidayDateRepository holidayDateRepository,
        WorkDayRepository workDayRepository,
        OverTimeApplicationRepository overTimeApplicationRepository
    ) {
        super(
            ScheduledTaskCode.START_ENTITY_PROCESS,
            scheduledTaskLogService,
            EntityProcessStartScheduler.class
        );

        this.projectService = projectService;
        this.wbsSearchService = wbsSearchService;
        this.planExecutionService = planExecutionService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.moduleProcessStaticsService = moduleProcessStaticsService;
        this.serverConfig = serverConfig;
        this.tagWeightStatisticsAPI = tagWeightStatisticsAPI;
        this.weldStatisticsAPI = weldStatisticsAPI;
        this.userFeignAPI = userFeignAPI;
        this.drawingRecordRepository = drawingRecordRepository;
        this.notificationFeignAPI = notificationFeignAPI;
        this.holidayDateRepository = holidayDateRepository;
        this.workDayRepository = workDayRepository;
        this.overTimeApplicationRepository = overTimeApplicationRepository;
    }

    /**
     * 开始定时任务。
     */
    @Override
//    @Scheduled(cron = "0 0 7,13,19 * * ?")
    public void start() {
        projectService.getNotFinishedProjects().forEach(self()::process);
    }

    /**
     * 取得待处理数据的总件数。
     *
     * @param project 项目信息
     * @return 总件数
     */
    public Integer count(final Project project) {
        return wbsSearchService
            .countNotStartedEntityProcesses(project.getId(), timestamp());
    }

    /**
     * 开始定时任务。
     *
     * @param project 项目
     */
    @Override
    @CheckRunningStatus
    public void process(final Project project) {

        final EntityProcessStartScheduler self = self();
        final Long projectId = project.getId();
        final double timestamp = timestamp();

        Long fromEntryId = null;
        int pageNo = 0;
        int pageSize;

        moduleProcessDefinitions = new HashMap<>();
        processes = new HashMap<>();

        while (true) {


            List<WBSEntry> entries = wbsSearchService
                .getNotStartedEntityProcesses(
                    projectId,
                    timestamp,
                    fromEntryId,
                    PageRequest.of(pageNo, BATCH_SIZE)
                );

            pageSize = entries.size();

            if (pageSize == 0) {
                break;
            }


            for (WBSEntry entry : entries) {
                try {
                    self.process(project, entry);
                } catch (BusinessError e) {
                    logger.info("1、错误的计划:" + entry.getName() + e.getMessage());
                } catch (ValidationError e) {
                    logger.info("2、错误的计划:" + entry.getName() + e.getMessage());
                }

            }

            pageNo++;

            fromEntryId = entries.get(entries.size() - 1).getId();
        }

    }

    /**
     * 输入数据处理：尝试启动四级计划。
     *
     * @param project 项目 ID
     * @param item    数据
     */
    @Override
    @UpdateProgress
    public Boolean process(final Project project, final WBSEntry item) {
        ContextDTO contextDTO = new ContextDTO();
        OperatorDTO operatorDTO = new OperatorDTO(1534408999551623695L, "吴庞");
        contextDTO.setOperator(operatorDTO);


        String funcPartStr = item.getFuncPart();
//        if (!FuncPart.isSupportedFuncPart(funcPartStr)) {
//            return false;
//        }
//
        final String funcPart = funcPartStr;

        return planExecutionService.startWBSEntryWhenPredecessorsApproved(contextDTO,
            todoTaskDispatchService,
            operator,
            project.getOrgId(),
            project.getId(),
            moduleProcessDefinitions.computeIfAbsent(
                item.getModuleType(),
                key -> planExecutionService.getModuleProcessDefinition(
                    project.getOrgId(),
                    project.getId(),
                    item.getModuleType(),
                    funcPart
                )
            ),
            processes,
            false,
            null,
            item
        );
    }

    /**
     * 开始定时任务（模块完成进度统计）。
     */
//    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void startModuleProcessStatics() {
        if (serverConfig.getUrl().indexOf("10.0.0.15") > -1) {
            List<Project> projects = projectService.getNotFinishedProjects();
            Long sOrgId = 0L;
            Long sProjectId = 0L;
            Long pOrgId = 0L;
            Long pProjectId = 0L;
            if (projects.size() > 0) {
                for (Project project : projects) {
                    if (project.getName().equals("F253-ST")) {
                        sOrgId = project.getOrgId();
                        sProjectId = project.getId();
                    }
                    if (project.getName().equals("F253-PI")) {
                        pOrgId = project.getOrgId();
                        pProjectId = project.getId();

                    }
                }
                if (sProjectId != 0L) {
                    moduleProcessStaticsService.createStructure(
                        sOrgId,
                        sProjectId
                    );
                    moduleProcessStaticsService.createElectrical(
                        sOrgId,
                        sProjectId
                    );
                }
                if (pProjectId != 0L) {
                    moduleProcessStaticsService.createPiping(
                        pOrgId,
                        pProjectId,
                        sOrgId,
                        sProjectId
                    );
                }

            }
        }

    }


    /**
     * 开始定时任务（Tag重量统计图）。
     */
//    @Scheduled(cron = "0 0 3 * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void startTagWeightStatics() {
        logger.info("Tag重量统计图开始定时任务----------------------------");
        List<Project> projects = projectService.getNotFinishedProjects();
        for (Project project : projects) {
            logger.info("Tag重量统计图开始定时任务，项目号：" + project.getId());
            tagWeightStatisticsAPI.postTagWeightStatistics(project.getOrgId(), project.getId());
        }

    }

    /**
     * 开始定时任务（Tag重量更新，包含WP02、03、04）。
     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void startTag() {
        logger.info("Tag重量更新开始定时任务----------------------------");
        List<Project> projects = projectService.getNotFinishedProjects();
        for (Project project : projects) {
            logger.info("Tag重量更新开始定时任务，项目号：" + project.getId());

        }
    }


    /**
     * 开始定时任务（每周工时统计，定在周一早上八点）。
     */
    @Scheduled(cron = "0 0 8 * * MON")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void startHour() {
        logger.info("工时统计开始定时任务----------------------------");

        // 先将当前数据清空，确保只留下当前周生成的数据
        notificationFeignAPI.deleteAll();

        // 查询当前所有用户
        List<UserProfile> users = userFeignAPI.searchList().getData();

        // 循环用户，在drawing_record表中查找当前周的工时记录
        if (users.size() <= 0) return;

        Calendar calendar = Calendar.getInstance();
        Integer weekly = Integer.valueOf(calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.WEEK_OF_YEAR));
        List<String> bosses = new ArrayList<>();
        bosses.add("NT_101");
        bosses.add("SG_001");
        bosses.add("SG_002");
        bosses.add("super");
        bosses.add("testvp");
        bosses.add("testnew");
        bosses.add("admin");

        for (int i = 0 ; i < users.size() ; i++) {
            if (bosses.contains(users.get(i).getUsername()) || users.get(i).getStatus().equals(EntityStatus.DISABLED)) {
                continue;
            }

            Double workHourSum = drawingRecordRepository.findByEngineerIdAndWeeklyAndDeletedIsFalse(users.get(i).getId(), weekly - 1);
            // 如果当前周的工时填报还未满40小时，则创建提醒信息
            if (workHourSum == null || workHourSum < 40) {
                // 向notification服务添加消息
                NotificationCreateDTO notificationCreateDTO = new NotificationCreateDTO();
                notificationCreateDTO.setContent("staff： " + users.get(i).getUsername() + " fill in less than 40 hours of working hours last week, please fill in!");
                notificationCreateDTO.setCreatorName("admin");
                notificationCreateDTO.setTitle("Working hours not up to standard");
                notificationCreateDTO.setType(NotificationType.WORKING_HOURS);
                notificationCreateDTO.setUserName(users.get(i).getUsername());
                notificationCreateDTO.setName(users.get(i).getName());
                notificationFeignAPI.create(notificationCreateDTO);

            }
        }

    }

    /**
     * 开始定时任务（每周工时统计，定在周一早上九点）。
     */
    @Scheduled(cron = "0 0 9 * * MON")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void hourReminder() throws UnknownHostException {
        logger.info("工时统计开始定时任务----------------------------");

        // 获取本机IP 地址
        InetAddress localHost =InetAddress.getLocalHost();
        String ipAddress = localHost.getHostAddress();
        System.out.println("本机IP 地址:"+ ipAddress);

//         判断 IP 地址是否为 172.31.216.222 或 172.31.216.221
        if (!"172.31.216.222".equals(ipAddress) && !"172.31.216.221".equals(ipAddress)) {
            logger.info("本机IP地址不符合条件，跳过邮件发送");
            return;
        }

        // 查询当前所有用户
        List<UserProfile> users = userFeignAPI.searchList().getData();
//        UserProfile user1 = new UserProfile();
//        user1.setEmail("haiyang.kong@oceanstar.com.sg");
//        user1.setId(1734506884403817731L);
//        user1.setStatus(ACTIVE);
//        user1.setName("haiyang kong 孔海阳");
//        user1.setCompany("OEJS");
//        List<UserProfile> users = new ArrayList<>();
//
//        UserProfile user2 = new UserProfile();
//        user2.setEmail("wangjing@oceanstar.com.sg");
//        user2.setId(1680160184583411901L);
//        user2.setStatus(ACTIVE);
//        user2.setName("Jing Wang 王京");
//
//        users.add(user1);
//        users.add(user2);

        // 循环用户，在drawing_record表中查找当前周的工时记录
        if (users.size() <= 0) return;

        Calendar calendar = Calendar.getInstance();
        Integer weekly = Integer.valueOf(calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.WEEK_OF_YEAR));
        List<String> bosses = new ArrayList<>();
        bosses.add("NT_101");
        bosses.add("SG_001");
        bosses.add("SG_002");
        bosses.add("super");
        bosses.add("testvp");
        bosses.add("testnew");
        bosses.add("admin");

        bosses.add("OSMO_021");
        bosses.add("OSMO_032");
        bosses.add("NT_245");
        bosses.add("NT_257");

        // 获取上一周剔除了节假日和周末的日期列表
        List<String> workDays = getWorkDaysOfLastWeek();

        // 标准工作时长（工作日天数 * 每天8小时）
        double standardWorkHours = workDays.size() * 8;

        for (int i = 0 ; i < users.size() ; i++) {
            UserProfile user = users.get(i);
            if (bosses.contains(users.get(i).getUsername()) || users.get(i).getStatus().equals(EntityStatus.DISABLED)) {
                continue;
            }

            if (!("OEJS".equals(user.getCompany()) || "OESZ".equals(user.getCompany()))) {
                continue; // 如果公司不是 OEJS 或 OESZ，则跳过当前用户
            }

            List<Map<String, Object>> drawingRecord = drawingRecordRepository.findWorkHourDateByEngineerIdAndWeeklyAndDeletedIsFalse(users.get(i).getId(), weekly - 1);

            // 检查用户是否在项目上
            boolean isOnProject = Boolean.TRUE.equals(user.getOnProject());


            // 已填报的工作日
            Set<String> filledDates = drawingRecord.stream()
                .filter(record -> record.get("work_hour") != null && !record.get("work_hour").toString().isEmpty())
                .map(record -> record.get("work_hour_date").toString())
                .collect(Collectors.toSet());

            // 找出未填报的日期
            List<String> missingDates = workDays.stream()
                .filter(date -> !filledDates.contains(date))
                .collect(Collectors.toList());

            // 找出工时不足8小时的日期
            List<String> insufficientDates = drawingRecord.stream()
                .filter(record -> record.get("work_hour") != null && Double.parseDouble(record.get("work_hour").toString()) < 8)
                .map(record -> record.get("work_hour_date").toString())
                .collect(Collectors.toList());

            // 计算总工时
            double totalWorkHours = drawingRecord.stream()
                .filter(record -> record.get("work_hour") != null)
                .mapToDouble(record -> Double.parseDouble(record.get("work_hour").toString()))
                .sum();

            // 如果当前周的工时填报小于当前周的标准工时，则创建提醒信息
            if (totalWorkHours < standardWorkHours) {
                logger.info("==========start send email==========");
                try {
                    String content;
                    if (isOnProject) {
                        // 在项目上的简化邮件内容
                        content = "Dear " + user.getName() + ",\n\n" +
                            "Your working hours for last week are below the standard requirement. Please review and update your timesheet.\n\n" +
                            "Please ensure all working hours are filled in a timely manner.\n\n" +
                            "Best regards,\n" +
                            "OceanSTAR IDE Team";
                    } else {
                        // 原详细邮件内容
                        content = "Dear " + user.getName() + ",\n\n" +
                            "Your working hours for last week are below the standard requirement. Please review and update your timesheet.\n\n" +
                            "Missing dates: " + String.join(", ", missingDates) + "\n" +
                            "Insufficient dates: " + String.join(", ", insufficientDates) + "\n" +
                            "Total working hours filled: " + totalWorkHours + " hours (Standard: " + standardWorkHours + " hours)\n\n" +
                            "Please ensure all working hours are filled in a timely manner.\n\n" +
                            "Best regards,\n" +
                            "OceanSTAR IDE Team";
                    }

                    // 邮件签名
                    String signature = "\n\nOceanSTAR IDE｜Intelligent & Digital Engineering\n" +
                        "\n" +
                        "OceanSTAR Elite Group of Companies\n" +
                        "HQ: 2 Venture Drive, #16-15,08,17,19,23,30,31 Vision Exchange,\n" +
                        "Singapore 608526\n" +
                        "WEB: www.oceanstar.com.sg\n" +
                        "This message contains confidential information and is intended only for the individual(s) addressed in the message. If you are not the named addressee, you should not disseminate, distribute, or copy this email. If you are not the intended recipient, you are notified that disclosing, distributing, or copying this email is strictly prohibited.";

                    MailUtils.sendText(new InternetAddress(mailFromAddress),
                        new InternetAddress(users.get(i).getEmail()),
                        "Working hours not up to standard",
                        content + signature);
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                }
                logger.info("==========end==========");

            }
        }
    }

    /**
     * 开始定时任务（每周工时统计，定在周一早上九点）。
     */
    @Scheduled(cron = "0 0 10 * * MON")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void approveReminder() throws UnknownHostException, ParseException {
        logger.info("工时统计开始定时任务----------------------------");

        // 获取本机IP 地址
        InetAddress localHost =InetAddress.getLocalHost();
        String ipAddress = localHost.getHostAddress();
        System.out.println("本机IP 地址:"+ ipAddress);

//         判断 IP 地址是否为 172.31.216.222 或 172.31.216.221
        if (!"172.31.216.222".equals(ipAddress) && !"172.31.216.221".equals(ipAddress)) {
            logger.info("本机IP地址不符合条件，跳过邮件发送");
            return;
        }

        // 查询未审批的加班申请
        List<Map<String, Object>> overTime = overTimeApplicationRepository.findByApplyStatus();

        // 模拟测试数据
//        List<Map<String, Object>> overTime = new ArrayList<>();
//        Map<String, Object> record1 = new HashMap<>();
//        record1.put("company", "OEJS");
//        record1.put("apply_role_name", "haiyang kong 孔海阳");
//        record1.put("email", "haiyang.kong@oceanstar.com.sg");
//        record1.put("start_date", new SimpleDateFormat("yyyy-MM-dd").parse("2025-03-20")); // 确保日期在上一周范围内
//        record1.put("apply_status", "UNAPPROVED");
//        overTime.add(record1);

        // 获取上一周的时间范围
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 设置为本周一
        calendar.add(Calendar.DATE, -7); // 减去7天，得到上一周的周一
        Date lastWeekStart = calendar.getTime();
        calendar.add(Calendar.DATE, 6); // 加上6天，得到上一周的周日
        Date lastWeekEnd = calendar.getTime();

        // 筛选出公司为 OEJS 和 OESZ 的记录，并且 start_date 在上一周的时间范围内
        List<Map<String, Object>> filteredOverTime = overTime.stream()
            .filter(record -> "OEJS".equals(record.get("company")) || "OESZ".equals(record.get("company")))
            .filter(record -> {
                Date startDate = (Date) record.get("start_date");
                return startDate != null && startDate.after(lastWeekStart) && startDate.before(lastWeekEnd);
            })
            .collect(Collectors.toList());

        // 遍历筛选后的记录，发送邮件提醒审批加班申请
        for (Map<String, Object> record : filteredOverTime) {
            String applyRoleName = (String) record.get("apply_role_name");
            String email = (String) record.get("email");

            // 检查是否需要发送邮件
            if (email == null || email.isEmpty()) {
                logger.warn("Email address is missing for user: " + applyRoleName);
                continue;
            }
            logger.info("==========start send email==========");
            // 构建邮件内容
            String content = "Dear " + applyRoleName + ",\n\n" +
                "You have an overtime application pending approval.\n\n" +
                "Please review and approve the overtime application as soon as possible.\n\n" +
                "Best regards,\n" +
                "OceanSTAR IDE Team";

            // 邮件签名
            String signature = "\n\nOceanSTAR IDE｜Intelligent & Digital Engineering\n" +
                "\n" +
                "OceanSTAR Elite Group of Companies\n" +
                "HQ: 2 Venture Drive, #16-15,08,17,19,23,30,31 Vision Exchange,\n" +
                "Singapore 608526\n" +
                "WEB: www.oceanstar.com.sg\n" +
                "This message contains confidential information and is intended only for the individual(s) addressed in the message. If you are not the named addressee, you should not disseminate, distribute, or copy this email. If you are not the intended recipient, you are notified that disclosing, distributing, or copying this email is strictly prohibited.";

            try {
                logger.info("Sending overtime approval reminder email to: " + email);
                MailUtils.sendText(new InternetAddress(mailFromAddress),
                    new InternetAddress(email),
                    "Overtime Application Approval Required",
                    content + signature);
            } catch (AddressException e) {
                logger.error("Failed to send email to: " + email, e);
            }
            logger.info("==========end==========");
        }
    }

    // 获取指定日期范围内的假期和周末列表
    private static List<String> getHolidayAndWeekendDates(String startDateStr, String endDateStr) {
        List<String> allHolidayDate = holidayDateRepository.findAllHolidayDateByCountry(startDateStr, endDateStr, "China");
        List<String> weekends = findWeekends(startDateStr, endDateStr);
        List<String> workDays = workDayRepository.findAllHolidayDate(startDateStr, endDateStr);
        if (workDays != null && workDays.size() > 0) {
            weekends = weekends.stream()
                .filter(date -> !workDays.contains(date))
                .collect(Collectors.toList());
        }
        return Stream.concat(allHolidayDate.stream(), weekends.stream())
            .distinct()
            .collect(Collectors.toList());
    }

    // 获取周末日期
    private static List<String> findWeekends(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<String> weekends = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends.add(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            currentDate = currentDate.plusDays(1);
        }
        return weekends;
    }

    // 获取上一周剔除了节假日和周末的日期列表
    public static List<String> getWorkDaysOfLastWeek() {
        // 1. 获取上一周的日期范围
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 2. 获取上一周的节假日和周末列表
        List<String> holidaysAndWeekends = getHolidayAndWeekendDates(startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            endOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 3. 生成上一周的日期列表（剔除周末和节假日）
        List<String> workDays = new ArrayList<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            // 剔除周末（周六和周日）
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // 剔除节假日
                if (!holidaysAndWeekends.contains(formattedDate)) {
                    workDays.add(formattedDate);
                }
            }
        }

        return workDays;
    }

}


