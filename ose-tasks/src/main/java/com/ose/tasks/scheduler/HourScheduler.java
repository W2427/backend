package com.ose.tasks.scheduler;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingWorkHourInterface;
import com.ose.tasks.dto.DrawingRecordCriteriaDTO;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.entity.holiday.HolidayData;
import com.ose.util.DateUtils;
import com.ose.util.DingTalkUtil;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.ose.tasks.domain.model.service.drawing.impl.DrawingWorkHourService.*;
import static com.ose.util.DingTalkUtil.*;

@Component
public class HourScheduler {

    private final static Logger logger = LoggerFactory.getLogger(HourScheduler.class);

    private final HolidayDateRepository holidayDateRepository;

    private final UserFeignAPI userFeignAPI;

    private final DrawingWorkHourInterface drawingWorkHourService;

    private final DingTalkWorkHourRepository dingTalkWorkHourRepository;

    private final DingTalkLeaveDataRepository dingTalkLeaveDataRepository;

    private final DingTalkEmployeeDataRepository dingTalkEmployeeDataRepository;

    private final EmployeeDataRepository employeeDataRepository;

    private final EmployeeDataTestRepository employeeDataTestRepository;

    // 新增特殊员工列表
    private static final Map<String, String> specialEmployees = new HashMap<>();
    static {
        specialEmployees.put("NT_205", "2025-06-30");
        specialEmployees.put("NT_244", "2025-06-30");
        specialEmployees.put("NT_281", "2025-08-31");
        specialEmployees.put("NT_291", "2025-08-31");
        specialEmployees.put("NT_212", "2025-08-31");
        specialEmployees.put("NT_240", "2025-12-31");
        specialEmployees.put("TJ_087", "2025-08-31");
    }

    @Autowired
    public HourScheduler(
        HolidayDateRepository holidayDateRepository,
        UserFeignAPI userFeignAPI,
        DrawingWorkHourInterface drawingWorkHourService,
        DingTalkWorkHourRepository dingTalkWorkHourRepository,
        DingTalkLeaveDataRepository dingTalkLeaveDataRepository,
        DingTalkEmployeeDataRepository dingTalkEmployeeDataRepository,
        EmployeeDataRepository employeeDataRepository,
        EmployeeDataTestRepository employeeDataTestRepository) {
        this.holidayDateRepository = holidayDateRepository;
        this.userFeignAPI = userFeignAPI;
        this.drawingWorkHourService = drawingWorkHourService;
        this.dingTalkWorkHourRepository = dingTalkWorkHourRepository;
        this.dingTalkLeaveDataRepository = dingTalkLeaveDataRepository;
        this.dingTalkEmployeeDataRepository = dingTalkEmployeeDataRepository;
        this.employeeDataRepository = employeeDataRepository;
        this.employeeDataTestRepository = employeeDataTestRepository;
    }

    /**
     * 开始定时任务(自动填报工时)
     */
    @Scheduled(cron = "0 0 21 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void startHoliday() {
        Calendar calendar = Calendar.getInstance();
        Integer weekly = Integer.valueOf(calendar.get(Calendar.YEAR) + "" + DateUtils.getFixedWeekOfYear(new Date()));
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        String today = format.format(new Date());

        List<String> chinaCity = new ArrayList<>();
        chinaCity.add("OEJS");
        chinaCity.add("OSTJ");
        chinaCity.add("OSQD");
        chinaCity.add("OSWH");
        chinaCity.add("OSNRG");
        chinaCity.add("OEIE");
        chinaCity.add("OEDL");

        List<String> singaporeCity = new ArrayList<>();
        singaporeCity.add("OSMOI");
        singaporeCity.add("OSMO");
        singaporeCity.add("EMOS");

        List<String> malaysiaCity = new ArrayList<>();
        malaysiaCity.add("OEH");
        malaysiaCity.add("TOI");

        List<String> indonesiaCity = new ArrayList<>();
        indonesiaCity.add("OCI");

        List<UserProfile> users = userFeignAPI.searchList().getData();
        for (UserProfile user : users) {
            // 用户有相关的公司信息并且是自动填报工时权限
            if (user.getStatus().equals(EntityStatus.ACTIVE) && user.getCompany() != null && !"".equals(user.getCompany()) && user.getAutoFillHours()) {
                // 根据不同国家判别当天是否为节假日
                HolidayData holidayData = new HolidayData();
                if (chinaCity.contains(user.getCompany())) {
                    holidayData = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(today, "China");
                } else if (singaporeCity.contains(user.getCompany())) {
                    holidayData = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(today, "Singapore");
                } else if (malaysiaCity.contains(user.getCompany())) {
                    holidayData = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(today, "Malaysia");
                } else if (indonesiaCity.contains(user.getCompany())) {
                    holidayData = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(today, "Indonesia");
                }


                if (holidayData == null && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    DrawingRecordCriteriaDTO drawingRecordCriteriaDTO = new DrawingRecordCriteriaDTO();

                    drawingRecordCriteriaDTO.setProjectName("Overhead");
//                    drawingRecordCriteriaDTO.setDrawingNo("Not-Application");
                    drawingRecordCriteriaDTO.setTask("autofill");

                    drawingRecordCriteriaDTO.setWorkHour(8.00);
                    drawingRecordCriteriaDTO.setDate(new Date());
                    drawingRecordCriteriaDTO.setWeekly(weekly);
                    drawingRecordCriteriaDTO.setAuto(true);

                    ContextDTO contextDTO = new ContextDTO();
                    OperatorDTO operatorDTO = new OperatorDTO();
                    operatorDTO.setName(user.getName());
                    operatorDTO.setAccessTokenId(user.getId());
                    contextDTO.setOperator(operatorDTO);
                    drawingWorkHourService.create(1677548865251514631L, 1677548865366586662L, drawingRecordCriteriaDTO, contextDTO);
                }
            }
        }

    }

