package com.ose.util;

import cn.hutool.json.ObjectMapper;
import com.aliyun.tea.TeaException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.checkerframework.checker.units.qual.K;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 钉钉工具类，用于与钉钉API进行交互，实现员工信息查询、考勤记录查询、请假记录查询等功能。
 */
public class DingTalkUtil {

    private static final String CORP_ID = "ding0a6a9fee1134af19f5bf40eda33b7ba0";// 企业ID
    private static final String GRANT_TYPE = "client_credentials";// 授权类型
    private static final String CLIENT_ID = "dingpor5mmehjnianjd2";// 应用ID
    private static final String CLIENT_SECRET = "I_8l1qpd8UK4ss11ujtJrouOeR60YCqZZAsmQR3PjxNZxByT2JQPY_uG3HhY1qUA";// 应用密钥

    /**
     * 使用 Token 初始化账号Client
     * @return Client 钉钉客户端
     * @throws Exception 如果创建客户端失败
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {// 创建钉钉客户端
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";//protocol表示客户端与钉钉API通信时使用的协议
        config.regionId = "central";//regionId表示客户端所属的区域ID,"central"是一个默认值
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);//返回值可以初始化客户端的配置信息，例如协议和区域ID
    }

    public static String getAccessToken() throws Exception {// 获取访问令牌(AccessToken)，用于身份验证
        com.aliyun.dingtalkoauth2_1_0.Client client = DingTalkUtil.createClient();//用了之前定义的createClient方法，创建了一个钉钉客户端实例client，返回类型是com.aliyun.dingtalkoauth2_1_0.Client
        com.aliyun.dingtalkoauth2_1_0.models.GetTokenRequest getTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetTokenRequest()//GetTokenRequest是阿里云SDK中定义的一个类，用于封装获取AccessToken所需的参数
            .setClientId(CLIENT_ID)//设置客户端ID（clientId），这是钉钉应用的唯一标识。
            .setClientSecret(CLIENT_SECRET)//设置客户端密钥（clientSecret），这是用于验证钉钉应用身份的密钥。
            .setGrantType(GRANT_TYPE);//设置授权类型（grantType），这里使用的是client_credentials，表示使用客户端凭证进行授权。

        return client.getToken(CORP_ID, getTokenRequest).getBody().getAccessToken();
        //这行代码调用钉钉客户端的getToken方法，传入企业ID（CORP_ID）和构造好的请求对象（getTokenRequest），向钉钉API发送请求，获取AccessToken
    }

    /**
     *  通过手机号获取用户ID
     *  16653668681516547
     * @param mobile
     * @return 用户id
     * @throws Exception
     */
    public static String getUserIdByMobile(String mobile) throws Exception {// 通过手机号获取用户ID
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");//DefaultDingTalkClient是钉钉SDK中提供的默认客户端类，用于发送HTTP请求到钉钉API
        OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();//OapiV2UserGetbymobileRequest是钉钉SDK中定义的一个类，用于封装通过手机号获取用户ID所需的参数。
        req.setMobile("15262750197");//设置了请求参数中的手机号为"15262750197",正常应该换为mobile
        OapiV2UserGetbymobileResponse rsp = client.execute(req, getAccessToken());//这行代码调用钉钉客户端的execute方法，发送请求到钉钉API，并获取响应
        return rsp.getResult().getUserid();//这行代码从响应对象rsp中提取用户ID，并将其返回
    }
    /**
     * 获取用户考勤组groupId
     * 940780005
     * @param userId 用户id
     * @return 考勤组id
     * @throws Exception
     */
    public static Long attendanceUserGroup(String userId) throws Exception {// 获取用户考勤组ID
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getusergroup");//"https://oapi.dingtalk.com/topapi/attendance/getusergroup"是钉钉API的URL，用于通过用户ID获取用户的考勤组ID
        OapiAttendanceGetusergroupRequest req = new OapiAttendanceGetusergroupRequest();//OapiAttendanceGetusergroupRequest是钉钉SDK中定义的一个类，用于封装通过用户ID获取考勤组ID所需的参数
        req.setUserid("16653668681516547");//设置了请求参数中的用户ID为"16653668681516547",正常应该换为userId
        OapiAttendanceGetusergroupResponse rsp = client.execute(req, getAccessToken());
        return rsp.getResult().getGroupId();
    }

