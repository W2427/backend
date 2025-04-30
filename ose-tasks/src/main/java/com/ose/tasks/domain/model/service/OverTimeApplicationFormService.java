package com.ose.tasks.domain.model.service;

import com.alibaba.excel.EasyExcel;
import com.google.common.base.Joiner;
import com.ose.util.DateUtils;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.OverTimeApplicationRepository;
import com.ose.tasks.domain.model.repository.OverTimeApplyUserTranferRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRecordRepository;
import com.ose.tasks.domain.model.repository.holiday.HolidayDateRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingWorkHourInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.OverTimeApplicationForm;
import com.ose.tasks.entity.OverTimeAppyFormUserTransferHistory;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.tasks.util.easyExcel.ApplyStatusConverter;
import com.ose.tasks.util.easyExcel.LocalDateConverter;
import com.ose.tasks.vo.ApplyStatus;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OverTimeApplicationFormService extends StringRedisService implements OverTimeApplicationFormInterface {

    private final static Logger logger = LoggerFactory.getLogger(OverTimeApplicationFormService.class);

    private final OverTimeApplicationRepository overTimeApplicationRepository;

    private final OverTimeApplyUserTranferRepository overTimeApplyUserTranferRepository;

    private final UserFeignAPI userFeignAPI;

    private final HolidayDateRepository holidayDateRepository;

    private final DrawingRecordRepository drawingRecordRepository;

    private final DrawingWorkHourInterface drawingWorkHourService;

    private final ProjectRepository projectRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.templateFilePath}")
    private String templateFilePath;

    @Value("${application.files.filePath}")
    private String filePath;


    @Autowired
    public OverTimeApplicationFormService(OverTimeApplicationRepository overTimeApplicationRepository,
                                          StringRedisTemplate stringRedisTemplate,
                                          UserFeignAPI userFeignAPI,
                                          HolidayDateRepository holidayDateRepository,
                                          DrawingRecordRepository drawingRecordRepository,
                                          ProjectRepository projectRepository,
                                          OverTimeApplyUserTranferRepository overTimeApplyUserTranferRepository,
                                          DrawingWorkHourInterface drawingWorkHourService) {
        super(stringRedisTemplate);
        this.overTimeApplicationRepository = overTimeApplicationRepository;
        this.userFeignAPI = userFeignAPI;
        this.holidayDateRepository = holidayDateRepository;
        this.drawingRecordRepository = drawingRecordRepository;
        this.projectRepository = projectRepository;
        this.overTimeApplyUserTranferRepository = overTimeApplyUserTranferRepository;
        this.drawingWorkHourService = drawingWorkHourService;
    }


    @Override
    public Page<OverTimeApplicationForm> getList(OverTimeApplicationFormSearchDTO dto, Long operatorId) {
        return overTimeApplicationRepository.search(
            operatorId,
            dto
        );
    }

    @Override
    public Page<OverTimeApplicationForm> getAllList(OverTimeApplicationFormSearchDTO dto, Long operatorId) {
        return overTimeApplicationRepository.searchAll(
            operatorId,
            dto
        );
    }

    /**
     * 先找team leader 不存在则找父级team leader直至找到或者不存在team leader
     * 2024.3.15新增规则：一个team可能 会有两个team leader
     * @return 存在TeamLeader的TeamName
     */
    private TeamLeaderDO findTeamLeader(String teamName, Long userId){
        TeamLeaderDO result = new TeamLeaderDO();

        if (teamName == null || "".equals(teamName)){
            return null;
        }
        List<Long> teamLeaderIds = overTimeApplicationRepository.findUserIdByTeam(teamName == null ? "" : teamName, userId);
        List<String> teamLeaderNameList = overTimeApplicationRepository.findUserNameByTeam(teamName == null ? "" : teamName, userId);

        String parentTeamName = overTimeApplicationRepository.findParentTeamByTeam(teamName == null ? "" : teamName);

        if (teamLeaderIds != null && teamLeaderIds.size() > 0){//如果当前team有team leader
            StringBuilder teamLeaderId = new StringBuilder();
            for (int i = 0; i < teamLeaderIds.size(); i++) {
                teamLeaderId.append(teamLeaderIds.get(i));
                if (i < teamLeaderIds.size() - 1) {
                    teamLeaderId.append(",");
                }
            }
            result.setId(teamLeaderId.toString());
            String teamLeaderName = String.join(",", teamLeaderNameList);
            result.setName(teamLeaderName);
            return result;
        }else if (!"".equals(parentTeamName)){//如果当前team有team leader但有parent team
            return findTeamLeader(parentTeamName,userId);
        }else {//都没有返回null
            return null;
        }
    }

    @Override
    public OverTimeApplicationForm create(OperatorDTO operatorDTO, OverTimeApplicationFormCreateDTO dto) throws ParseException {

        //新增判断判断是否根据项目组织架构设置审批人
        Boolean isProjectApprove = false;

        if (dto.getProjectName() == null || "".equals(dto.getProjectName())){
            throw new BusinessError("Please reselect project");
        }

        if ( dto.getProjectId() == null || dto.getProjectId() == 0L ){
            throw new BusinessError("Please reselect project");
        }

        //新增判断判断填报项目是否关闭或禁止填报加班工时
        Optional<Project> optional = projectRepository.findById(dto.getProjectId());
        Project project1 = projectRepository.findByNameAndDeletedIsFalse(dto.getProjectName());
        if (project1 != null) {
            if (project1.getStatus() != EntityStatus.ACTIVE) {
                throw new BusinessError("this project is closed");
            }
            if (project1.getHaveOvertime() != null && !project1.getHaveOvertime()) {
                throw new BusinessError("this project is not allow filling in overtime");
            }
            if (project1.getApproveOvertime() != null){
                isProjectApprove = project1.getApproveOvertime();
            }
        }

        // 1、判断当前日期是否在节假日内
        Boolean startDateIsInHoliday = holidayDateRepository.existsByHolidayDateAndDeletedIsFalse(dto.getStartDate());
//        Boolean endDateIsInHoliday = holidayDateRepository.existsByHolidayDateAndDeletedIsFalse(dto.getEndDate());
        if (startDateIsInHoliday) {
            throw new BusinessError("The selected overtime date conflicts with holidays!");
        }

        // 2、判断当前所填加班时间是否重复
        List<OverTimeApplicationForm> forms = overTimeApplicationRepository.findByUserIdAndProjectIdAndApplyStatusNotAndDeletedIsFalse(operatorDTO.getId(), dto.getProjectId(), ApplyStatus.REJECT);
        if (forms.size() > 0) {
            for (int i = 0; i < forms.size(); i++) {
                // 2.2、如果有相同的项目和任务填写，再比较填写时间是否冲突
                OverTimeApplicationForm form = forms.get(i);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sf.format(form.getStartDate());
                if (dto.getStartDate().contains(date)) {
                    throw new BusinessError("On the same day, the same project, and status as ACCESS can only be applied for once!");
                }
            }
        }

        OverTimeApplicationForm overTimeApplicationForm = new OverTimeApplicationForm();
        overTimeApplicationForm.setStatus(EntityStatus.ACTIVE);
        overTimeApplicationForm.setDeleted(false);
        overTimeApplicationForm.setCreatedAt();
        overTimeApplicationForm.setCreatedBy(operatorDTO.getId());
        overTimeApplicationForm.setLastModifiedAt();
        overTimeApplicationForm.setLastModifiedBy(operatorDTO.getId());

        // 3、通过userFeign接口获取当前申请人的信息
        overTimeApplicationForm.setUserId(operatorDTO.getId());
        overTimeApplicationForm.setEmployeeId(userFeignAPI.get(operatorDTO.getId()).getData().getUsername());
        overTimeApplicationForm.setDisplayName(userFeignAPI.get(operatorDTO.getId()).getData().getName());
        overTimeApplicationForm.setCompany(userFeignAPI.get(operatorDTO.getId()).getData().getCompany());
        overTimeApplicationForm.setDivision(userFeignAPI.get(operatorDTO.getId()).getData().getDivision());
        overTimeApplicationForm.setDepartment(userFeignAPI.get(operatorDTO.getId()).getData().getDepartment());
        overTimeApplicationForm.setTeam(userFeignAPI.get(operatorDTO.getId()).getData().getTeam());

        Double manHour = 0.0;
        // 4、结算加班工时数据
        if (dto.getMealTimeHours() != null) {
            manHour = dto.getOverTimeHours() - dto.getMealTimeHours();
            overTimeApplicationForm.setManHour(manHour);
            overTimeApplicationForm.setMealTimeHours(dto.getMealTimeHours());
            overTimeApplicationForm.setOverTimeHours(dto.getOverTimeHours());
        } else {
            manHour = dto.getOverTimeHours();
            overTimeApplicationForm.setManHour(dto.getOverTimeHours());
            overTimeApplicationForm.setOverTimeHours(dto.getOverTimeHours());
        }
        // 5、判断当前加班时间是否超过半小时
        if (manHour > 16) {
            throw new BusinessError("Overtime work hours have exceeded the upper limit!");
        } else if (manHour <= 0.5 && manHour >= 0.0) {

            overTimeApplicationForm.setApplyStatus(ApplyStatus.ACCEPT);
            overTimeApplicationForm.setReviewStatus(ApplyStatus.UNAPPROVED);
        } else if (manHour < 0.0) {
            throw new BusinessError("Please note whether the overtime is normal!");
        } else {
            overTimeApplicationForm.setApplyStatus(ApplyStatus.UNAPPROVED);
            overTimeApplicationForm.setReviewStatus(ApplyStatus.UNAPPROVED);
        }

        UserProfile userProfile = userFeignAPI.get(operatorDTO.getId()).getData();

        Long orgId = overTimeApplicationRepository.findOrgIdByName(dto.getProjectName());
        // 6、通过组织id获取相关审批vp信息
        Boolean noApprovalRequired = userFeignAPI.get(operatorDTO.getId()).getData().getNoApprovalRequired();
        if (!noApprovalRequired) {
            //20241230新增薛云的OT审批给许总
            if (overTimeApplicationForm.getUserId() == 1719803683549884483L){
                overTimeApplicationForm.setApplyRoleId("1680160184583411877");
                overTimeApplicationForm.setApplyRoleName("Bob Xu 许冬晨");
            }else if(isProjectApprove){
                // 项目
                Long applyRoleId = overTimeApplicationRepository.findUserIdByOrgId(orgId, operatorDTO.getId());
                String applyRoleName = overTimeApplicationRepository.findUserNameByOrgId(orgId, operatorDTO.getId());
                // 查出当前级主管后，还要确认本人是不是主管，如果是继续往上查一级
                if (applyRoleId != null && applyRoleId != 0L) {
                    overTimeApplicationForm.setApplyRoleId(applyRoleId.toString());
                    overTimeApplicationForm.setApplyRoleName(applyRoleName);
                }
            }else  {
                // 非项目
                //            if (orgId == null || orgId == 0L) {
                TeamLeaderDO teamLeader = findTeamLeader(overTimeApplicationForm.getTeam(), operatorDTO.getId());
                //                Long teamLeaderId = overTimeApplicationRepository.findUserIdByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
                //                String teamLeaderName = overTimeApplicationRepository.findUserNameByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
                if (teamLeader != null && !userProfile.getTeamLeader()) {
                    overTimeApplicationForm.setApplyRoleId(teamLeader.getId());
                    overTimeApplicationForm.setApplyRoleName(teamLeader.getName());
                } else {
                    // teamLeader为Null,则去找divisionVP
                    Long divisionVPId = overTimeApplicationRepository.findUserIdByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                    String divisionVPName = overTimeApplicationRepository.findUserNameByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                    //2024.1.16新增规则如果部门是PRPI就把审批人设为Sean He 何晓峰  id：1680160184583411703
                    if (overTimeApplicationForm.getDivision() != null && overTimeApplicationForm.getDivision().equals("PRPI")) {
                        divisionVPId = 1680160184583411703L;
                        divisionVPName = "Sean He 何晓峰";
                    }
                    if (divisionVPId != null && divisionVPId != 0L && !userProfile.getDivisionVP()) {
                        overTimeApplicationForm.setApplyRoleId(divisionVPId.toString());
                        overTimeApplicationForm.setApplyRoleName(divisionVPName);
                    } else {
                        // 则去找divisionVP,则去找companyGM
                        Long companyGMId = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                        String companyGMName = overTimeApplicationRepository.findUserNameByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                        if (companyGMId != null && companyGMId != 0L) {
                            overTimeApplicationForm.setApplyRoleId(companyGMId.toString());
                            overTimeApplicationForm.setApplyRoleName(companyGMName);
                        } else {
                            throw new BusinessError("Leader、VP、GM not found under project!");
                        }
                    }
                }
            }
        } else {
            overTimeApplicationForm.setApplyStatus(ApplyStatus.ACCEPT);
        }

        // 7、通过用户表的company来获取相关公司的行政审批人员
        String reviewRoleId = "";
        if (overTimeApplicationForm.getCompany() != null && (overTimeApplicationForm.getCompany().equals("OSEH") ||
            overTimeApplicationForm.getCompany().equals("OSMO") || overTimeApplicationForm.getCompany().equals("OCI"))) {
            List<String> companys = new ArrayList<>();
            companys.add("OSEH");
            companys.add("OSMO");
            companys.add("OCI");
            List<Long> reviewRoleIds = overTimeApplicationRepository.findUserIdByCompanyIn(companys);
            if (!reviewRoleIds.isEmpty()){
                reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
        } else {
            List<Long> reviewRoleIds  = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany());
            if (!reviewRoleIds.isEmpty()){
                reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
        }
        if (reviewRoleId.isEmpty()) {
            List<Long> reviewRoleIds  = overTimeApplicationRepository.findUserIdByReviewCompany("%" + overTimeApplicationForm.getCompany() + "%");
            reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        if (reviewRoleId.isEmpty()) {
            throw new BusinessError("There are no auditors under the company!");
        } else {
            overTimeApplicationForm.setReviewRoleId(reviewRoleId);
        }

        overTimeApplicationForm.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getStartDate()));
        overTimeApplicationForm.setStartTime(dto.getStartTime());
        overTimeApplicationForm.setProjectName(dto.getProjectName());
        Project project = projectRepository.findByNameAndDeletedIsFalse(dto.getProjectName());
        if (project != null) {
            overTimeApplicationForm.setProjectId(project.getId());
        } else {
            overTimeApplicationForm.setProjectId(1677548865366586662L);
        }
        String taskList = "";
        if (dto.getTask() != null && dto.getTask().size() > 0) {
            taskList = Joiner.on(",").join(dto.getTask());
        }
        overTimeApplicationForm.setTask(taskList);
        overTimeApplicationForm.setTaskDescription(dto.getTaskDescription() == null ? null : dto.getTaskDescription());


        overTimeApplicationRepository.save(overTimeApplicationForm);
        //申请加班工时小于等于0.5自动填写工时信息
        if (overTimeApplicationForm.getManHour() <= 0.5 && overTimeApplicationForm.getManHour() > 0.0) {
            autoCreateDrawingRecord(overTimeApplicationForm);
        }

        if (overTimeApplicationForm.getApplyRoleId() != null && overTimeApplicationForm.getApplyRoleName() != null) {
            OverTimeAppyFormUserTransferHistory history = new OverTimeAppyFormUserTransferHistory();
            history.setOvertimeApplyFormId(overTimeApplicationForm.getId());
            history.setApplyRoleId(overTimeApplicationForm.getApplyRoleId());
            history.setApplyRoleName(overTimeApplicationForm.getApplyRoleName());
            history.setStatus(EntityStatus.ACTIVE);
            overTimeApplyUserTranferRepository.save(history);
        }

        return overTimeApplicationForm;
    }

    @Override
    public void delete(OperatorDTO operatorDTO, Long id) {
        Optional<OverTimeApplicationForm> optionalOverTimeApplicationForm = overTimeApplicationRepository.findByIdAndDeletedIsFalse(id);
        if (optionalOverTimeApplicationForm.isPresent()) {
            OverTimeApplicationForm overTimeApplicationForm = optionalOverTimeApplicationForm.get();
            overTimeApplicationForm.setDeleted(true);
            overTimeApplicationForm.setStatus(EntityStatus.DELETED);
            overTimeApplicationForm.setDeletedAt();
            overTimeApplicationForm.setDeletedBy(operatorDTO.getId());
            overTimeApplicationForm.setLastModifiedBy(operatorDTO.getId());
            overTimeApplicationForm.setLastModifiedAt();
            overTimeApplicationRepository.save(overTimeApplicationForm);
        }
    }

    private void autoCreateDrawingRecord(OverTimeApplicationForm overTimeApplicationForm) {
        DrawingRecordCriteriaDTO drawingRecordCriteriaDTO = new DrawingRecordCriteriaDTO();
        if (overTimeApplicationForm.getProjectName() != null) {
            drawingRecordCriteriaDTO.setProjectName(overTimeApplicationForm.getProjectName());
        }
        if (overTimeApplicationForm.getTask() != null) {
            drawingRecordCriteriaDTO.setTask(overTimeApplicationForm.getTask());
        }
        if (overTimeApplicationForm.getStartDate() != null) {
            drawingRecordCriteriaDTO.setDate(overTimeApplicationForm.getStartDate());

            //补全周次信息
            Integer weekly = Integer.valueOf(DateUtils.getYear(overTimeApplicationForm.getStartDate()) + "" + DateUtils.getFixedWeekOfYear(overTimeApplicationForm.getStartDate()));
            drawingRecordCriteriaDTO.setWeekly(weekly);
        }
        if (overTimeApplicationForm.getManHour() != null) {
            drawingRecordCriteriaDTO.setOutHour(overTimeApplicationForm.getManHour());
        }
        if (overTimeApplicationForm.getTaskDescription() != null) {
            drawingRecordCriteriaDTO.setRemark(overTimeApplicationForm.getTaskDescription());
        }

        ContextDTO contextDTO = new ContextDTO();
        OperatorDTO employee = new OperatorDTO();
        employee.setName(overTimeApplicationForm.getDisplayName());
        employee.setAccessTokenId(drawingRecordRepository.findEmployeeIdByName(overTimeApplicationForm.getEmployeeId()));
        contextDTO.setOperator(employee);

        Long orgId = drawingRecordRepository.findOrgIdByProjectId(overTimeApplicationForm.getProjectId());

        drawingWorkHourService.create(orgId, overTimeApplicationForm.getProjectId(), drawingRecordCriteriaDTO, contextDTO);

    }

    @Override
    public OverTimeApplicationForm modify(OperatorDTO operatorDTO, ContextDTO context, Long id, OverTimeApplicationFormCreateDTO dto) throws ParseException {
        if (dto.getProjectName() == null || "".equals(dto.getProjectName())){
            throw new BusinessError("Please reselect project");
        }

        if ( dto.getProjectId() == null || dto.getProjectId() == 0L ){
            throw new BusinessError("Please reselect project");
        }
        Optional<OverTimeApplicationForm> overTimeApplicationFormOptional = overTimeApplicationRepository.findByIdAndDeletedIsFalse(id);
        if (overTimeApplicationFormOptional.isPresent()) {
            OverTimeApplicationForm overTimeApplicationForm = overTimeApplicationFormOptional.get();
            // 1、判断当前日期是否在节假日内
            Boolean startDateIsInHoliday = holidayDateRepository.existsByHolidayDateAndDeletedIsFalse(dto.getStartDate());
//            Boolean endDateIsInHoliday = holidayDateRepository.existsByHolidayDateAndDeletedIsFalse(dto.getEndDate());
            if (startDateIsInHoliday) {
                throw new BusinessError("The selected overtime date conflicts with holidays!");
            }

            // 2、判断当前所填加班时间是否重复
            List<OverTimeApplicationForm> forms = overTimeApplicationRepository.findByUserIdAndProjectIdAndApplyStatusNotAndDeletedIsFalse(operatorDTO.getId(), dto.getProjectId(), ApplyStatus.REJECT);
            if (forms.size() > 0) {
                for (int i = 0; i < forms.size(); i++) {
                    // 2.2、如果有相同的项目和任务填写，再比较填写时间是否冲突
                    OverTimeApplicationForm form = forms.get(i);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sf.format(form.getStartDate());
                    if (dto.getStartDate().contains(date) && !form.getId().equals(overTimeApplicationForm.getId())) {
                        throw new BusinessError("On the same day, the same project, and status as ACCESS can only be applied for once!");
                    }
                }
            }

            overTimeApplicationForm.setLastModifiedAt();
            overTimeApplicationForm.setLastModifiedBy(operatorDTO.getId());
            // 3、通过userFeign接口获取当前申请人的信息
            overTimeApplicationForm.setUserId(operatorDTO.getId());
            overTimeApplicationForm.setEmployeeId(userFeignAPI.get(operatorDTO.getId()).getData().getUsername());
            overTimeApplicationForm.setDisplayName(userFeignAPI.get(operatorDTO.getId()).getData().getName());
            overTimeApplicationForm.setCompany(userFeignAPI.get(operatorDTO.getId()).getData().getCompany());
            overTimeApplicationForm.setDivision(userFeignAPI.get(operatorDTO.getId()).getData().getDivision());
            overTimeApplicationForm.setDepartment(userFeignAPI.get(operatorDTO.getId()).getData().getDepartment());
            overTimeApplicationForm.setTeam(userFeignAPI.get(operatorDTO.getId()).getData().getTeam());

            Double manHour = 0.0;
            // 4、结算加班工时数据
            if (dto.getMealTimeHours() != null) {
                manHour = dto.getOverTimeHours() - dto.getMealTimeHours();
                overTimeApplicationForm.setManHour(manHour);
                overTimeApplicationForm.setMealTimeHours(dto.getMealTimeHours());
                overTimeApplicationForm.setOverTimeHours(dto.getOverTimeHours());
            } else {
                manHour = dto.getOverTimeHours();
                overTimeApplicationForm.setManHour(dto.getOverTimeHours());
                overTimeApplicationForm.setOverTimeHours(dto.getOverTimeHours());
            }
            // 5、判断当前加班时间是否超过半小时
            if (manHour > 16) {
                throw new BusinessError("Overtime work hours have exceeded the upper limit!");
            } else if (manHour <= 0.5 && manHour >= 0.0) {
                overTimeApplicationForm.setApplyStatus(ApplyStatus.ACCEPT);
                if (overTimeApplicationForm.getReviewStatus() == null) {
                    overTimeApplicationForm.setReviewStatus(ApplyStatus.UNAPPROVED);
                }
            } else if (manHour < 0.0) {
                throw new BusinessError("Please note whether the overtime is normal!");
            } else {
                overTimeApplicationForm.setApplyStatus(ApplyStatus.UNAPPROVED);
                if (overTimeApplicationForm.getReviewStatus() == null) {
                    overTimeApplicationForm.setReviewStatus(ApplyStatus.UNAPPROVED);
                }
            }


            // 6、通过组织id获取相关审批vp信息
            Boolean noApprovalRequired = userFeignAPI.get(operatorDTO.getId()).getData().getNoApprovalRequired();
            if (!noApprovalRequired) {
                Long orgId = overTimeApplicationRepository.findOrgIdByName(dto.getProjectName());
                // 非项目
                if (orgId == null || orgId == 0L) {
                    TeamLeaderDO teamLeader = findTeamLeader(overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                Long teamLeaderId = overTimeApplicationRepository.findUserIdByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                String teamLeaderName = overTimeApplicationRepository.findUserNameByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
                    if (teamLeader != null ) {
                        overTimeApplicationForm.setApplyRoleId(teamLeader.getId());
                        overTimeApplicationForm.setApplyRoleName(teamLeader.getName());
                    } else {
                        // teamLeader为Null,则去找divisionVP
                        Long divisionVPId = overTimeApplicationRepository.findUserIdByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                        String divisionVPName = overTimeApplicationRepository.findUserNameByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                        //2024.1.16新增规则如果部门是PRPI就把审批人设为Sean He 何晓峰  id：1680160184583411703
                        if (overTimeApplicationForm.getDivision().equals("PRPI")){
                            divisionVPId = 1680160184583411703L;
                            divisionVPName = "Sean He 何晓峰";
                        }
                        if (divisionVPId != null && divisionVPId != 0L) {
                            overTimeApplicationForm.setApplyRoleId(divisionVPId.toString());
                            overTimeApplicationForm.setApplyRoleName(divisionVPName);
                        } else {
                            // 则去找divisionVP,则去找companyGM
                            Long companyGMId = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                            String companyGMName = overTimeApplicationRepository.findUserNameByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                            if (companyGMId != null && companyGMId != 0L) {
                                overTimeApplicationForm.setApplyRoleId(companyGMId.toString());
                                overTimeApplicationForm.setApplyRoleName(companyGMName);
                            } else {
                                throw new BusinessError("Leader、VP、GM not found under project!");
                            }
                        }
                    }
                } else {
                    // 项目
                    List<BigInteger> childrenOrgIds = overTimeApplicationRepository.findByOrgIdAndUserId("%" + orgId.toString() + "%", operatorDTO.getId());
                    if (childrenOrgIds.size() > 0) {
                        for (int i = 0; i < childrenOrgIds.size(); i++) {
                            Long applyRoleId = overTimeApplicationRepository.findUserIdByOrgId(childrenOrgIds.get(i).longValue(), operatorDTO.getId());
                            String applyRoleName = overTimeApplicationRepository.findUserNameByOrgId(childrenOrgIds.get(i).longValue(), operatorDTO.getId());
                            // 查出当前级主管后，还要确认本人是不是主管，如果是继续往上查一级
                            if (applyRoleId != null && applyRoleId != 0L) {
                                overTimeApplicationForm.setApplyRoleId(applyRoleId.toString());
                                overTimeApplicationForm.setApplyRoleName(applyRoleName);
                                break;
                            }
                        }
                        // 如果项目中未查询到相应的leader，去users表中查询相同的team、division、company的信息
                        if (overTimeApplicationForm.getApplyRoleId() == null || overTimeApplicationForm.getApplyRoleId().equals("")) {
                            TeamLeaderDO teamLeader = findTeamLeader(overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                Long teamLeaderId = overTimeApplicationRepository.findUserIdByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                String teamLeaderName = overTimeApplicationRepository.findUserNameByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
                            if (teamLeader != null ) {
                                overTimeApplicationForm.setApplyRoleId(teamLeader.getId());
                                overTimeApplicationForm.setApplyRoleName(teamLeader.getName());
                            } else {
                                // teamLeader为Null,则去找divisionVP
                                Long divisionVPId = overTimeApplicationRepository.findUserIdByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                                String divisionVPName = overTimeApplicationRepository.findUserNameByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                                //2024.1.16新增规则如果部门是PRPI就把审批人设为Sean He 何晓峰  id：1680160184583411703
                                if (overTimeApplicationForm.getDivision().equals("PRPI")){
                                    divisionVPId = 1680160184583411703L;
                                    divisionVPName = "Sean He 何晓峰";
                                }
                                if (divisionVPId != null && divisionVPId != 0L) {
                                    overTimeApplicationForm.setApplyRoleId(divisionVPId.toString());
                                    overTimeApplicationForm.setApplyRoleName(divisionVPName);
                                } else {
                                    // 则去找divisionVP,则去找companyGM
                                    Long companyGMId = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                                    String companyGMName = overTimeApplicationRepository.findUserNameByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                                    if (companyGMId != null && companyGMId != 0L) {
                                        overTimeApplicationForm.setApplyRoleId(companyGMId.toString());
                                        overTimeApplicationForm.setApplyRoleName(companyGMName);
                                    } else {
                                        throw new BusinessError("Leader、VP、GM not found under project!");
                                    }
                                }
                            }
                        }
                    } else {
//            throw new BusinessError("This project has no organization set up!");
                        // 如果项目中未查询到相应的leader，去users表中查询相同的team、division、company的信息
                        if (overTimeApplicationForm.getApplyRoleId() == null || overTimeApplicationForm.getApplyRoleId().equals("")) {
                            TeamLeaderDO teamLeader = findTeamLeader(overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                Long teamLeaderId = overTimeApplicationRepository.findUserIdByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
//                String teamLeaderName = overTimeApplicationRepository.findUserNameByTeam(overTimeApplicationForm.getTeam() == null ? "" : overTimeApplicationForm.getTeam(), operatorDTO.getId());
                            if (teamLeader != null ) {
                                overTimeApplicationForm.setApplyRoleId(teamLeader.getId());
                                overTimeApplicationForm.setApplyRoleName(teamLeader.getName());
                            } else {
                                // teamLeader为Null,则去找divisionVP
                                Long divisionVPId = overTimeApplicationRepository.findUserIdByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                                String divisionVPName = overTimeApplicationRepository.findUserNameByDivision(overTimeApplicationForm.getDivision() == null ? "" : overTimeApplicationForm.getDivision(), operatorDTO.getId());
                                //2024.1.16新增规则如果部门是PRPI就把审批人设为Sean He 何晓峰  id：1680160184583411703
                                if (overTimeApplicationForm.getDivision().equals("PRPI")){
                                    divisionVPId = 1680160184583411703L;
                                    divisionVPName = "Sean He 何晓峰";
                                }
                                if (divisionVPId != null && divisionVPId != 0L) {
                                    overTimeApplicationForm.setApplyRoleId(divisionVPId.toString());
                                    overTimeApplicationForm.setApplyRoleName(divisionVPName);
                                } else {
                                    // 则去找divisionVP,则去找companyGM
                                    Long companyGMId = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                                    String companyGMName = overTimeApplicationRepository.findUserNameByCompany(overTimeApplicationForm.getCompany() == null ? "" : overTimeApplicationForm.getCompany(), operatorDTO.getId());
                                    if (companyGMId != null && companyGMId != 0L) {
                                        overTimeApplicationForm.setApplyRoleId(companyGMId.toString());
                                        overTimeApplicationForm.setApplyRoleName(companyGMName);
                                    } else {
                                        throw new BusinessError("Leader、VP、GM not found under project!");
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                overTimeApplicationForm.setApplyStatus(ApplyStatus.ACCEPT);
            }

            // 7、通过用户表的company来获取相关公司的行政审批人员
            String reviewRoleId = "";
            if (overTimeApplicationForm.getCompany() != null && (overTimeApplicationForm.getCompany().equals("OSEH") ||
                overTimeApplicationForm.getCompany().equals("OSMO") || overTimeApplicationForm.getCompany().equals("OCI"))) {
                List<String> companys = new ArrayList<>();
                companys.add("OSEH");
                companys.add("OSMO");
                companys.add("OCI");
                List<Long> reviewRoleIds = overTimeApplicationRepository.findUserIdByCompanyIn(companys);
                if (!reviewRoleIds.isEmpty()){
                    reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                }
            } else {
                List<Long> reviewRoleIds  = overTimeApplicationRepository.findUserIdByCompany(overTimeApplicationForm.getCompany());
                if (!reviewRoleIds.isEmpty()){
                    reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                }
            }
            if (reviewRoleId.isEmpty()) {
                List<Long> reviewRoleIds  = overTimeApplicationRepository.findUserIdByReviewCompany("%" + overTimeApplicationForm.getCompany() + "%");
                reviewRoleId = reviewRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            if (reviewRoleId.isEmpty()) {
                throw new BusinessError("There are no auditors under the company!");
            } else {
                overTimeApplicationForm.setReviewRoleId(reviewRoleId);
            }

            overTimeApplicationForm.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getStartDate()));
            overTimeApplicationForm.setStartTime(dto.getStartTime());
            overTimeApplicationForm.setProjectName(dto.getProjectName());
            Project project = projectRepository.findByNameAndDeletedIsFalse(dto.getProjectName());
            if (project != null) {
                overTimeApplicationForm.setProjectId(project.getId());
            } else {
                overTimeApplicationForm.setProjectId(1677548865366586662L);
            }
            String taskList = "";
            if (dto.getTask() != null && dto.getTask().size() > 0) {
                taskList = Joiner.on(",").join(dto.getTask());
            }
            overTimeApplicationForm.setTask(taskList);
            overTimeApplicationForm.setTaskDescription(dto.getTaskDescription() == null ? null : dto.getTaskDescription());

            overTimeApplicationRepository.save(overTimeApplicationForm);

            //申请加班工时小于等于0.5自动填写工时信息
            if (overTimeApplicationForm.getManHour() <= 0.5 && overTimeApplicationForm.getManHour() > 0.0) {
                autoCreateDrawingRecord(overTimeApplicationForm);
            }

            return overTimeApplicationForm;
        }
        return null;
    }

    @Override
    public void handle(Long id, OverTimeApplicationFormHandleDTO dto, OperatorDTO operatorDTO) {
        Optional<OverTimeApplicationForm> overTimeApplicationFormOptional = overTimeApplicationRepository.findById(id);
        if (overTimeApplicationFormOptional.isPresent()) {
            OverTimeApplicationForm overTimeApplicationForm = overTimeApplicationFormOptional.get();
            overTimeApplicationForm.setApplyStatus(ApplyStatus.valueOf(dto.getResult()));
            if (dto.getResult().equals(ApplyStatus.ACCEPT.toString())) {

//                //审核通过自动添加overtime数据
//                DrawingRecordCriteriaDTO drawingRecordCriteriaDTO = new DrawingRecordCriteriaDTO();
//                if (overTimeApplicationForm.getProjectName() != null){
//                    drawingRecordCriteriaDTO.setProjectName(overTimeApplicationForm.getProjectName());
//                }
//                if (overTimeApplicationForm.getTask() != null){
//                    drawingRecordCriteriaDTO.setTask(overTimeApplicationForm.getTask());
//                }
//                if (overTimeApplicationForm.getStartDate() != null){
//                    drawingRecordCriteriaDTO.setDate(overTimeApplicationForm.getStartDate());
//
//                    //补全周次信息
//                    Integer weekly = Integer.valueOf(DateUtils.getYear(overTimeApplicationForm.getStartDate()) + "" + DateUtils.getFixedWeekOfYear(overTimeApplicationForm.getStartDate()));
//                    drawingRecordCriteriaDTO.setWeekly(weekly);
//                }
//                if (overTimeApplicationForm.getManHour() != null){
//                    drawingRecordCriteriaDTO.setOutHour(overTimeApplicationForm.getManHour());
//                }
//
//
//
//                ContextDTO contextDTO = new ContextDTO();
//                OperatorDTO employee = new OperatorDTO();
//                employee.setName(overTimeApplicationForm.getDisplayName());
//                employee.setAccessTokenId(drawingRecordRepository.findEmployeeIdByName(overTimeApplicationForm.getEmployeeId()));
//                contextDTO.setOperator(employee);
//
//                Long orgId = drawingRecordRepository.findOrgIdByProjectId(overTimeApplicationForm.getProjectId());
//
//                drawingWorkHourService.create(orgId,overTimeApplicationForm.getProjectId(),drawingRecordCriteriaDTO,contextDTO);

                if (overTimeApplicationForm.getReviewStatus().equals(ApplyStatus.REJECT)) {
                    overTimeApplicationForm.setReviewStatus(ApplyStatus.UNAPPROVED);
                }
            }

            if (dto.getRemark() != null) {
                overTimeApplicationForm.setApplyRemark(dto.getRemark());
            }

            overTimeApplicationForm.setLastModifiedAt();
            overTimeApplicationForm.setLastModifiedBy(operatorDTO.getId());
            overTimeApplicationRepository.save(overTimeApplicationForm);

        }
    }

    @Override
    public void reviewHandle(Long id, OverTimeApplicationFormHandleDTO dto, OperatorDTO operatorDTO) {
        Optional<OverTimeApplicationForm> overTimeApplicationFormOptional = overTimeApplicationRepository.findById(id);
        if (overTimeApplicationFormOptional.isPresent()) {
            OverTimeApplicationForm overTimeApplicationForm = overTimeApplicationFormOptional.get();
            overTimeApplicationForm.setReviewStatus(ApplyStatus.valueOf(dto.getResult()));
            if (dto.getResult().equals(ApplyStatus.REJECT.toString())) {
                overTimeApplicationForm.setApplyStatus(ApplyStatus.UNAPPROVED);
                // 将相应的工时记录作废
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                List<DrawingRecord> drawingRecords = drawingRecordRepository.findByProjectIdAndEngineerIdAndWorkHourDateAndOutHour(
                    overTimeApplicationForm.getProjectId(),
                    overTimeApplicationForm.getUserId(),
                    sdf.format(overTimeApplicationForm.getStartDate())
                );
                if (drawingRecords.size() > 0) {
                    for (DrawingRecord drawingRecord : drawingRecords) {
                        drawingRecord.setStatus(EntityStatus.DELETED);
                        drawingRecord.setDeleted(true);
                        drawingRecord.setDeletedAt();
                        drawingRecord.setDeletedBy(operatorDTO.getId());
                        drawingRecordRepository.save(drawingRecord);
                    }
                }
            } else if (dto.getResult().equals(ApplyStatus.ACCEPT.toString()) && overTimeApplicationForm.getManHour() > 0.5 ) {
                //审核通过自动添加overtime数据
                DrawingRecordCriteriaDTO drawingRecordCriteriaDTO = new DrawingRecordCriteriaDTO();
                if (overTimeApplicationForm.getProjectName() != null) {
                    drawingRecordCriteriaDTO.setProjectName(overTimeApplicationForm.getProjectName());
                }
                if (overTimeApplicationForm.getTask() != null) {
                    drawingRecordCriteriaDTO.setTask(overTimeApplicationForm.getTask());
                }
                if (overTimeApplicationForm.getStartDate() != null) {
                    drawingRecordCriteriaDTO.setDate(overTimeApplicationForm.getStartDate());

                    //补全周次信息
                    Integer weekly = Integer.valueOf(DateUtils.getYear(overTimeApplicationForm.getStartDate()) + "" + DateUtils.getFixedWeekOfYear(overTimeApplicationForm.getStartDate()));
                    drawingRecordCriteriaDTO.setWeekly(weekly);
                }
                if (overTimeApplicationForm.getManHour() != null) {
                    drawingRecordCriteriaDTO.setOutHour(overTimeApplicationForm.getManHour());
                }
                if (overTimeApplicationForm.getTaskDescription() != null) {
                    drawingRecordCriteriaDTO.setRemark(overTimeApplicationForm.getTaskDescription());
                }

                ContextDTO contextDTO = new ContextDTO();
                OperatorDTO employee = new OperatorDTO();
                employee.setName(overTimeApplicationForm.getDisplayName());
                employee.setAccessTokenId(drawingRecordRepository.findEmployeeIdByName(overTimeApplicationForm.getEmployeeId()));
                contextDTO.setOperator(employee);

//                Long orgId = drawingRecordRepository.findOrgIdByProjectId(overTimeApplicationForm.getProjectId());
                Long orgId = 0L;
                Optional<Project> optionalProject = projectRepository.findById(overTimeApplicationForm.getProjectId());
                if (optionalProject.isPresent()) {
                    Project project = optionalProject.get();
                    orgId = project.getOrgId();
                }

                drawingWorkHourService.create(orgId, overTimeApplicationForm.getProjectId(), drawingRecordCriteriaDTO, contextDTO);
            }
            if (dto.getRemark() != null) {
                overTimeApplicationForm.setReviewRemark(dto.getRemark());
            }
            overTimeApplicationForm.setLastModifiedAt();
            overTimeApplicationForm.setLastModifiedBy(operatorDTO.getId());
            overTimeApplicationRepository.save(overTimeApplicationForm);
        }
    }

    @Override
    public OverTimeApplicationForm get(Long id) {
        Optional<OverTimeApplicationForm> optionalOverTimeApplicationForm = overTimeApplicationRepository.findById(id);
        if (optionalOverTimeApplicationForm.isPresent()) {
            return optionalOverTimeApplicationForm.get();
        }
        return null;
    }

    private double getDifferHour(Date startDate, Date endDate) {
        double dayM = 1000 * 24 * 60 * 60;
        double hourM = 1000 * 60 * 60;
        double differ = endDate.getTime() - startDate.getTime();
        double hour = differ % dayM / hourM;
        return Math.round(hour * 10) / 10.0;
    }

    @Override
    public OverTimeApplicationFormFilterDTO filter() {
        List<String> companyList = overTimeApplicationRepository.findByCompany();
        List<String> teamList = overTimeApplicationRepository.findByTeam();
        List<String> departmentList = overTimeApplicationRepository.findByDepartment();
        List<String> divisionList = overTimeApplicationRepository.findByDivision();
        List<String> projectNameList = overTimeApplicationRepository.findByProjectName();

        OverTimeApplicationFormFilterDTO overTimeApplicationFormFilterDTO = new OverTimeApplicationFormFilterDTO();
        overTimeApplicationFormFilterDTO.setCompanyList(companyList);
        overTimeApplicationFormFilterDTO.setTeamList(teamList);
        overTimeApplicationFormFilterDTO.setDepartmentList(departmentList);
        overTimeApplicationFormFilterDTO.setProjectNameList(projectNameList);
        overTimeApplicationFormFilterDTO.setDivisionList(divisionList);

        return overTimeApplicationFormFilterDTO;
    }

    @Override
    public void transfer(Long id, OverTimeApplicationFormTransferDTO dto, OperatorDTO operatorDTO) {
        Optional<OverTimeApplicationForm> optional = overTimeApplicationRepository.findById(id);
        if (optional.isPresent()) {
            optional.get().setApplyRoleId(dto.getId().toString());
            optional.get().setApplyRoleName(dto.getName());
            overTimeApplicationRepository.save(optional.get());
        }
        OverTimeAppyFormUserTransferHistory history = new OverTimeAppyFormUserTransferHistory();
        history.setOvertimeApplyFormId(id);
        history.setApplyRoleId(dto.getId().toString());
        history.setApplyRoleName(dto.getName());
        history.setStatus(EntityStatus.ACTIVE);
        overTimeApplyUserTranferRepository.save(history);
    }

    @Override
    public File saveDownloadFile(OverTimeApplicationFormSearchDTO criteriaDTO, Long operatorId) throws ParseException {


        String templateFilePathDown = templateFilePath + "export-over-time.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePathDown = filePath + temporaryFileName;

        File excel = new File(temporaryDir, temporaryFileName);

        criteriaDTO.getPage().setNo(1);
        criteriaDTO.getPage().setSize(36655);

        List<OverTimeApplicationForm> overTimeApplicationFormDataDTOS = getList(criteriaDTO, operatorId).getContent();

        EasyExcel.write(filePathDown).registerConverter(new LocalDateConverter()).registerConverter(new ApplyStatusConverter()).withTemplate(templateFilePathDown).sheet("over_time").doFill(overTimeApplicationFormDataDTOS);
        return excel;
    }
}
