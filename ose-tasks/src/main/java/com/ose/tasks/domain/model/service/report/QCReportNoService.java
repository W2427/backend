package com.ose.tasks.domain.model.service.report;

import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportTypeList;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.vo.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class QCReportNoService extends StringRedisService implements QCReportNoInterface {


    private static final String REPORT_NO_AT_REDIS_KEY = "REPORT_NO:%s:%s";

    private final QCReportRepository qcReportRepository;


    /**
     * 构造方法。
     *
     * @param stringRedisTemplate Redis 模板
     * @param qcReportRepository
     */
    @Autowired
    public QCReportNoService(StringRedisTemplate stringRedisTemplate, QCReportRepository qcReportRepository) {
        super(stringRedisTemplate);
        this.qcReportRepository = qcReportRepository;
    }


    @Override
    public synchronized String getReportSn(Long orgId, Long projectId) {


        String redisKey = String.format(REPORT_NO_AT_REDIS_KEY,orgId.toString(), projectId.toString());
        String reportSn = getRedisKey(redisKey);

        if(StringUtils.isEmpty(reportSn)) {
            String maxSerialNo = qcReportRepository.findMaxSn(projectId);
            if (StringUtils.isEmpty(maxSerialNo)) {
                reportSn = "10001";
            } else {
                reportSn = String.valueOf(Integer.valueOf(maxSerialNo)+1);
            }
        } else {
            reportSn = String.valueOf(Integer.valueOf(reportSn)+1);
        }
        setRedisKey(redisKey, reportSn);

        return reportSn;
    }

    @Override
    public synchronized String seriesNo(Long orgId,
                                        Long projectId,
                                        ReportSubType type,
                                        String moduleName,
                                        Integer n,
                                        String projectName,
                                        int retryTimes) {
        List<QCReport> qcReportList = new ArrayList<>();

        String sn;

        if (ReportTypeList.typeList.contains(type.toString())) {
            Set<String> skipedSns = smembers(String.format(RedisKey.REPORT_SN_SKIPPED.getDisplayName(), projectId.toString(), type.toString(), moduleName));
            if(CollectionUtils.isEmpty(skipedSns) || retryTimes > 0) {
                sn = getRedisKey(String.format(RedisKey.REPORT_SN_MAX.getDisplayName(), projectId.toString(), type.toString(), moduleName));
                if(retryTimes > 0 && sn != null) {
                    sn = String.valueOf(Integer.parseInt(sn) + retryTimes);
                    setRedisKey(String.format(RedisKey.REPORT_SN_MAX.getDisplayName(), projectId.toString(), type.toString(), moduleName), sn, 7200);
                } else if(sn == null) {
                    qcReportList = qcReportRepository.findByProjectIdAndReportSubTypeAndModuleNameAndReportStatusNot(projectId, type, moduleName, ReportStatus.CANCEL);
                    sn = initReportSn(qcReportList, String.format(RedisKey.REPORT_SN_SKIPPED.getDisplayName(), projectId.toString(), type.toString(), moduleName),
                        String.format(RedisKey.REPORT_SN_MAX.getDisplayName(), projectId.toString(), type.toString(), moduleName), retryTimes);
                } else {
                    sn = String.valueOf(Integer.parseInt(sn) + 1);
                    setRedisKey(String.format(RedisKey.REPORT_SN_MAX.getDisplayName(), projectId.toString(), type.toString(), moduleName), sn, 7200);
                }
            } else {
                sn = skipedSns.iterator().next();
                srem(String.format(RedisKey.REPORT_SN_SKIPPED.getDisplayName(), projectId.toString(), type.toString(), moduleName), sn);
            }
        } else {
            Set<String> skipedSns = smembers(String.format(RedisKey.REPORT_SN_GENERAL_SKIPPED.getDisplayName(), projectId.toString(), type.toString()));
            if(CollectionUtils.isEmpty(skipedSns) || retryTimes > 0) {
                sn = getRedisKey(String.format(RedisKey.REPORT_SN_GENERAL_MAX.getDisplayName(), projectId.toString(), type.toString()));
                if(retryTimes > 0 && sn != null) {
                    sn = String.valueOf(Integer.parseInt(sn) + retryTimes);
                    setRedisKey(String.format(RedisKey.REPORT_SN_GENERAL_MAX.getDisplayName(), projectId.toString(), type.toString()), sn, 7200);

                } else if (sn == null) {
                    qcReportList = qcReportRepository.findByProjectIdAndReportSubTypeAndReportStatusNot(projectId, type, ReportStatus.CANCEL);
                    sn = initReportSn(qcReportList, String.format(RedisKey.REPORT_SN_GENERAL_SKIPPED.getDisplayName(), projectId.toString(), type.toString()),
                        String.format(RedisKey.REPORT_SN_GENERAL_MAX.getDisplayName(), projectId.toString(), type.toString()), retryTimes);
                } else {
                    sn = String.valueOf(Integer.parseInt(sn) + 1);
                    setRedisKey(String.format(RedisKey.REPORT_SN_GENERAL_MAX.getDisplayName(), projectId.toString(), type.toString()), sn, 7200);
                }
            }  else {
                sn = skipedSns.iterator().next();
                srem(String.format(RedisKey.REPORT_SN_GENERAL_SKIPPED.getDisplayName(), projectId.toString(), type.toString()), sn);
            }

        }


        String result = "";
        String count = "" + sn;
        if (count.length() <= n) {
            for (int i = 0; i < n - count.length(); i++) {
                result += "0";
            }
        }
        result += count;

        return result;
    }

    private String initReportSn(List<QCReport> qcReportList, String redisKeySkipped, String redisKeyMax, int retryTimes) {
        Integer seriesNo = 1;
        boolean isSeriesNoSet = false;
        Integer sn = 0;
        List<Integer> reportList = new ArrayList<>();
        for (QCReport qr : qcReportList) {
            reportList.add(qr.getSeriesNum());
        }
        Collections.sort(reportList);
        List<Integer> newReportList = reportList.stream().distinct().collect(Collectors.toList());

        if (newReportList == null || newReportList.isEmpty()) {
            seriesNo = 1 + retryTimes;
            setRedisKey(redisKeyMax, seriesNo.toString(), 7200);
        } else {
            int max = newReportList.stream().reduce(Integer::max).get();
            int size = newReportList.size();
            if (max != newReportList.size()) {
                for (int i = 1; i < 10000 && i <= size; i++) {
                    sn++;
                    if (newReportList.get(i - 1).equals(sn)) {
                        continue;
                    }

                    seriesNo = newReportList.get(i - 1);

                    for(int j = sn; j < newReportList.get(i - 1); j++) {
                        if(!seriesNo.equals(sn)) {
                            sadd(redisKeySkipped, sn.toString());
                        }
                        sn++;
                    }

                }
            }
            seriesNo = max + 1 + retryTimes;
            setRedisKey(redisKeyMax, seriesNo.toString(), 7200);
        }
        return seriesNo.toString();
    }

    @Override
    public synchronized String reportNo(String operatorName,
                           ReportSubType type,
                           String moduleName,
                           String result,
                           String projectName,
                           ReportConfig reportConfig) {
        String redisKey = null;
        if (ReportTypeList.typeList.contains(type.toString())) {
            redisKey = String.format(RedisKey.REPORT_SN.getDisplayName(),projectName, type, moduleName, result);
        } else {
            redisKey = String.format(RedisKey.REPORT_SN_GENERAL.getDisplayName(),projectName, type, result);
        }

        if (reportConfig.getReportSubType().equals(ReportSubType.STRUCTURE_Phase_Array_Ultrasonic)) {
            type = ReportSubType.STRUCTURE_Phase_Array_Ultrasonic;
        }

        setRedisKey(redisKey, result, 1800);

        String companyName = "";
        if (operatorName.equals("孙志超")) {
            companyName = "NK";
        } else if (operatorName.equals("韩少军")) {
            companyName = "TR";
        } else if (operatorName.equals("伍宏伟")) {
            companyName = "JT";
        } else if (operatorName.equals("李子豪")) {
            companyName = "NK-TU01";
        } else if (operatorName.equals("周岩")) {
            companyName = "SZHY";
        } else if (operatorName.equals("常家宇")) {
            companyName = "SF";
        } else {
            companyName = "";
        }
        Map<String, String> newModuleName = new HashMap<String, String>();
        newModuleName.put("2TMR1", "TMR001");
        newModuleName.put("2TMR2", "TMR002");
        newModuleName.put("2TMR3", "TMR003");
        newModuleName.put("2TMR4", "TMR004");
        newModuleName.put("2BLM1", "BLM001");
        newModuleName.put("2BLM2", "BLM002");
        newModuleName.put("2BLM3", "BLM003");
        newModuleName.put("2BLM4", "BLM004");
        newModuleName.put("2BLM5", "BLM005");
        newModuleName.put("2BLM7", "BLM007");
        newModuleName.put("2BLM8", "BLM008");
        newModuleName.put("3TMR1", "TMR001");
        newModuleName.put("3TMR2", "TMR002");
        newModuleName.put("3TMR3", "TMR003");
        newModuleName.put("3TMR4", "TMR004");
        newModuleName.put("3BLM1", "BLM001");
        newModuleName.put("3BLM2", "BLM002");
        newModuleName.put("3BLM3", "BLM003");
        newModuleName.put("3BLM4", "BLM004");
        newModuleName.put("3BLM5", "BLM005");
        newModuleName.put("3BLM7", "BLM007");
        newModuleName.put("3BLM8", "BLM008");
        newModuleName.put("1TMR1", "TMR001");
        newModuleName.put("1TMR2", "TMR002");
        newModuleName.put("1TMR3", "TMR003");
        newModuleName.put("1TMR4", "TMR004");
        newModuleName.put("1BLM1", "BLM001");
        newModuleName.put("1BLM2", "BLM002");
        newModuleName.put("1BLM3", "BLM003");
        newModuleName.put("1BLM4", "BLM004");
        newModuleName.put("1BLM5", "BLM005");
        newModuleName.put("1BLM7", "BLM007");
        newModuleName.put("1BLM8", "BLM008");

        Map<String, String> blmName = new HashMap<String, String>();
        blmName.put("2TMR1", "TMR001");
        blmName.put("2TMR2", "TMR002");
        blmName.put("2TMR3", "TMR003");
        blmName.put("2TMR4", "TMR004");
        blmName.put("2BLM1", "TMR001");
        blmName.put("2BLM2", "TMR002");
        blmName.put("2BLM3", "TMR003");
        blmName.put("2BLM4", "TMR004");
        blmName.put("2BLM5", "TMR004");
        blmName.put("2BLM7", "TMR003");
        blmName.put("2BLM8", "TMR001");
        blmName.put("3TMR1", "TMR001");
        blmName.put("3TMR2", "TMR002");
        blmName.put("3TMR3", "TMR003");
        blmName.put("3TMR4", "TMR004");
        blmName.put("3BLM1", "TMR001");
        blmName.put("3BLM2", "TMR002");
        blmName.put("3BLM3", "TMR003");
        blmName.put("3BLM4", "TMR004");
        blmName.put("3BLM5", "TMR004");
        blmName.put("3BLM7", "TMR003");
        blmName.put("3BLM8", "TMR001");
        blmName.put("1TMR1", "TMR001");
        blmName.put("1TMR2", "TMR002");
        blmName.put("1TMR3", "TMR003");
        blmName.put("1TMR4", "TMR004");
        blmName.put("1BLM1", "TMR001");
        blmName.put("1BLM2", "TMR002");
        blmName.put("1BLM3", "TMR003");
        blmName.put("1BLM4", "TMR004");
        blmName.put("1BLM5", "TMR004");
        blmName.put("1BLM7", "TMR003");
        blmName.put("1BLM8", "TMR001");
        String name = newModuleName.get(moduleName);
        String blmNewName = blmName.get(moduleName);
        String reportNo = "";
        switch (type.toString()) {
            case "STRUCTURE_Radio_Graphic":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050102-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + name + "-08-050102-TR-" + result;
                        }
                    }
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050102-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + name + "-08-050102-TR-" + result;
                        }
                    }
                } else {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050102-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + name + "-08-050102-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + name + "-08-050102-TR-" + result;
                        }
                    }
                }
                break;
            case "STRUCTURE_Phase_Array_Ultrasonic":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050105-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + name + "-08-050105-TR-" + result;
                        }
                    }
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050105-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + name + "-08-050105-TR-" + result;
                        }
                    }
                } else {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050105-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + name + "-08-050105-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + name + "-08-050105-TR-" + result;
                        }
                    }
                }
                break;
            case "STRUCTURE_Ultrasonic":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + blmNewName + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050101-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + name + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + name + "-08-050101-TR-" + result;
                        }
                    }
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050101-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + name + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + name + "-08-050101-TR-" + result;
                        }
                    }
                } else {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050101-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + name + "-08-050101-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + name + "-08-050101-TR-" + result;
                        }
                    }
                }

                break;
            case "STRUCTURE_Magnetic_Particle":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050103-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + name + "-08-050103-TR-" + result;
                        }
                    }
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050103-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + name + "-08-050103-TR-" + result;
                        }
                    }
                } else {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050103-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + name + "-08-050103-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + name + "-08-050103-TR-" + result;
                        }
                    }
                }

                break;
            case "STRUCTURE_Penetration":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + blmNewName + "-" + name + "-08-050104-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS2-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS2-" + name + "-08-050104-TR-" + result;
                        }
                    }
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + blmNewName + "-" + name + "-08-050104-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS1-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS1-" + name + "-08-050104-TR-" + result;
                        }
                    }
                } else {
                    if (moduleName.contains("BLM")) {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + blmNewName + "-" + name + "-08-050104-TR-" + result;
                        }
                    } else {
                        if (!companyName.equals("")) {
                            reportNo = "F217-GBS3-" + name + "-08-050104-" + companyName + "-" + result;
                        } else {
                            reportNo = "F217-GBS3-" + name + "-08-050104-TR-" + result;
                        }
                    }
                }

                break;
            case "PIPING_MATERIAL":
                reportNo = "F217-0000-08-0302-PP-" + result;
                break;
            case "PIPING_FIT_UP":
                reportNo = "F217-" + moduleName + "-08-0302-PP-" + result;
                break;
            case "PIPING_Radio_Graphic":
                reportNo = moduleName + "-050202" + "-N" + result;
                break;
            case "PIPING_Ultrasonic":
                reportNo = moduleName + "-050201" + "-N" + result;
                break;
            case "PIPING_Magnetic_Particle":
                reportNo = moduleName + "-050203" + "-N" + result;
                break;
            case "PIPING_Penetration":
                reportNo = moduleName + "-050204" + "-N" + result;
                break;
            case "STRUCTURE_FIT_UP":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    reportNo = "F217-GBS2-" + moduleName + "-08-050301-" + result;
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    reportNo = "F217-GBS1-" + moduleName + "-08-050301-" + result;
                } else {
                    reportNo = "F217-GBS3-" + moduleName + "-08-050301-" + result;
                }

                break;
            case "STRUCTURE_WELD":
                if (reportConfig.getProjectId().equals(1595407955858982139L) || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    reportNo = "F217-GBS2-" + moduleName + "-08-050302-" + result;
                } else if (reportConfig.getProjectId().equals(1616469247194450532L)) {
                    reportNo = "F217-GBS1-" + moduleName + "-08-050302-" + result;
                } else {
                    reportNo = "F217-GBS3-" + moduleName + "-08-050302-" + result;
                }

                break;
            case "STRUCTURE_NT_F253_FIT_UP":
                reportNo = "F253-" + moduleName + "-11-0503-01-" + result;
                break;
            case "PIPE_NT_F253_MATERIAL":
                reportNo = "F253-11-0302-01-" + result;
                break;
            case "PIPE_NT_F253_FITTING":
                reportNo = "F253-11-0302-03-" + result;
                break;
            case "VALVE_NT_F253":
                reportNo = "F253-11-0302-04-" + result;
                break;
            case "STRUCTURE_NT_F253_WELD":
                reportNo = "F253-" + moduleName + "-11-0503-02-" + result;
                break;
            case "PIPING_NT_F253_WELD":
                reportNo = "F253-" + moduleName + "-11-0504-02-" + result;
                break;

            case "STRUCTURE_NT_F253_Radio_Graphic":
                reportNo = "F253-" + moduleName + "-11-0501-02-" + result;
                break;
            case "STRUCTURE_NT_F253_Ultrasonic":
                reportNo = "F253-" + moduleName + "-11-0501-01-" + result;
                break;
            case "STRUCTURE_NT_F253_Magnetic_Particle":
                reportNo = "F253-" + moduleName + "-11-0501-03-" + result;
                break;
            case "STRUCTURE_NT_F253_Penetrant":
                reportNo = "F253-" + moduleName + "-11-0501-04-" + result;
				break;
            case "PIPING_NT_F253_FIT_UP":
                reportNo = "F253-" + moduleName + "-11-0504-01-" + result;
                break;
            case "PIPING_NT_F253_Magnetic_Particle":
                reportNo = "F253" + moduleName + "11-0502-03-" + result;
                break;
            case "PIPING_NT_F253_Radio_Graphic":
                reportNo = "F253" + moduleName + "11-0502-02-" + result;
                break;
            case "PIPING_NT_F253_Ultrasonic":
                reportNo = "F253" + moduleName + "11-0502-01-" + result;
                break;
            case "PIPING_NT_F253_Penetrant":
                reportNo = "F253" + moduleName + "11-0502-04-" + result;
                break;
            default:
                reportNo = projectName + "-" + reportConfig.getReportSubType().toString() + "-" + result;
        }

        return reportNo;
    }
}
