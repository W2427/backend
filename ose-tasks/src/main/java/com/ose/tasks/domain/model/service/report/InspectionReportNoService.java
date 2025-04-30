package com.ose.tasks.domain.model.service.report;

import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.entity.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报告编号服务。
 */
@Component
public class InspectionReportNoService implements InspectionReportNoInterface {

    private ProjectService projectService;

    private final static String SEPARATOR_MID_BAR_LINE = "-";

    /**
     * 构造方法。
     */
    @Autowired
    public InspectionReportNoService(
        ProjectService projectService
    ) {
        this.projectService = projectService;
    }

    @Override
    public String generateNewReportNo(Long orgId, Long projectId, String process, String type, String moduleName) {

        final Project project = projectService.get(orgId, projectId);

//        InspectionReportNoManagement management = inspectionReportNoManagementRepository
//            .findByProjectNameAndProcessAndType(project.getName(), process, type);
//        if (management == null) {
//            Random r = new Random();
//            return project.getName()
//                + SEPARATOR_MID_BAR_LINE
//                + process
//                + SEPARATOR_MID_BAR_LINE
//                + this.getStrSeriesNo(r.nextInt(10000));
//        }
        String strSeriesNo = "";
        int numSeriesNo = 1;
//        InspectionReportNoManagementDetail detail = inspectionReportNoManagementDetailRepository
//            .findByReportNoManagementIdAndModule(management.getId(), moduleName);
//
//        if (detail != null) {
//            numSeriesNo = detail.getSeriesNo() + 1;
//            strSeriesNo = this.getStrSeriesNo(numSeriesNo);
//            detail.setSeriesNo(numSeriesNo);
//        } else {
//            strSeriesNo = "0001";
//
//            detail = new InspectionReportNoManagementDetail();
//            detail.setModule(moduleName);
//            detail.setReportNoManagementId(management.getId());
//            detail.setSeriesNo(1);
//        }
//        inspectionReportNoManagementDetailRepository.save(detail);

        StringBuffer reportNo = new StringBuffer("");
//        reportNo.append(project.getName())
//            .append(SEPARATOR_MID_BAR_LINE)
//            .append(management.getDepartmentNo())
//            .append(SEPARATOR_MID_BAR_LINE)
//            .append(management.getDisciplineNo());

        if (moduleName != null
            && !"".equals(moduleName)) {
            reportNo.append(SEPARATOR_MID_BAR_LINE)
                .append(moduleName);
        }
        reportNo.append(SEPARATOR_MID_BAR_LINE)
            .append(strSeriesNo);

        return reportNo.toString();
    }

    /**
     * 根据数字序列号生成新的长度小于等于4位的字符序列号
     *
     * @param numSeriesNo
     * @return
     */
    private String getStrSeriesNo(int numSeriesNo) {
        String result = "";
        String count = "" + numSeriesNo;
        if (count.length() <= 4) {
            for (int i = 0; i < 4 - count.length(); i++) {
                result += "0";
            }
        }
        result += count;
        return result;
    }

    @Override
    public String generateTempReportNo(Long orgId, Long projectId, String process, String ndeType,
                                       String moduleName) {

        final Project project = projectService.get(orgId, projectId);

//        InspectionReportNoManagement management = inspectionReportNoManagementRepository
//            .findByProjectNameAndProcessAndType(project.getName(), process, ndeType);
//        if (management == null) {
//
//            return project.getName()
//                + SEPARATOR_MID_BAR_LINE
//                + process
//                + SEPARATOR_MID_BAR_LINE
//                + "DUMMY";
//        }
//
//        StringBuffer reportNo = new StringBuffer("");
//        reportNo.append(project.getName())
//            .append(SEPARATOR_MID_BAR_LINE)
//            .append(management.getDepartmentNo())
//            .append(SEPARATOR_MID_BAR_LINE)
//            .append(management.getDisciplineNo());
//
        if (moduleName != null
            && !"".equals(moduleName)) {
//            reportNo.append(SEPARATOR_MID_BAR_LINE)
//                .append(moduleName);
        }
//        reportNo.append(SEPARATOR_MID_BAR_LINE)
//            .append("DUMMY");

        return null;// reportNo.toString();
    }

}