    /**
     * 将考勤组的groupId转换为groupKey
     * 62AF360A52FA0BD3ED6726EC4C7284AE
     */
//    public static String groupIdToGroupKey(String userId, Long groupId) throws Exception {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/groups/idtokey");
//        OapiAttendanceGroupsIdtokeyRequest req = new OapiAttendanceGroupsIdtokeyRequest();
//        req.setOpUserId("16653668681516547");
//        req.setGroupId(940780005L);
//        OapiAttendanceGroupsIdtokeyResponse rsp = client.execute(req, getAccessToken());
//        return rsp.getResult();
//    }

    /**
     * 根据用户考勤组groupId获取userId,返回值是userId列表
     *@param groupId 考勤组id
     *@return 用户id列表
     *@throws Exception
     */
    public static List<String> getUserIdListByGroupId(Long groupId) throws Exception {// 获取考勤组下的所有用户ID
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/group/memberusers/list");
        OapiAttendanceGroupMemberusersListRequest req = new OapiAttendanceGroupMemberusersListRequest();
        req.setCursor(0L);  //游标值，表示从第几个开始，不传默认从第一个开始。
        req.setGroupId(940780005L);//应该设置为groupId
        OapiAttendanceGroupMemberusersListResponse rsp = client.execute(req, getAccessToken());
        return rsp.getResult().getResult();
    }

    /**
     * 根据用户Id,返回职工号
     * NT_213
     *@param userId 用户id
     *@return 工号
     *@throws Exception
     */
    public static String getJobNumber(String userId) throws Exception {// 根据用户ID获取工号
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");//这个url是用于通过用户ID获取用户信息
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");//表示返回的用户信息使用中文
        OapiV2UserGetResponse rsp = client.execute(req, getAccessToken());
        if (rsp == null || rsp.getResult() == null || rsp.getResult().getJobNumber() == null) {
            // 这里可以根据实际情况进行处理，比如返回默认值或者抛出更有意义的异常
            return null;
        }
        return rsp.getResult().getJobNumber().replace('-','_');
    }

    /**
     * 判断是否还有下一页，获取所有用户Id
     * @param setOffset 当前偏移量
     * @return 下一页的偏移量，如果没有则返回null
     * @throws Exception
     */

