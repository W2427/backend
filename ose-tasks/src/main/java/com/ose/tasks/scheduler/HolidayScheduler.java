package com.ose.tasks.scheduler;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.drawing.DrawingRecordRepository;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingWorkHourInterface;
import com.ose.tasks.dto.DrawingRecordCriteriaDTO;
import com.ose.tasks.entity.holiday.HolidayData;
import com.ose.util.DateUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class HolidayScheduler {

    private final static Logger logger = LoggerFactory.getLogger(HolidayScheduler.class);

    private final HolidayDateRepository holidayDateRepository;

    private final UserFeignAPI userFeignAPI;

    private final DrawingWorkHourInterface drawingWorkHourService;

    private final DrawingRecordRepository drawingRecordRepository;
    @Autowired
    public HolidayScheduler(
        HolidayDateRepository holidayDateRepository,
        UserFeignAPI userFeignAPI,
        DrawingWorkHourInterface drawingWorkHourService,
        DrawingRecordRepository drawingRecordRepository
    ) {
        this.holidayDateRepository = holidayDateRepository;
        this.userFeignAPI = userFeignAPI;
        this.drawingWorkHourService = drawingWorkHourService;
        this.drawingRecordRepository = drawingRecordRepository;
    }

    /**
     * 开始定时任务（查看是否为节假日）。
     */
    @Scheduled(cron = "0 0 20 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void startHoliday() {

        List<String> chinaCity = new ArrayList<>();
        chinaCity.add("OEJS");
        chinaCity.add("OSTJ");
        chinaCity.add("OSQD");
        chinaCity.add("OSWH");
        chinaCity.add("OSNRG");
        chinaCity.add("OESZ");
        chinaCity.add("OEDL");
        chinaCity.add("IDE");

        List<String> singaporeCity = new ArrayList<>();
        singaporeCity.add("OSMOI");
        singaporeCity.add("OSMO");
        singaporeCity.add("EMOS");

        List<String> malaysiaCity = new ArrayList<>();
        malaysiaCity.add("OEH");
        malaysiaCity.add("TOI");

        List<String> indonesiaCity = new ArrayList<>();
        indonesiaCity.add("OCI");


        Calendar calendar = Calendar.getInstance();
//        Integer weekly = Integer.valueOf(calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.WEEK_OF_YEAR));
        Integer weekly = Integer.valueOf(calendar.get(Calendar.YEAR) + "" + DateUtils.getFixedWeekOfYear(new Date()));
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        String today = format.format(new Date());
        List<HolidayData> holidayDatas = holidayDateRepository.findByHolidayDateAndDeletedIsFalse(today);
        List<UserProfile> users = userFeignAPI.searchList().getData();
        for (HolidayData holidayData : holidayDatas) {
            if (holidayData != null) {
                for (UserProfile user : users) {
                    if (user.getStatus().equals(EntityStatus.ACTIVE) && user.getCompany() != null && !"".equals(user.getCompany())) {

                        if ((chinaCity.contains(user.getCompany()) && holidayData.getCountry().equals("China")) ||
                            (singaporeCity.contains(user.getCompany()) && holidayData.getCountry().equals("Singapore")) ||
                            (malaysiaCity.contains(user.getCompany()) && holidayData.getCountry().equals("Malaysia")) ||
                            (indonesiaCity.contains(user.getCompany()) && holidayData.getCountry().equals("Indonesia"))) {
                            DrawingRecordCriteriaDTO drawingRecordCriteriaDTO = new DrawingRecordCriteriaDTO();

                            drawingRecordCriteriaDTO.setTask("Holiday");
                            drawingRecordCriteriaDTO.setProjectName("Leave");

                            drawingRecordCriteriaDTO.setWorkHour(8.00);
                            drawingRecordCriteriaDTO.setDate(new Date());
                            drawingRecordCriteriaDTO.setWeekly(weekly);
                            drawingRecordCriteriaDTO.setRemark(holidayData.getHolidayName());

                            ContextDTO contextDTO = new ContextDTO();
                            OperatorDTO operatorDTO = new OperatorDTO();
                            operatorDTO.setName(user.getName());
                            operatorDTO.setAccessTokenId(user.getId());
                            contextDTO.setOperator(operatorDTO); try {
                                drawingWorkHourService.create(1677548865251514631L, 1677548865366586662L, drawingRecordCriteriaDTO, contextDTO);
                            } catch (Exception e) {
                                // 记录错误日志
                                logger.error("Failed to create drawing work hour: {}", user.getName(), e);
                                // 继续循环
                                continue;
                            }

                        }
                    }
                }
            }
        }

    }


//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void testWeek() throws ParseException {
        System.out.println("==========start==========");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Integer fixedWeekOfYear = DateUtils.getFixedWeekOfYear(new Date());

//        List<DrawingRecord> needFixedRecord = drawingRecordRepository.findNeedFixedRecord();
//        for (DrawingRecord item:needFixedRecord) {
//            String date = item.getWorkHourDate();
//            Date parse = simpleDateFormat.parse(date);
//            Integer weekly = Integer.valueOf(  DateUtils.getYear(parse) + "" + DateUtils.getFixedWeekOfYear(parse));
//            item.setWeekly(weekly);
//            drawingRecordRepository.save(item);
//        }

        System.out.println("======end=======");

    }
}
