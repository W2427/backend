package com.ose.tasks.domain.model.service;

import com.alibaba.excel.EasyExcel;
import com.ose.auth.api.UserFeignAPI;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.OverTimeApplicationRepository;
import com.ose.tasks.domain.model.repository.OverTimeApplyUserTranferRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRecordRepository;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.repository.holiday.WorkDayRepository;
import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PerformanceEvaluationService extends StringRedisService implements PerformanceEvaluationInterface {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceEvaluationService.class);

    private final OverTimeApplicationRepository overTimeApplicationRepository;

    private final OverTimeApplyUserTranferRepository overTimeApplyUserTranferRepository;

    private final UserFeignAPI userFeignAPI;

    private final HolidayDateRepository holidayDateRepository;

    private final WorkDayRepository workDayRepository;

    private final DrawingRecordRepository drawingRecordRepository;

    private final ProjectRepository projectRepository;


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.templateFilePath}")
    private String templateFilePath;

    @Value("${application.files.filePath}")
    private String filePath;


    @Autowired
    public PerformanceEvaluationService(OverTimeApplicationRepository overTimeApplicationRepository,
                                        StringRedisTemplate stringRedisTemplate,
                                        UserFeignAPI userFeignAPI,
                                        HolidayDateRepository holidayDateRepository,
                                        DrawingRecordRepository drawingRecordRepository,
                                        ProjectRepository projectRepository,
                                        OverTimeApplyUserTranferRepository overTimeApplyUserTranferRepository,
                                        WorkDayRepository workDayRepository) {
        super(stringRedisTemplate);
        this.overTimeApplicationRepository = overTimeApplicationRepository;
        this.userFeignAPI = userFeignAPI;
        this.holidayDateRepository = holidayDateRepository;
        this.drawingRecordRepository = drawingRecordRepository;
        this.projectRepository = projectRepository;
        this.overTimeApplyUserTranferRepository = overTimeApplyUserTranferRepository;
        this.workDayRepository = workDayRepository;
    }


    @Override
    public Page<PerformanceEvaluationDataDTO> getList(PerformanceEvaluationSearchDTO dto, Long operatorId) {
        //查询日期范围内的假期和周末列表

        Map<String,Integer> companyToNormalDays = new HashMap<>();
        Map<String,List<String>> companyToHolidates = new HashMap<>();
        if (dto.getStartDate() != null && dto.getEndDate() != null ){
            Map<String, String> companyToCountry = companyToCountry();
            for (Map.Entry<String, String> entry : companyToCountry.entrySet()) {
                String company = entry.getKey();
                String country = entry.getValue();

                List<String> allHolidayDate = holidayDateRepository.findAllHolidayDateByCountry(dto.getStartDate(), dto.getEndDate(),country);
                //计算周末
                List<String> weekends = findWeekends(dto.getStartDate(), dto.getEndDate());
                //周末去掉调休日期
                List<String> workDays = workDayRepository.findAllHolidayDate(dto.getStartDate(), dto.getEndDate());
                if (workDays !=null && workDays.size() > 0){
                    weekends = weekends.stream()
                        .filter(date -> !workDays.contains(date))
                        .collect(Collectors.toList());
                }

                List<String> holidayDates = Stream.concat(allHolidayDate.stream(), weekends.stream())
                    .distinct()
                    .collect(Collectors.toList());

                if (allHolidayDate.size() > 0){
                    companyToHolidates.put(company,allHolidayDate);
                }

                // 将字符串日期转换为 LocalDate
                LocalDate startDate = LocalDate.parse(dto.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate endDate = LocalDate.parse(dto.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // 创建集合来存储不在排除列表中的日期
                List<LocalDate> includedDates = new ArrayList<>();

                // 遍历日期范围
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    // 检查日期是否在排除列表中
                    if (!holidayDates.contains(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                        includedDates.add(currentDate);
                    }
                    // 增加一天
                    currentDate = currentDate.plusDays(1);
                }

                companyToNormalDays.put(company,includedDates.size());
            }
        }
        Page<PerformanceEvaluationDataDTO> performanceEvaluationDataDTOS = overTimeApplicationRepository.searchEvaluation(
            operatorId,
            dto,
            companyToNormalDays,
            companyToHolidates
        );
        //holiday&totalLeave
        for (PerformanceEvaluationDataDTO item : performanceEvaluationDataDTOS) {
//            if (companyToHolidates.get(item.getCompany()) != null){
            Double holidayHour = overTimeApplicationRepository.searchHolidayHour(item.getEngineerId(), dto.getStartDate(), dto.getEndDate());
            item.setHoliday(holidayHour);

            Double totalLeave = overTimeApplicationRepository.searchTotalLeave(item.getEngineerId(), dto.getStartDate(), dto.getEndDate());
            item.setTotalLeave(totalLeave);
//            }else {
//                item.setHoliday(0.0);
//
//                Double totalLeave = overTimeApplicationRepository.searchTotalLeave(item.getEngineerId(), dto.getStartDate(), dto.getEndDate());
//                item.setTotalLeave(totalLeave);
//            }
        }
        return performanceEvaluationDataDTOS;
    }


    private List<String> findWeekends(String startDateStr, String endDateStr) {
        List<String> weekends = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            calendar.setTime(startDate);
            while (!calendar.getTime().after(endDate)) {
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                // 判断是否为周六或周日
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    weekends.add(sdf.format(calendar.getTime()));
                }
                // 增加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return weekends;
    }

    @Override
    public File saveDownloadFile(PerformanceEvaluationSearchDTO criteriaDTO, Long operatorId) throws ParseException {



        String templateFilePathDown = templateFilePath +"export-timesheet-summary.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePathDown = filePath + temporaryFileName;

        File excel = new File(temporaryDir, temporaryFileName);

        criteriaDTO.getPage().setNo(1);
        criteriaDTO.getPage().setSize(36655);

        List<PerformanceEvaluationDataDTO> performanceEvaluationDataDTOs = getList(criteriaDTO,operatorId).getContent();
        for (PerformanceEvaluationDataDTO item : performanceEvaluationDataDTOs) {
            // 使用 DecimalFormat 格式化为两位小数
            DecimalFormat df = new DecimalFormat("#.00");
            // 计算 pjManhourSaturation
            if ((item.getStandardHour() - item.getTotalLeave()) > 0 ){
                double pjManhourSaturation = (item.getProjectManhour() * 100) / (item.getStandardHour() - item.getTotalLeave());
                item.setPjManhourSaturation(Double.valueOf(df.format(pjManhourSaturation)));
            }else {
                item.setPjManhourSaturation(0.00);
            }
            // 计算 pjManhourSaturationStudyExclude
            if((item.getStandardHour() - item.getTotalLeave() - item.getGeneralStudyHour() - item.getProjectStudyHour()) > 0){
                double pjManhourSaturationStudyExclude = (item.getProjectManhour() * 100) / (item.getStandardHour() - item.getTotalLeave() - item.getGeneralStudyHour() - item.getProjectStudyHour());
                item.setPjManhourSaturationStudyExclude(Double.valueOf(df.format(!Double.isNaN(pjManhourSaturationStudyExclude) ? pjManhourSaturationStudyExclude :0.00)));
            }else {
                item.setPjManhourSaturationStudyExclude(0.00);
            }
        }
        EasyExcel.write(filePathDown).withTemplate(templateFilePathDown).sheet("timesheet").doFill(performanceEvaluationDataDTOs);
        return excel;
    }

    private Map<String, String> companyToCountry(){

        Map<String, String> companyToCountry = new HashMap<>();
        companyToCountry.put("OEJS", "China");
        companyToCountry.put("OSTJ", "China");
        companyToCountry.put("OSQD", "China");
        companyToCountry.put("OSWH", "China");
        companyToCountry.put("OSNRG", "China");
        companyToCountry.put("OESZ", "China");
        companyToCountry.put("OEIE", "China");
        companyToCountry.put("OEDL", "China");

        companyToCountry.put("OSMOI", "Singapore");
        companyToCountry.put("OSMO", "Singapore");
        companyToCountry.put("EMOS", "Singapore");

        companyToCountry.put("OEH", "Malaysia");
        companyToCountry.put("TOI", "Malaysia");
        companyToCountry.put("OSEH", "Malaysia");

        companyToCountry.put("OCI", "Indonesia");

        return companyToCountry;
    }
}