    /**
     *  开始定时任务(获取加班数据)
     */
        @Scheduled(cron = "0 0 3 * * ?")
//    @Scheduled(cron = "0 0/1 7-23 * * ?")
    public void saveDingTalkHour() throws Exception {
        logger.info("读取数据定时任务准备执行");
        //获取前一天的加班数据
        List<Map<String, Object>> dataList = DingTalkUtil.getDingTalkHourData();
        logger.info("定时任务开始执行");
        for (Map<String, Object> data : dataList) {
                String jobNumber = (String) data.get("jobNumber");
                String workHourDate = (String) data.get("workHourDate");
                // 检查是否存在相同jobNumber和workHourDate的数据
                DingTalkWorkHour existingWorkHour = dingTalkWorkHourRepository.findByJobNumberAndWorkHourDate(jobNumber, workHourDate);
                if (existingWorkHour != null) {
                    // 如果存在，更新数据
                    existingWorkHour.setStartCheckTime((String) data.get("startCheckTime"));
                    existingWorkHour.setEndCheckTime((String) data.get("endCheckTime"));
                    existingWorkHour.setWorkDuration((Long) data.get("workDuration"));
                    existingWorkHour.setLastModifiedAt(new Date());
                    dingTalkWorkHourRepository.save(existingWorkHour);
                    logger.info("更新数据: jobNumber={}, workHourDate={}", jobNumber, workHourDate);
                } else {
                    // 如果不存在，保存新数据
                    DingTalkWorkHour workHour = new DingTalkWorkHour();
                    workHour.setStartCheckTime((String) data.get("startCheckTime"));
                    workHour.setEndCheckTime((String) data.get("endCheckTime"));
                    workHour.setJobNumber(jobNumber);
                    workHour.setWorkDuration((Long) data.get("workDuration"));
                    workHour.setWorkHourDate(workHourDate);

                    workHour.setStatus(EntityStatus.ACTIVE);
                    workHour.setCreatedAt(new Date());
                    workHour.setLastModifiedAt(new Date());
                    dingTalkWorkHourRepository.save(workHour);
                    logger.info("保存新数据: jobNumber={}, workHourDate={}", jobNumber, workHourDate);
                }
            }
            logger.info("定时任务结束，打印完毕");
        }

    /**
     *  开始定时任务(获取请假数据)
     */
            @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0 0/1 7-23 * * ?")
     public void saveDingTalkLeaveData() throws Exception{
                logger.info("读取数据定时任务准备执行");

                // 获取当前月的第一天和最后一天
                LocalDate now = LocalDate.now();
                YearMonth currentYearMonth = YearMonth.from(now);
                LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
                LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth().plusDays(1);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String startDate = firstDayOfMonth.format(formatter);
                String endDate = lastDayOfMonth.format(formatter);

                // 获取当前月的请假数据
                List<Map<String, Object>> dataList = DingTalkUtil.getDingTalkLeaveData(startDate, endDate);

            //获取前一天的请假数据
//            List<Map<String, Object>> dataList = DingTalkUtil.getDingTalkLeaveData();
            //获取时间范围内的请假数据
//            List<Map<String, Object>> dataList = DingTalkUtil.getDingTalkLeaveData("2025-02-01", "2025-03-05");
                logger.info("定时任务开始执行");
                for (Map<String, Object> data : dataList) {
                    String jobNumber = (String) data.get("jobNumber");
                    String startLeaveTime = (String) data.get("startLeaveTime");
                    // 检查是否存在相同 jobNumber 和 startLeaveTime 的数据
                    DingTalkLeaveData existingLeaveData = dingTalkLeaveDataRepository.findByJobNumberAndStartLeaveTime(jobNumber, startLeaveTime);
                    if (existingLeaveData != null) {
                        // 更新现有数据
                        existingLeaveData.setUserId((String) data.get("userid"));
                        existingLeaveData.setEndLeaveTime((String) data.get("endLeaveTime"));
                        existingLeaveData.setDurationUnit((String) data.get("durationUnit"));
                        existingLeaveData.setLeaveCode((String) data.get("leaveCode"));
                        existingLeaveData.setDurationPercent((Double) data.get("durationPercent"));
                        existingLeaveData.setLastModifiedAt(new Date());
                        dingTalkLeaveDataRepository.save(existingLeaveData);
                        logger.info("正在更新数据");
                    } else {
                        // 创建新数据
                        DingTalkLeaveData leaveData = new DingTalkLeaveData();
                        leaveData.setJobNumber((String) data.get("jobNumber"));
                        leaveData.setUserId((String) data.get("userid"));
                        leaveData.setStartLeaveTime((String) data.get("startLeaveTime"));
                        leaveData.setEndLeaveTime((String) data.get("endLeaveTime"));
                        leaveData.setDurationUnit((String) data.get("durationUnit"));
                        leaveData.setLeaveCode((String) data.get("leaveCode"));
                        leaveData.setDurationPercent((Double) data.get("durationPercent"));

                        leaveData.setStatus(EntityStatus.ACTIVE);
                        leaveData.setCreatedAt(new Date());
                        leaveData.setLastModifiedAt(new Date());
                        dingTalkLeaveDataRepository.save(leaveData);
                        logger.info("正在保存新数据");
                    }
                }
                logger.info("打印完毕");
        }