    public static Long getAllUserIdList(Long setOffset) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/queryonjob");//这个url用于查询在职员工的用户ID列表。
        OapiSmartworkHrmEmployeeQueryonjobRequest req = new OapiSmartworkHrmEmployeeQueryonjobRequest();//这个类用于封装查询在职员工所需的参数。
        req.setStatusList("2,3,5,-1");//这行代码设置了请求参数中的员工状态列表为"2,3,5,-1"
        req.setOffset(setOffset);//这行代码设置了请求参数中的偏移量为方法参数setOffset，偏移量用于分页查询，表示从第几个记录开始查询。
        req.setSize(50L);//这行代码设置了请求参数中的分页大小为50，每次查询返回的最大记录数为50条。
        OapiSmartworkHrmEmployeeQueryonjobResponse rsp = client.execute(req, getAccessToken());
        return  rsp.getResult().getNextCursor();
    }

    /**
     * 获取所有用户Id列表，这个方法通过循环调用钉钉API，分页查询所有在职员工的用户ID，并将它们收集到一个列表中返回。
     * @return 用户id列表
     * @throws Exception
     */
    public static List<String> getAllEmployeeUserId() throws Exception{
        Long setOffset = 0L;//表示从第一页开始查询。
        boolean flag = true;//表示循环开始时继续执行。
        List<String> employeeList = new ArrayList<>();//employeeList用于存储所有在职员工的用户ID
        while(flag){//循环用于分页查询所有在职员工的用户ID。
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/queryonjob");
            OapiSmartworkHrmEmployeeQueryonjobRequest req = new OapiSmartworkHrmEmployeeQueryonjobRequest();
            req.setStatusList("2,3,5"); //2：试用期 3：正式 5：待离职 -1：无状态
            req.setOffset(setOffset);
            req.setSize(50L);
            OapiSmartworkHrmEmployeeQueryonjobResponse rsp = client.execute(req, getAccessToken());
            setOffset = getAllUserIdList(setOffset);//这行代码调用之前定义的getAllUserIdList方法，获取下一页的偏移量；如果getAllUserIdList方法返回null，表示已经查询到最后一页，没有更多数据。
            employeeList.addAll(rsp.getResult().getDataList());//这行代码将当前页的用户ID列表添加到employeeList中；rsp.getResult().getDataList()：从响应对象中提取用户ID列表。
            if(setOffset == null){//这行代码检查下一页的偏移量是否为null，如果setOffset为null，表示已经查询到最后一页，将flag设置为false，并退出循环。
                flag = false;
                break;
            }
        }
        return employeeList;
    }

    /**
     * 调用钉钉的考勤接口，获取前一天的员工考勤记录 https://open.dingtalk.com/document/orgapp/open-attendance-clock-in-data
     * 这个方法通过分页查询的方式从钉钉API获取考勤数据，并将结果整理后返回。
     * @return 考勤记录列表
     * @throws Exception
     */
    public static List<Map<String, Object>> getDingTalkHourData() throws Exception{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        OapiAttendanceListRequest req = new OapiAttendanceListRequest();
        //设置查询时间范围
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String dateFrom = dateFormat.format(calendar.getTime()) + "00:00:00";
        String dateTo = dateFormat.format(calendar.getTime()) + "23:59:59";
        req.setWorkDateFrom(dateFrom);
        req.setWorkDateTo(dateTo);
        //设置分页参数
        req.setOffset(0L);
        req.setLimit(50L);
        req.setIsI18n(false);
        //获取所有员工userId列表
        List<String> userIdList = getAllEmployeeUserId();
        int chunkSize = 50; // 每次请求的用户ID数量，可根据实际情况调整
        List<OapiAttendanceListResponse.Recordresult> allRecordresult = new ArrayList<>();

        for (int i = 0; i < userIdList.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, userIdList.size());
            List<String> subUserIdList = userIdList.subList(i, endIndex);
            req.setUserIdList(subUserIdList);

            boolean hasMore = true;
            req.setOffset(0L);
            while (hasMore) {
                OapiAttendanceListResponse rsp = client.execute(req, getAccessToken());
                if (rsp!= null && rsp.getErrcode() == 0) {
                    allRecordresult.addAll(rsp.getRecordresult());
                    hasMore = rsp.getHasMore();
                    req.setOffset(req.getOffset() + req.getLimit());
                } else {
                    System.err.println("API请求错误: " + rsp.getErrmsg());
                    hasMore = false;
                }
            }
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Map<String, String>> userAttendanceMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat workDateSdf = new SimpleDateFormat("yyyy-MM-dd");

        for (OapiAttendanceListResponse.Recordresult res : allRecordresult) {
            String userId = res.getUserId();
            String jobNumber = getJobNumber(userId);
            String workHourDate = workDateSdf.format(res.getWorkDate());

            if (!userAttendanceMap.containsKey(userId)) {
                userAttendanceMap.put(userId, new HashMap<>());
                userAttendanceMap.get(userId).put("jobNumber", jobNumber);
                userAttendanceMap.get(userId).put("workHourDate", workHourDate);
            }

            if ("OnDuty".equals(res.getCheckType())) {
                userAttendanceMap.get(userId).put("startCheckTime", res.getUserCheckTime().toString());
            } else if ("OffDuty".equals(res.getCheckType())) {
                userAttendanceMap.get(userId).put("endCheckTime", res.getUserCheckTime().toString());
            }
        }

        for (String userId : userAttendanceMap.keySet()) {
            Map<String, String> attendanceInfo = userAttendanceMap.get(userId);
            String startCheckTime = attendanceInfo.get("startCheckTime");
            String endCheckTime = attendanceInfo.get("endCheckTime");
            long workDuration = 0;

            if (startCheckTime!= null && endCheckTime!= null) {
                try {
                    Date startDate = sdf.parse(startCheckTime);
                    Date endDate = sdf.parse(endCheckTime);
                    workDuration = (endDate.getTime() - startDate.getTime()) / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("jobNumber", attendanceInfo.get("jobNumber"));
            result.put("startCheckTime", startCheckTime);
            result.put("endCheckTime", endCheckTime);
            result.put("workDuration", workDuration);
            result.put("workHourDate", attendanceInfo.get("workHourDate"));
            resultList.add(result);
        }
        return resultList;
    }

    public static List<Map<String, Object>> getDingTalkHourData(String dateFrom, String dateTo) throws Exception{// 获取指定日期的员工考勤记录
        if (!dateFrom.equals(dateTo)) {
            throw new IllegalArgumentException("dateFrom and dateTo must be the same day");
        }
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/list");
        OapiAttendanceListRequest req = new OapiAttendanceListRequest();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        req.setWorkDateFrom(dateFrom + " 00:00:00");
        req.setWorkDateTo(dateTo + " 23:59:59");
        req.setOffset(0L);
        req.setLimit(50L);
        req.setIsI18n(false);

        List<String> userIdList = getAllEmployeeUserId();
        int chunkSize = 50; // 每次请求的用户ID数量，可根据实际情况调整
        List<OapiAttendanceListResponse.Recordresult> allRecordresult = new ArrayList<>();

        for (int i = 0; i < userIdList.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, userIdList.size());
            List<String> subUserIdList = userIdList.subList(i, endIndex);
            req.setUserIdList(subUserIdList);

            boolean hasMore = true;
            req.setOffset(0L);
            while (hasMore) {
                OapiAttendanceListResponse rsp = client.execute(req, getAccessToken());
                if (rsp!= null && rsp.getErrcode() == 0) {
                    allRecordresult.addAll(rsp.getRecordresult());
                    hasMore = rsp.getHasMore();
                    req.setOffset(req.getOffset() + req.getLimit());
                } else {
                    System.err.println("API请求错误: " + rsp.getErrmsg());
                    hasMore = false;
                }
            }
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Map<String, String>> userAttendanceMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat workDateSdf = new SimpleDateFormat("yyyy-MM-dd");

        for (OapiAttendanceListResponse.Recordresult res : allRecordresult) {
            String userId = res.getUserId();
            String jobNumber = getJobNumber(userId);
            String workHourDate = workDateSdf.format(res.getWorkDate());

            if (!userAttendanceMap.containsKey(userId)) {
                userAttendanceMap.put(userId, new HashMap<>());
                userAttendanceMap.get(userId).put("jobNumber", jobNumber);
                userAttendanceMap.get(userId).put("workHourDate", workHourDate);
            }

            if ("OnDuty".equals(res.getCheckType())) {
                userAttendanceMap.get(userId).put("startCheckTime", res.getUserCheckTime().toString());
            } else if ("OffDuty".equals(res.getCheckType())) {
                userAttendanceMap.get(userId).put("endCheckTime", res.getUserCheckTime().toString());
            }
        }

        for (String userId : userAttendanceMap.keySet()) {
            Map<String, String> attendanceInfo = userAttendanceMap.get(userId);
            String startCheckTime = attendanceInfo.get("startCheckTime");
            String endCheckTime = attendanceInfo.get("endCheckTime");
            long workDuration = 0;

            if (startCheckTime!= null && endCheckTime!= null) {
                try {
                    Date startDate = sdf.parse(startCheckTime);
                    Date endDate = sdf.parse(endCheckTime);
                    workDuration = (endDate.getTime() - startDate.getTime()) / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("jobNumber", attendanceInfo.get("jobNumber"));
            result.put("startCheckTime", startCheckTime);
            result.put("endCheckTime", endCheckTime);
            result.put("workDuration", workDuration);
            result.put("workHourDate", attendanceInfo.get("workHourDate"));
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 用钉钉的考勤接口，获取时间范围内的员工考勤记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     * @throws Exception
     */
    public static List<Map<String, Object>> getDingTalkHourDataForRange(String startDate, String endDate) throws Exception {
        List<Map<String, Object>> allResults = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (calendar.getTime().before(end) || calendar.getTime().equals(end)) {
            String currentDate = dateFormat.format(calendar.getTime());
            List<Map<String, Object>> dailyResult = getDingTalkHourData(currentDate, currentDate);
            allResults.addAll(dailyResult);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return allResults;
    }

    /**
     * 调用钉钉的假勤审批接口，获取前一天的员工请假记录 https://open.dingtalk.com/document/orgapp/query-status
     * @return
     * @throws Exception
     */
    public  static List<Map<String, Object>> getDingTalkLeaveData() throws Exception{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");
        List<Map<String, Object>> allLeaveData = new ArrayList<>();

        List<String> userIdList = getAllEmployeeUserId();
        int maxUserIdsPerRequest = 100;
        for (int i = 0; i < userIdList.size(); i += maxUserIdsPerRequest) {
            int endIndex = Math.min(i + maxUserIdsPerRequest, userIdList.size());
            List<String> subUserIdList = userIdList.subList(i, endIndex);

            OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
            req.setUseridList(String.join(",", subUserIdList));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                String dateFrom = sdf.format(calendar.getTime());
                String dateTo = sdf.format(calendar.getTime());
                Date startDate = sdf.parse(dateFrom);
                long startTime = startDate.getTime();
                req.setStartTime(startTime);
                Date endDate = sdf.parse(dateTo);
                long endTime = endDate.getTime();
                req.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            req.setOffset(0L);
            Long pageSize = 20L;
            req.setSize(pageSize);

            boolean hasMore = true;
            while (hasMore) {
                try {
                    OapiAttendanceGetleavestatusResponse rsp = client.execute(req, getAccessToken());
                    if (rsp!= null && rsp.getErrcode() == 0) {
                        List<OapiAttendanceGetleavestatusResponse.LeaveStatusVO> leaveStatusList = rsp.getResult().getLeaveStatus();
                        if (leaveStatusList!= null) {
                            for (OapiAttendanceGetleavestatusResponse.LeaveStatusVO leaveStatus : leaveStatusList) {
                                Map<String, Object> leaveData = new HashMap<>();
                                String jobNumber = getJobNumber(leaveStatus.getUserid());
                                leaveData.put("jobNumber", jobNumber);
                                leaveData.put("userid", leaveStatus.getUserid());
                                SimpleDateFormat sdfLeave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                leaveData.put("startLeaveTime", sdfLeave.format(leaveStatus.getStartTime()));
                                leaveData.put("endLeaveTime", sdfLeave.format(leaveStatus.getEndTime()));
                                leaveData.put("durationUnit", leaveStatus.getDurationUnit());
                                if (leaveStatus.getDurationUnit().equals("percent_day")){
                                    Date startTime = new Date(leaveStatus.getStartTime());
                                    Date endTime = new Date(leaveStatus.getEndTime());
                                    // 计算时间差
                                    long diffInMillis = endTime.getTime() - startTime.getTime();
                                    double diffInHours = diffInMillis / (60 * 60 * 1000);
                                    if (diffInHours > 8){
                                        leaveData.put("durationPercent", (leaveStatus.getDurationPercent() / 100.0) * 8);
                                    } else {
                                        leaveData.put("durationPercent", diffInHours);
                                    }
                                } else {
                                    leaveData.put("durationPercent", leaveStatus.getDurationPercent() / 100.0);
                                }

                                if(leaveStatus.getLeaveCode().equals("bbca4a30-cb94-4868-91f8-88aadae63421")){
                                    leaveData.put("leaveCode", "Personal 事假");
                                } else if (leaveStatus.getLeaveCode().equals("931e2c3a-b60f-48e7-bd3e-2a30ab792366")){
                                    leaveData.put("leaveCode", "Sick 病假");
                                } else if (leaveStatus.getLeaveCode().equals("954aaf82-d600-4598-a22d-0a88044806f2")){
                                    leaveData.put("leaveCode", "Annual 年假");
                                } else if (leaveStatus.getLeaveCode().equals("2169967c-e3a4-4b05-b523-45892f149302")){
                                    leaveData.put("leaveCode", "Maternity 产假");
                                } else if (leaveStatus.getLeaveCode().equals("ed164c33-80a2-4972-8a8b-eb38a622eb7d")){
                                    leaveData.put("leaveCode", "Childcare Leave 育儿假");
                                } else if (leaveStatus.getLeaveCode().equals("7815e5d0-2cf8-4a2f-9394-1d0d60fcfd34")){
                                    leaveData.put("leaveCode", "Paternity Leave 陪产假");
                                } else if (leaveStatus.getLeaveCode().equals("12c9cec9-5561-49ad-ad60-13d2764291d4")){
                                    leaveData.put("leaveCode", "Marriage Leave 婚假");
                                } else if (leaveStatus.getLeaveCode().equals("73413788-7d16-474d-a286-b596532d126b")){
                                    leaveData.put("leaveCode", "Prenatal Check-up Leave 产检假");
                                } else if (leaveStatus.getLeaveCode().equals("33784db8-3313-4db0-92a9-f79990149fb1")){
                                    leaveData.put("leaveCode", "Miscarriage Leave 小产假");
                                } else if (leaveStatus.getLeaveCode().equals("9dd3b5ac-5276-4a70-bf93-9d56b53978aa")){
                                    leaveData.put("leaveCode", "Funeral Leave 丧假");
                                } else if (leaveStatus.getLeaveCode().equals("9a48b105-fe74-41e5-a6a6-6e8a4f07e889")){
                                    leaveData.put("leaveCode", "Breastfeeding Leave 哺乳假");
                                } else if (leaveStatus.getLeaveCode().equals("3da5cdd5-afb4-47b1-9f84-cf6661e6a387")){
                                    leaveData.put("leaveCode", "Parental Nursing Leave 父母护理");
                                }
                                allLeaveData.add(leaveData);
                            }
                        }
                        hasMore = rsp.getResult().getHasMore();
                        req.setOffset(req.getOffset() + pageSize);
                    } else {
                        System.err.println("API请求错误: " + rsp.getErrmsg());
                        hasMore = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hasMore = false;
                }
            }
        }
        return allLeaveData;
    }

    /**
     * 调用钉钉的假勤审批接口，获取时间范围内的员工请假记录
     * @param startLeaveDate
     * @param endLeaveDate
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getDingTalkLeaveData(String startLeaveDate, String endLeaveDate) throws Exception{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavestatus");
        List<Map<String, Object>> allLeaveData = new ArrayList<>();

//        List<String> userIdList = Arrays.asList("16653668681516547");
        List<String> userIdList = getAllEmployeeUserId();
        int maxUserIdsPerRequest = 100;
        for (int i = 0; i < userIdList.size(); i += maxUserIdsPerRequest) {
            int endIndex = Math.min(i + maxUserIdsPerRequest, userIdList.size());
            List<String> subUserIdList = userIdList.subList(i, endIndex);

            OapiAttendanceGetleavestatusRequest req = new OapiAttendanceGetleavestatusRequest();
            req.setUseridList(String.join(",", subUserIdList));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = sdf.parse(startLeaveDate);
                long startTime = startDate.getTime();
                req.setStartTime(startTime);
                Date endDate = sdf.parse(endLeaveDate);
                long endTime = endDate.getTime();
                req.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            req.setOffset(0L);
            Long pageSize = 20L;
            req.setSize(pageSize);

            boolean hasMore = true;
            while (hasMore) {
                try {
                    OapiAttendanceGetleavestatusResponse rsp = client.execute(req, getAccessToken());
                    if (rsp!= null && rsp.getErrcode() == 0) {
                        List<OapiAttendanceGetleavestatusResponse.LeaveStatusVO> leaveStatusList = rsp.getResult().getLeaveStatus();
                        if (leaveStatusList!= null) {
                            for (OapiAttendanceGetleavestatusResponse.LeaveStatusVO leaveStatus : leaveStatusList) {
                                Map<String, Object> leaveData = new HashMap<>();
                                String jobNumber = getJobNumber(leaveStatus.getUserid());
                                leaveData.put("jobNumber", jobNumber);
                                leaveData.put("userid", leaveStatus.getUserid());
                                SimpleDateFormat sdfLeave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                leaveData.put("startLeaveTime", sdfLeave.format(leaveStatus.getStartTime()));
                                leaveData.put("endLeaveTime", sdfLeave.format(leaveStatus.getEndTime()));
                                leaveData.put("durationUnit", leaveStatus.getDurationUnit());

                                if (leaveStatus.getDurationUnit().equals("percent_day")){
                                    // 将 long 类型的时间戳转换为 Date 对象
                                    Date startTime = new Date(leaveStatus.getStartTime());
                                    Date endTime = new Date(leaveStatus.getEndTime());
                                    // 计算时间差
                                    long diffInMillis = endTime.getTime() - startTime.getTime();
                                    double diffInHours = diffInMillis / (60 * 60 * 1000);
                                    if (diffInHours > 8){
                                        leaveData.put("durationPercent", (leaveStatus.getDurationPercent() / 100.0) * 8);
                                    } else {
                                        leaveData.put("durationPercent", diffInHours);
                                    }
                                } else {
                                    leaveData.put("durationPercent", leaveStatus.getDurationPercent() / 100.0);
                                }

                                if(leaveStatus.getLeaveCode().equals("bbca4a30-cb94-4868-91f8-88aadae63421")){
                                    leaveData.put("leaveCode", "Personal 事假");
                                } else if (leaveStatus.getLeaveCode().equals("931e2c3a-b60f-48e7-bd3e-2a30ab792366")){
                                    leaveData.put("leaveCode", "Sick 病假");
                                } else if (leaveStatus.getLeaveCode().equals("954aaf82-d600-4598-a22d-0a88044806f2")){
                                    leaveData.put("leaveCode", "Annual 年假");
                                } else if (leaveStatus.getLeaveCode().equals("2169967c-e3a4-4b05-b523-45892f149302")){
                                    leaveData.put("leaveCode", "Maternity 产假");
                                } else if (leaveStatus.getLeaveCode().equals("ed164c33-80a2-4972-8a8b-eb38a622eb7d")){
                                    leaveData.put("leaveCode", "Childcare Leave 育儿假");
                                } else if (leaveStatus.getLeaveCode().equals("7815e5d0-2cf8-4a2f-9394-1d0d60fcfd34")){
                                    leaveData.put("leaveCode", "Paternity Leave 陪产假");
                                } else if (leaveStatus.getLeaveCode().equals("12c9cec9-5561-49ad-ad60-13d2764291d4")){
                                    leaveData.put("leaveCode", "Marriage Leave 婚假");
                                } else if (leaveStatus.getLeaveCode().equals("73413788-7d16-474d-a286-b596532d126b")){
                                    leaveData.put("leaveCode", "Prenatal Check-up Leave 产检假");
                                } else if (leaveStatus.getLeaveCode().equals("33784db8-3313-4db0-92a9-f79990149fb1")){
                                    leaveData.put("leaveCode", "Miscarriage Leave 小产假");
                                } else if (leaveStatus.getLeaveCode().equals("9dd3b5ac-5276-4a70-bf93-9d56b53978aa")){
                                    leaveData.put("leaveCode", "Funeral Leave 丧假");
                                } else if (leaveStatus.getLeaveCode().equals("9a48b105-fe74-41e5-a6a6-6e8a4f07e889")){
                                    leaveData.put("leaveCode", "Breastfeeding Leave 哺乳假");
                                } else if (leaveStatus.getLeaveCode().equals("3da5cdd5-afb4-47b1-9f84-cf6661e6a387")){
                                    leaveData.put("leaveCode", "Parental Nursing Leave 父母护理");
                                }
                                allLeaveData.add(leaveData);
                            }
                        }
                        hasMore = rsp.getResult().getHasMore();
                        req.setOffset(req.getOffset() + pageSize);
                    } else {
                        System.err.println("API请求错误: " + rsp.getErrmsg());
                        hasMore = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hasMore = false;
                }
            }
        }
        return allLeaveData;
    }

    /**
     * 调用钉钉的花名册接口,获取员工花名册字段信息 https://open.dingtalk.com/document/isvapp/intelligent-personnel-obtain-employee-roster-information
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getDingTalkEmployeeData() throws Exception{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/v2/list");
//        List<String> allEmployeeIds = Arrays.asList("16653668681516547");
        List<String> allEmployeeIds = getAllEmployeeUserId();
        int batchSize = 100;
        List<Map<String, Object>> finalResultList = new ArrayList<>();

        for (int i = 0; i < allEmployeeIds.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, allEmployeeIds.size());
            List<String> batchIds = allEmployeeIds.subList(i, endIndex);

            OapiSmartworkHrmEmployeeV2ListRequest req = new OapiSmartworkHrmEmployeeV2ListRequest();
            req.setUseridList(String.join(",", batchIds));
            //花名册自定义字段业务code:姓名、工号、入职时间、员工状态、转正日期、试用期
            req.setFieldFilterList("sys00-name, sys00-jobNumber, sys00-confirmJoinTime, sys01-employeeStatus," +
                                   " sys01-regularTime, sys01-probationPeriodType");
            req.setAgentid(3334845477L);

            try {
                OapiSmartworkHrmEmployeeV2ListResponse rsp = client.execute(req, getAccessToken());
                if (rsp.isSuccess()) {
                    List<OapiSmartworkHrmEmployeeV2ListResponse.EmpRosterFieldVo> resultList = rsp.getResult();
                    for (OapiSmartworkHrmEmployeeV2ListResponse.EmpRosterFieldVo empRosterFieldVo : resultList) {
                        Map<String, Object> dataMap = new HashMap<>();
                        List<OapiSmartworkHrmEmployeeV2ListResponse.EmpFieldDataVo> fieldDataList = empRosterFieldVo.getFieldDataList();
                        for (OapiSmartworkHrmEmployeeV2ListResponse.EmpFieldDataVo fieldData : fieldDataList) {
                            String fieldName = fieldData.getFieldName();
                            List<OapiSmartworkHrmEmployeeV2ListResponse.FieldValueVo> fieldValueList = fieldData.getFieldValueList();
                            if (fieldValueList.size() > 0) {
                                dataMap.put(fieldName, fieldValueList.get(0).getValue());
                            }
                        }
                        finalResultList.add(dataMap);
                    }
                } else {
                    System.out.println("请求失败，错误信息: " + rsp.getErrmsg());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
        // 打印整理后的数据
        return finalResultList;
    }


    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkcontact_1_0.Client createContactClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }


    // 计算员工入职时当年度的年假
    public static double calculateAnnualLeaveOnHire(String hireDateStr, double serviceYears) {
        LocalDate currentDate = LocalDate.now();
        LocalDate hireDate = LocalDate.parse(hireDateStr);
        long yearsWorkedInCompany = ChronoUnit.YEARS.between(hireDate, currentDate);
        // 入职首年的年假计算
        if (yearsWorkedInCompany == 0) {
            int annualLeaveHours = 0;
            if (serviceYears >= 1 && serviceYears < 10) {
                annualLeaveHours = 40;
            } else if (serviceYears >= 10 && serviceYears < 20) {
                annualLeaveHours = 80;
            } else if (serviceYears >= 20) {
                annualLeaveHours = 120;
            }
            LocalDate endOfYear = LocalDate.of(hireDate.getYear(), 12, 31);
            long daysRemaining = ChronoUnit.DAYS.between(hireDate, endOfYear) + 1;
            double annualLeaveResultHours = ((double) daysRemaining / 365) * annualLeaveHours;
            return Math.floor(annualLeaveResultHours);
        } else {
            int annualLeaveHours = 0;
            if (serviceYears >= 1 && serviceYears < 10) {
                annualLeaveHours = 40;
            } else if (serviceYears >= 10 && serviceYears < 20) {
                annualLeaveHours = 80;
            } else if (serviceYears >= 20) {
                annualLeaveHours = 120;
            }
            return annualLeaveHours;
        }

    }

    // 计算试用期员工的每月年假
    public static double calculateProbationMonthlyAnnualLeave(String hireDateStr, double serviceYears, LocalDate currentDate) {
        LocalDate hireDate = LocalDate.parse(hireDateStr);
        // 获取入职年份
        int hireYear = hireDate.getYear();
        // 获取当前年份
        int currentYear = currentDate.getYear();

        if (currentYear == hireYear) {
            // 入职当年的计算逻辑
            double annualLeaveHours = calculateAnnualLeaveOnHire(hireDateStr, serviceYears);
            LocalDate endOfYear = LocalDate.of(hireYear, 12, 31);
            long monthsAfterHire = ChronoUnit.MONTHS.between(hireDate, endOfYear) + 1;
            double monthlyLeaveHours = Math.floor(annualLeaveHours / monthsAfterHire * 2) / 2; // 舍去不足半小时的部分
            return monthlyLeaveHours;
        } else {
            // 下一年度仍在试用期的计算逻辑
            double annualLeaveHours = calculateAnnualLeaveOnHire(hireDateStr, serviceYears);
            double rawMonthlyLeave = annualLeaveHours / 12;
            double integerPart = Math.floor(rawMonthlyLeave); // 整数部分
            double decimalPart = rawMonthlyLeave - integerPart; // 小数部分
            if (decimalPart > 0.5) {
                return integerPart + 0.5;
            } else {
                return integerPart;
            }
        }
    }

    // 计算员工离职时当年度的年假
    public static double calculateAnnualLeaveOnLeave(String leaveDateStr, double serviceYears) {
        int annualLeaveHours = 0;
        if (serviceYears >= 1 && serviceYears < 10) {
            annualLeaveHours = 40;
        } else if (serviceYears >= 10 && serviceYears < 20) {
            annualLeaveHours = 80;
        } else if (serviceYears >= 20) {
            annualLeaveHours = 120;
        }
        LocalDate leaveDate = LocalDate.parse(leaveDateStr);
        LocalDate startOfYear = LocalDate.of(leaveDate.getYear(), 1, 1);
        long daysPassed = ChronoUnit.DAYS.between(startOfYear, leaveDate) + 1;
        double annualLeaveResultHours = ((double) daysPassed / 365) * annualLeaveHours;
        return Math.floor(annualLeaveResultHours);
    }

    // 计算工龄
    public static double calculateLengthOfCareer(String firstWorkDateStr) {
        LocalDate firstWorkDate = LocalDate.parse(firstWorkDateStr);
        LocalDate currentDate = LocalDate.now();
        long daysWorked = ChronoUnit.DAYS.between(firstWorkDate, currentDate);
        double yearsWorked = (double) daysWorked / 365.0;
        return Math.round(yearsWorked * 10.0) / 10.0;
    }

}