    /**
     * 开始定时任务(获取员工入职相关数据) 每个月的第7天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 7 * ?")
//    @Scheduled(cron = "0 0/1 7-23 * * ?")
    public void saveDingTalkEmployeeData() throws Exception{
        logger.info("读取数据定时任务准3备执行");
        List<Map<String, Object>> dataList = DingTalkUtil.getDingTalkEmployeeData();
        logger.info("定时任务开始执行");
        for (Map<String, Object> data : dataList) {
            // 格式化工号
            String jobNumber = formatJobNumber((String) data.get("工号"));
            String name = (String) data.get("姓名");
            if (jobNumber == null) {
                logger.error("工号为 null，跳过该条数据");
                continue;
            }

            // 检查是否存在相同 jobNumber 和 name 的数据
            DingTalkEmployeeData existingEmployeeData = dingTalkEmployeeDataRepository.findByJobNumberAndName(jobNumber, name);
            // 若存在则更新相关数据,不存在则创建
            if (existingEmployeeData != null) {
                updateExistingEmployeeData(existingEmployeeData, data);
            } else {
                createNewEmployeeData(data, jobNumber);
            }
        }
        logger.info("打印完毕");

        // 年假处理
        List<Map<String, Object>> employeeDataList = dingTalkEmployeeDataRepository.searchUserInfo();
        for (Map<String, Object> existingEmployeeData : employeeDataList) {
            String jobNumber = (String) existingEmployeeData.get("job_number");
            if (jobNumber == null || jobNumber.trim().isEmpty()) {
                continue; // 如果为空，跳过当前循环，处理下一条记录
            }
            String name = (String) existingEmployeeData.get("name");
            // 检查是否存在相同 jobNumber 和 name 的数据
            DingTalkEmployeeData foundEmployeeData = dingTalkEmployeeDataRepository.findByJobNumberAndName(jobNumber, name);
            if (foundEmployeeData!= null) {
                // 设置首次参保日期
                String initialEmploymentDate = (String) existingEmployeeData.get("initial_employment_date");
                foundEmployeeData.setInitialEmploymentDate(initialEmploymentDate);
                logger.info("首次参保日期更新");
                // 设置转正日期
                String transferToRegularDate = (String) existingEmployeeData.get("transfer_to_regular_date");
                foundEmployeeData.setRegularTime(transferToRegularDate);

                if (existingEmployeeData.get("length_of_career") != null) {
                    Double lengthOfCareer = Double.parseDouble((String) existingEmployeeData.get("length_of_career"));
                    foundEmployeeData.setLengthOfCareer(lengthOfCareer);
                }

                dingTalkEmployeeDataRepository.save(foundEmployeeData);

                if (initialEmploymentDate != null) {
                    // 初始化上月数据
//                    initializeLastMonthData(foundEmployeeData);

                    // 每月数据交换
                    updateMonthlyData(foundEmployeeData);

                    // 每季度加班时长清零和三月一号剩余年假清零
                    resetQuarterlyOvertimeAndAnnualLeaveMarch(foundEmployeeData);

                    // 计算年假和加班时长
                    calculateAnnualLeaveAndOvertime(foundEmployeeData, existingEmployeeData, initialEmploymentDate);

                }
                dingTalkEmployeeDataRepository.save(foundEmployeeData);
            }
        }
        logger.info("打印完毕");
    }

    // 计算上一个月的加班时长
    public double calculateMonthlyOvertime(String jobNumber) {

        double monthlyOvertime = 0.0;
        LocalDate currentDate = LocalDate.now();

        // 获取上一个月的起始日期和结束日期
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        LocalDate startWorkHourDate = previousMonthDate.withDayOfMonth(1);  // 上个月的第一天
        LocalDate endWorkHourDate = previousMonthDate.withDayOfMonth(previousMonthDate.lengthOfMonth());  // 上个月的最后一天

        // 查询该员工的加班数据
        List<Map<String, Object>> dataList = dingTalkWorkHourRepository.findOutHourByJobNumberAndWorkHourDate(
            jobNumber,
            startWorkHourDate.toString(),  // 转换为字符串
            endWorkHourDate.toString()    // 转换为字符串
        );

        // 计算加班总时长
        for (Map<String, Object> data : dataList) {
            if (data.get("outHour") != null) {
                monthlyOvertime += ((Number) data.get("outHour")).doubleValue();
            }
        }

        return monthlyOvertime;
    }

    // 格式化工号
    private String formatJobNumber(String jobNumber) {
        return jobNumber != null ? jobNumber.replace("-", "_") : null;
    }

    // 更新现有员工数据
    private void updateExistingEmployeeData(DingTalkEmployeeData existingEmployeeData, Map<String, Object> data) {
        existingEmployeeData.setConfirmJoinTime((String) data.get("入职时间"));
        existingEmployeeData.setEmployeeStatus((String) data.get("员工状态")); // 2:试用期 3:正式 5:待离职 -1:无状态
        existingEmployeeData.setRegularTime((String) data.get("实际转正日期"));
        existingEmployeeData.setProbationPeriodType((String) data.get("试用期"));
        existingEmployeeData.setLastModifiedAt(new Date());
        // 获取首次参保日期计算工龄
        String initialEmploymentDate = existingEmployeeData.getInitialEmploymentDate();
        if (initialEmploymentDate != null) {
            double lengthOfCareer = calculateLengthOfCareer(initialEmploymentDate);
            existingEmployeeData.setLengthOfCareer(lengthOfCareer);
        }

        dingTalkEmployeeDataRepository.save(existingEmployeeData);
        logger.info("正在更新数据");
    }

    // 创建新员工数据
    private void createNewEmployeeData(Map<String, Object> data, String jobNumber) {
        DingTalkEmployeeData dingTalkEmployeeData = new DingTalkEmployeeData();
        dingTalkEmployeeData.setJobNumber(jobNumber);
        dingTalkEmployeeData.setName((String) data.get("姓名"));
        dingTalkEmployeeData.setConfirmJoinTime((String) data.get("入职时间"));
        dingTalkEmployeeData.setEmployeeStatus((String) data.get("员工状态"));
        dingTalkEmployeeData.setRegularTime((String) data.get("实际转正日期"));
        dingTalkEmployeeData.setProbationPeriodType((String) data.get("试用期"));

        String initialEmploymentDate = dingTalkEmployeeData.getInitialEmploymentDate();
        if (initialEmploymentDate != null) {
            double lengthOfCareer = calculateLengthOfCareer(initialEmploymentDate);
            dingTalkEmployeeData.setLengthOfCareer(lengthOfCareer);
        }

        dingTalkEmployeeData.setStatus(EntityStatus.ACTIVE);
        dingTalkEmployeeData.setCreatedAt(new Date());
        dingTalkEmployeeData.setLastModifiedAt(new Date());
        dingTalkEmployeeDataRepository.save(dingTalkEmployeeData);
        logger.info("正在保存新数据");
    }

    // 计算年假和加班时长
    private void calculateAnnualLeaveAndOvertime(DingTalkEmployeeData foundEmployeeData, Map<String, Object> existingEmployeeData, String initialEmploymentDate) throws Exception {

        // 计算工龄
        double lengthOfCareer = calculateLengthOfCareer(initialEmploymentDate);
        foundEmployeeData.setLengthOfCareer(lengthOfCareer);

        // 处理 NT_ 开头工号员工的状态,在转正期当月仍未试用期
        handleNTEmployeeStatus(foundEmployeeData);

        // 根据员工状态计算加班时长
        String employeeStatus = foundEmployeeData.getEmployeeStatus();
        double overtime = 0.0;

        if (employeeStatus.equals("3")) { // 正式员工
            // 正式员工的加班时长为当前季度的加班时长
//            overtime = calculateQuarterlyOvertime(foundEmployeeData.getJobNumber());
            overtime = foundEmployeeData.getRemainingOt(); // -----修改
        } else if (employeeStatus.equals("2")) { // 试用期员工
            // 试用期员工的加班时长为累计的月度加班时长
            overtime = foundEmployeeData.getRemainingOt();
        }

        // 设置总加班时长
        foundEmployeeData.setTotalOT(overtime);

        // 超出时间默认为0
        foundEmployeeData.setAbsence(0.0);


        // 计算本年度年假或试用期年假
        double totalAnnualLeave = 0;
        double probationAnnualLeave = 0;

        if (employeeStatus != null) {
            if (employeeStatus.equals("3")) { // 正式员工
                totalAnnualLeave = calculateAnnualLeaveOnHire(initialEmploymentDate, lengthOfCareer);
                foundEmployeeData.setTotalAnnualLeave(totalAnnualLeave);
            } else if (employeeStatus.equals("2")) { // 试用期员工
                LocalDate currentDate = LocalDate.now().minusMonths(1);
                probationAnnualLeave = calculateProbationMonthlyAnnualLeave(initialEmploymentDate, lengthOfCareer, currentDate);
                // 如果存在上一年度剩余年假，则扣除
//                double remainingAnnualLeave = Optional.ofNullable(foundEmployeeData.getRemainingAnnual()).orElse(0.0) - foundEmployeeData.getSpecialAnnualLeave();
//                foundEmployeeData.setTotalAnnualLeave(remainingAnnualLeave + probationAnnualLeave);
                foundEmployeeData.setTotalAnnualLeave(probationAnnualLeave);
            }
            if ("NT_284".equals(foundEmployeeData.getJobNumber())) {
                // NT_284年假刷新按照入职日期算
                totalAnnualLeave = handleSpecialEmployeeAnnualLeave(foundEmployeeData, foundEmployeeData.getConfirmJoinTime());
                foundEmployeeData.setTotalAnnualLeave(totalAnnualLeave);
            }
        }

        // 处理一月和二月的特殊规则
        LocalDate currentDate = LocalDate.now().minusMonths(1); // 获取上个月的信息
//        LocalDate currentDate = LocalDate.of(2025, 01, 01);
        // 初始化剩余年假
        if (foundEmployeeData.getRemainingAnnual() == null) {
            if (currentDate.getMonth() == Month.JANUARY) {
                // 一月份：剩余年假 = 当年度总年假 + 上一年度剩余年假
                foundEmployeeData.setRemainingAnnual(totalAnnualLeave + foundEmployeeData.getSpecialAnnualLeave());
                foundEmployeeData.setRemainingOt(overtime);
            }
        }

        // 处理特殊员工的年假延长
        if (specialEmployees.containsKey(foundEmployeeData.getJobNumber())) {
            String extendedDateStr = specialEmployees.get(foundEmployeeData.getJobNumber());
            LocalDate extendedDate = LocalDate.parse(extendedDateStr);

            if (currentDate.isBefore(extendedDate)) {
                // 如果当前日期在延长日期之前，优先使用上一年度的年假
                double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(foundEmployeeData.getJobNumber(), dingTalkLeaveDataRepository);
                double remainingSpecialAnnualLeave = foundEmployeeData.getSpecialAnnualLeave() - usedAnnualLeaveThisMonth;

                if (remainingSpecialAnnualLeave >= 0) {
                    // 上一年度的剩余年假足够扣除
                    foundEmployeeData.setSpecialAnnualLeave(remainingSpecialAnnualLeave);
                    if (foundEmployeeData.getEmployeeStatus().equals("2")){ // 试用期的年假需要累计 需要用上一月的剩余年假计算
                        foundEmployeeData.setRemainingAnnual(foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth);
                    } else {
                        foundEmployeeData.setRemainingAnnual(foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave);
                    }
                } else {
                    // 如果上一年度的剩余年假不够扣除，则扣除本年度的剩余年假
                    foundEmployeeData.setSpecialAnnualLeave(0.0);
                    double remainingAnnualLeave = 0.0;
                    if(foundEmployeeData.getEmployeeStatus().equals("3")){
                        if (foundEmployeeData.getRegularTime() != null && !foundEmployeeData.getRegularTime().isEmpty()) { // 判断是否是本月刚转正的员工
                            LocalDate regularTime = LocalDate.parse(foundEmployeeData.getRegularTime()); // 解析转正日期字符串
                            LocalDate actualConvertDate = regularTime.plusMonths(1); // 实际转正日期为下一个月
                            boolean isRecentlyConverted = currentDate.getMonthValue() == actualConvertDate.getMonthValue()
                                && currentDate.getYear() == actualConvertDate.getYear();
                            if (isRecentlyConverted) {
                                probationAnnualLeave = calculateProbationMonthlyAnnualLeave(initialEmploymentDate, lengthOfCareer, currentDate);
                                int n = actualConvertDate.getMonthValue(); // 实际转正的月份
                                foundEmployeeData.setTotalAnnualLeave(foundEmployeeData.getTotalAnnualLeave()
                                    - probationAnnualLeave
                                    * n
                                    + foundEmployeeData.getRemainingAnnual());

                                remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave;
                            } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                                remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                            } else {
                                remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                            }
                        } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                            remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        } else {
                            remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        }

                    } else if (foundEmployeeData.getEmployeeStatus().equals("2")){
                        remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth; // remainingSpecialAnnualLeave 为负数
                    }
                    if (remainingAnnualLeave >= 0) {
                        foundEmployeeData.setRemainingAnnual(remainingAnnualLeave);
                    } else {
                        // 如果本年度的剩余年假也不够扣除，则扣除加班时长
                        foundEmployeeData.setRemainingAnnual(0.0);
                        double remainingOt = Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + remainingAnnualLeave; // remainingAnnualLeave 为负数
                        if (remainingOt >= 0) {
                            foundEmployeeData.setRemainingOt(remainingOt);
                        } else {
                            // 如果加班时长也不够扣除，则在 Absence 中显示超出的时长
                            foundEmployeeData.setRemainingOt(0.0);
                            foundEmployeeData.setAbsence(foundEmployeeData.getAbsence() + remainingOt);
                        }
                    }
                }
            } else {
                // 如果当前日期超过延长日期，清零上一年度的年假
                foundEmployeeData.setSpecialAnnualLeave(0.0);
                logger.info("特殊员工 " + foundEmployeeData.getJobNumber() + " 的年假延长日期已过，上一年度年假已清零");
            }
        } else if (currentDate.getMonth() == Month.JANUARY) {
            // 优先扣除上一年度的剩余年假
            double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(foundEmployeeData.getJobNumber(), dingTalkLeaveDataRepository);
            double remainingSpecialAnnualLeave = foundEmployeeData.getSpecialAnnualLeave() - usedAnnualLeaveThisMonth;

            if (remainingSpecialAnnualLeave >= 0) {
                // 上一年度的剩余年假足够扣除，则当前剩余年假为 扣除后的上一年度剩余年假 和 本年度剩余年假 的总和
                foundEmployeeData.setSpecialAnnualLeave(remainingSpecialAnnualLeave);
                foundEmployeeData.setRemainingAnnual(foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave);
            } else {
                // 如果上一年度的剩余年假不够扣除，则扣除本年度的剩余年假
                foundEmployeeData.setSpecialAnnualLeave(0.0);
                double remainingAnnualLeave = 0.0;
                if(foundEmployeeData.getEmployeeStatus().equals("3")){
                    if (foundEmployeeData.getRegularTime() != null && !foundEmployeeData.getRegularTime().isEmpty()) { // 判断是否是本月刚转正的员工
                        LocalDate regularTime = LocalDate.parse(foundEmployeeData.getRegularTime()); // 解析转正日期字符串
                        LocalDate actualConvertDate = regularTime.plusMonths(1); // 实际转正日期为下一个月
                        boolean isRecentlyConverted = currentDate.getMonthValue() == actualConvertDate.getMonthValue()
                            && currentDate.getYear() == actualConvertDate.getYear();
                        if (isRecentlyConverted) {
                            probationAnnualLeave = calculateProbationMonthlyAnnualLeave(initialEmploymentDate, lengthOfCareer, currentDate);
                            int n = actualConvertDate.getMonthValue(); // 实际转正的月份
                            foundEmployeeData.setTotalAnnualLeave(foundEmployeeData.getTotalAnnualLeave()
                                - probationAnnualLeave
                                * n
                                + foundEmployeeData.getRemainingAnnual());

                            remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave;
                        } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                            remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        } else {
                            remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        }
                    } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                        remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                    } else {
                        remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                    }

                } else if (foundEmployeeData.getEmployeeStatus().equals("2")){
                    remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth; // remainingSpecialAnnualLeave 为负数
                }
                if (remainingAnnualLeave >= 0) {
                    foundEmployeeData.setRemainingAnnual(remainingAnnualLeave);
                } else {
                    // 如果本年度的剩余年假也不够扣除，则扣除加班时长
                    foundEmployeeData.setRemainingAnnual(0.0);
                    double remainingOt = Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + remainingAnnualLeave; // remainingAnnualLeave 为负数
                    if (remainingOt >= 0) {
                        foundEmployeeData.setRemainingOt(remainingOt);
                    } else {
                        // 如果加班时长也不够扣除，则在 Absence 中显示超出的时长
                        foundEmployeeData.setRemainingOt(0.0);
                        foundEmployeeData.setAbsence(foundEmployeeData.getAbsence() + remainingOt);
                    }
                }
            }
        } else if(currentDate.getMonth() == Month.FEBRUARY){
            // 优先扣除上一年度的剩余年假
            double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(foundEmployeeData.getJobNumber(), dingTalkLeaveDataRepository);
            double remainingSpecialAnnualLeave = foundEmployeeData.getSpecialAnnualLeave() - usedAnnualLeaveThisMonth;
            if (usedAnnualLeaveThisMonth != 0){
                if (remainingSpecialAnnualLeave >= 0) {
                    // 上一年度的剩余年假足够扣除，则当前剩余年假为 扣除后的上一年度剩余年假 和 本年度剩余年假 的总和
                    foundEmployeeData.setSpecialAnnualLeave(remainingSpecialAnnualLeave);
                    if (foundEmployeeData.getEmployeeStatus().equals("3")){
                        foundEmployeeData.setRemainingAnnual(foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave);
                    } else if (foundEmployeeData.getEmployeeStatus().equals("2")){
                        foundEmployeeData.setRemainingAnnual(foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth);
                    }
                } else {
                    // 如果上一年度的剩余年假不够扣除，则扣除本年度的剩余年假
                    foundEmployeeData.setSpecialAnnualLeave(0.0);
                    double remainingAnnualLeave = 0.0;
                    if(foundEmployeeData.getEmployeeStatus().equals("3")){
                        if (foundEmployeeData.getRegularTime() != null && !foundEmployeeData.getRegularTime().isEmpty()) { // 判断是否是本月刚转正的员工
                            LocalDate regularTime = LocalDate.parse(foundEmployeeData.getRegularTime()); // 解析转正日期字符串
                            LocalDate actualConvertDate = regularTime.plusMonths(1); // 实际转正日期为下一个月
                            boolean isRecentlyConverted = currentDate.getMonthValue() == actualConvertDate.getMonthValue()
                                                          && currentDate.getYear() == actualConvertDate.getYear();
                            if (isRecentlyConverted) {
                                probationAnnualLeave = calculateProbationMonthlyAnnualLeave(initialEmploymentDate, lengthOfCareer, currentDate);
                                int n = actualConvertDate.getMonthValue(); // 实际转正的月份
                                foundEmployeeData.setTotalAnnualLeave(foundEmployeeData.getTotalAnnualLeave()
                                                                      - probationAnnualLeave
                                                                      * n
                                                                      + foundEmployeeData.getRemainingAnnual());

                                remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave;
                            } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                                remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                            } else {
                                remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                            }
                        } else if (foundEmployeeData.getTotalAnnualLeave() >= foundEmployeeData.getRemainingAnnual()) { // 不是刚转正的员工
                            remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        } else {
                            remainingAnnualLeave = foundEmployeeData.getTotalAnnualLeave() + remainingSpecialAnnualLeave; // remainingSpecialAnnualLeave 为负数
                        }

                    } else if (foundEmployeeData.getEmployeeStatus().equals("2")){
                        remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth; // remainingSpecialAnnualLeave 为负数
                    }
                    if (remainingAnnualLeave >= 0) {
                        foundEmployeeData.setRemainingAnnual(remainingAnnualLeave);
                    } else {
                        // 如果本年度的剩余年假也不够扣除，则扣除加班时长
                        foundEmployeeData.setRemainingAnnual(0.0);
                        double remainingOt = Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + remainingAnnualLeave; // remainingAnnualLeave 为负数
                        if (remainingOt >= 0) {
                            foundEmployeeData.setRemainingOt(remainingOt);
                        } else {
                            // 如果加班时长也不够扣除，则在 Absence 中显示超出的时长
                            foundEmployeeData.setRemainingOt(0.0);
                            foundEmployeeData.setAbsence(foundEmployeeData.getAbsence() + remainingOt);
                        }
                    }
                }
            }
            if (foundEmployeeData.getSpecialAnnualLeave() != 0){
                foundEmployeeData.setRemainingAnnual(foundEmployeeData.getRemainingAnnual() - foundEmployeeData.getSpecialAnnualLeave());

            }
        }else {
            // 其他月份，优先扣除本年度的剩余年假
            double usedAnnualLeaveThisMonth = calculateUsedAnnualLeaveThisMonth(foundEmployeeData.getJobNumber(), dingTalkLeaveDataRepository);
            double remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() - usedAnnualLeaveThisMonth;

            if (remainingAnnualLeave >= 0) {
                foundEmployeeData.setRemainingAnnual(remainingAnnualLeave);
            } else {
                // 如果本年度的剩余年假不够扣除，则扣除加班时长
                foundEmployeeData.setRemainingAnnual(0.0);
                double remainingOt = Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + remainingAnnualLeave; // remainingAnnualLeave 为负数
                if (remainingOt >= 0) {
                    foundEmployeeData.setRemainingOt(remainingOt);
                } else {
                    // 如果加班时长也不够扣除，则在 Absence 中显示超出的时长
                    foundEmployeeData.setRemainingOt(0.0);
                    foundEmployeeData.setAbsence(foundEmployeeData.getAbsence() + remainingOt);
                }
            }
        }

        // 更新 totalAnnual
        double updatedTotalAnnual = foundEmployeeData.getTotalAnnualLeave() + foundEmployeeData.getSpecialAnnualLeave();
        foundEmployeeData.setTotalAnnual(updatedTotalAnnual);
    }

    // 初始化上月数据
    private void initializeLastMonthData(DingTalkEmployeeData foundEmployeeData) {
        List<Map<String, Object>> employeeDataTest = employeeDataTestRepository.searchDataByJobNumber(foundEmployeeData.getJobNumber());
        for (Map<String, Object> data : employeeDataTest) {
            foundEmployeeData.setRemainingAnnualLastMth((double) data.get("remaining_annual_leave"));
            foundEmployeeData.setSpecialAnnualLeave((double) data.get("remaining_annual_leave"));
            double totalAnnual = foundEmployeeData.getSpecialAnnualLeave() + foundEmployeeData.getTotalAnnualLeave();
            foundEmployeeData.setTotalAnnual(totalAnnual);
            foundEmployeeData.setRegularTime((String) data.get("transfer_to_regular_date"));
            // 获取剩余加班时长
            double remainingOtLastMth = ((Double) data.get("remaining_ot_manhour")) + ((Double) data.get("remaining_ot_manhour_until_the_end_of_last_mth"));
            remainingOtLastMth = Math.max(0, remainingOtLastMth);
            foundEmployeeData.setRemainingOtLastMth(remainingOtLastMth);
            foundEmployeeData.setRemainingOt(remainingOtLastMth);
            logger.info("初始化上月数据");
        }
    }

    // 每季度加班时长处理及清零和三月一号剩余年假清零
    private void resetQuarterlyOvertimeAndAnnualLeaveMarch(DingTalkEmployeeData foundEmployeeData) {

        if ("NT_284".equals(foundEmployeeData.getJobNumber())) {
            // 特殊员工 NT_284 的年假周期重置逻辑
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate employmentDate = LocalDate.parse(foundEmployeeData.getConfirmJoinTime(), formatter);
            LocalDate currentDate = LocalDate.now();

            // 计算当前周期的结束日期
            long completeCycles = ChronoUnit.YEARS.between(employmentDate, currentDate);
            LocalDate currentCycleEndDate = employmentDate.plusYears(completeCycles + 1);

            // 如果当前日期超过当前周期的结束日期，则重置年假
            if (currentDate.isAfter(currentCycleEndDate)) {
                foundEmployeeData.setRemainingAnnual(168.0); // 重置为 168 小时
                foundEmployeeData.setTotalAnnualLeave(168.0); // 重置总年假
                logger.info("特殊员工 NT_284 的年假周期已结束，年假已重置为 168 小时");
            }
        }

        // 获取上个月的信息，并将日期设置为该月的第一天
        LocalDate currentDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
//        LocalDate currentDate = LocalDate.of(2025, 01, 01);
        int currentDay = currentDate.getDayOfMonth();
        Month currentMonth = currentDate.getMonth();

        // 处理 NT_ 开头工号员工的状态,在转正期当月仍未试用期
        handleNTEmployeeStatus(foundEmployeeData);

        // 判断是否为每季度的第一天（1月1日、4月1日、7月1日、10月1日）
        if ((currentMonth == Month.JANUARY && currentDay == 1) ||
            (currentMonth == Month.APRIL && currentDay == 1) ||
            (currentMonth == Month.JULY && currentDay == 1) ||
            (currentMonth == Month.OCTOBER && currentDay == 1)) {
            // 正式员工的加班时长每季度清零
            if (foundEmployeeData.getEmployeeStatus().equals("3")) {
                foundEmployeeData.setRemainingOt(0.0);
//                foundEmployeeData.setRemainingOt(calculateMonthlyOvertime(foundEmployeeData.getJobNumber())); // ----修改
                foundEmployeeData.setRemainingOtLastMth(0.0); // 清零上月加班时长
                logger.info("每季度第一天，正式员工加班时长已清零");
            }
        }

        // 处理特殊员工的年假延长
        if (specialEmployees.containsKey(foundEmployeeData.getJobNumber())) {
            String extendedDateStr = specialEmployees.get(foundEmployeeData.getJobNumber());
            LocalDate extendedDate = LocalDate.parse(extendedDateStr);

            if (currentDate.isAfter(extendedDate)) {
                // 如果当前日期超过延长日期，清零上一年度的年假
                foundEmployeeData.setSpecialAnnualLeave(0.0);
                logger.info("特殊员工 " + foundEmployeeData.getJobNumber() + " 的年假延长日期已过，上一年度年假已清零");
            }
        }

//        // 判断是否为三月一号
//        if (currentMonth == Month.MARCH && currentDay == 1) {
//            // 清零剩余年假
//            foundEmployeeData.setRemainingAnnual(foundEmployeeData.getRemainingAnnual() -
//                                                 foundEmployeeData.getSpecialAnnualLeave()); // 三月剩余年假应扣除上一年度年假。
////            foundEmployeeData.setRemainingAnnualLastMth(0.0);
//            foundEmployeeData.setSpecialAnnualLeave(0.0); // 清零上一年度剩余年假
//            logger.info("三月一号，剩余年假已清零");
//        }

        // 处理试用期员工的加班时长和年假的累计
        if (foundEmployeeData.getEmployeeStatus().equals("2")) {
            LocalDate regularTime = LocalDate.parse(foundEmployeeData.getRegularTime());
            if (currentDate.isAfter(regularTime)) {
                // 转正后，加班时长清零
                foundEmployeeData.setRemainingOt(0.0); // 转正后清零加班时长
                foundEmployeeData.setRemainingOtLastMth(0.0);
                logger.info("试用期员工已转正，加班时长已清零");
            } else {
                // 试用期期间，按月累计加班时长
                double monthlyOvertime = calculateMonthlyOvertime(foundEmployeeData.getJobNumber());
                foundEmployeeData.setRemainingOt(Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + monthlyOvertime);
                logger.info("试用期员工加班时长累计中，当前累计加班时长: " + foundEmployeeData.getRemainingOt());

                // 试用期期间，按月累计年假
                double monthlyAnnualLeave = calculateProbationMonthlyAnnualLeave(
                    foundEmployeeData.getInitialEmploymentDate(), // 入职日期
                    foundEmployeeData.getLengthOfCareer(), // 工龄
                    LocalDate.now().minusMonths(1)          // 上个月的日期
                );
                // 如果存在上一年度剩余年假，则扣除
                if (!"NT_284".equals(foundEmployeeData.getJobNumber())){
//                    double remainingAnnualLeave = foundEmployeeData.getRemainingAnnual() - foundEmployeeData.getSpecialAnnualLeave();
                    foundEmployeeData.setRemainingAnnual(Optional.ofNullable(foundEmployeeData.getRemainingAnnual()).orElse(0.0) + monthlyAnnualLeave);
//                  foundEmployeeData.setRemainingAnnual(remainingAnnualLeave + monthlyAnnualLeave);
                    logger.info("试用期员工年假累计中，当前累计年假: " + foundEmployeeData.getRemainingAnnual());
                }
            }
        }

        if (foundEmployeeData.getEmployeeStatus().equals("3")) {
            // 一季度内，按月累计加班时长
            double monthlyOvertime = calculateMonthlyOvertime(foundEmployeeData.getJobNumber());
            foundEmployeeData.setRemainingOt(Optional.ofNullable(foundEmployeeData.getRemainingOt()).orElse(0.0) + monthlyOvertime);
            logger.info("员工加班时长累计中，当前累计加班时长: " + foundEmployeeData.getRemainingOt());
        }
    }

    // 每月数据交换
    private void updateMonthlyData(DingTalkEmployeeData foundEmployeeData) {
        LocalDate currentDate = LocalDate.now().minusMonths(1); // 获取上个月的信息
//        LocalDate currentDate = LocalDate.of(2025, 01, 01);
        YearMonth currentYearMonth = YearMonth.from(currentDate);

        // 假设上一次记录的日期
        LocalDate previousDate = LocalDate.of(2025, 01, 01); // 可以改为动态日期
        YearMonth previousYearMonth = YearMonth.from(previousDate);

        // 判断是否进入新的一个月
        if (!previousYearMonth.equals(currentYearMonth)) {
            // 交换上月数据和当月数据
            foundEmployeeData.setRemainingAnnualLastMth(foundEmployeeData.getRemainingAnnual());
            foundEmployeeData.setRemainingOtLastMth(foundEmployeeData.getRemainingOt());

            logger.info("进入新的一个月，上月数据和当月数据已交换");
        }

        // 如果当前月份是1月，将上个月的剩余年假赋值给 SpecialAnnualLeave
        if (currentDate.getMonth() == Month.JANUARY) {
            foundEmployeeData.setSpecialAnnualLeave(foundEmployeeData.getRemainingAnnualLastMth());
        }
    }

    // 处理 NT_ 开头工号员工的状态,在转正期当月仍未试用期
    public static void handleNTEmployeeStatus(DingTalkEmployeeData foundEmployeeData) {
        String jobNumber = foundEmployeeData.getJobNumber();
        String regularTime = foundEmployeeData.getRegularTime();
        LocalDate currentDate = LocalDate.now().minusMonths(1);

        if (jobNumber != null && jobNumber.startsWith("NT_") && regularTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate regularizationDate = LocalDate.parse(regularTime, formatter);
            if (currentDate.isBefore(regularizationDate.withDayOfMonth(1).plusMonths(1))) {
                foundEmployeeData.setEmployeeStatus("2");
            }
        }
    }

    private double handleSpecialEmployeeAnnualLeave(DingTalkEmployeeData foundEmployeeData, String initialEmploymentDate) {
        double fixedAnnualLeave = 168.0; // 固定年假时长
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate employmentDate = LocalDate.parse(initialEmploymentDate, formatter); // 入职日期
        LocalDate currentDate = LocalDate.now(); // 当前日期

        // 计算当前周期的结束日期（入职日期 + 1 年）
        LocalDate currentCycleEndDate = employmentDate.plusYears(1);

        // 如果当前日期超过当前周期的结束日期，则重置年假
        if (currentDate.isAfter(currentCycleEndDate)) {
            return fixedAnnualLeave; // 重置为固定年假时长
        } else {
            return foundEmployeeData.getRemainingAnnual(); // 保持当前剩余年假
        }
    }
}
