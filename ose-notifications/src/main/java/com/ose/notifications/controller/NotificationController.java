package com.ose.notifications.controller;

import com.ose.annotation.InternalAccessOnly;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.dto.TeamPrivilegeDTO;
import com.ose.auth.dto.TeamPrivilegeListDTO;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.notifications.api.NotificationAPI;
import com.ose.notifications.domain.model.service.NotificationConfigurationInterface;
import com.ose.notifications.domain.model.service.NotificationInterface;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.dto.NotificationPostDTO;
import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.dto.receiver.TeamReceiverDTO;
import com.ose.notifications.dto.receiver.UserReceiverDTO;
import com.ose.notifications.entity.*;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "通知管理接口")
@RestController
public class NotificationController extends BaseController implements NotificationAPI {

    // 通知配置接口
    private final NotificationConfigurationInterface configurationService;

    // 通知服务接口
    private final NotificationInterface notificationService;

    // 用户接口
    private final UserFeignAPI userFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationController(
        NotificationConfigurationInterface notificationConfigurationService,
        NotificationInterface notificationService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UserFeignAPI userFeignAPI
    ) {
        this.configurationService = notificationConfigurationService;
        this.notificationService = notificationService;
        this.userFeignAPI = userFeignAPI;
    }

    @Operation(
        summary = "发送通知",
        description = "该接口仅限内网访问，如要手动发送通知，需要通过其他服务提供接口。"
    )
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notifications",
        produces = APPLICATION_JSON_VALUE
    )
    @InternalAccessOnly
    @WithPrivilege(required = false)
    public JsonObjectResponseBody<NotificationBatch> send(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(name = "type") @Parameter(name = "通知类型", required = true) NotificationType notificationType,
        @RequestBody @Valid NotificationPostDTO notificationDTO
    ) {

        final BatchGetDTO userBatchGetDTO = new BatchGetDTO();
        final Map<Long, UserReceiverDTO> userMap = new HashMap<>();
        final List<TeamPrivilegeDTO> teamList = new ArrayList<>();

        NotificationConfiguration configuration = configurationService
            .get(orgId, projectId, notificationType);

        if (configuration == null) {
            throw new BusinessError("配置信息不存在"); // TODO
        }

        if (notificationDTO.getUsers() != null) {
            for (UserReceiverDTO user : notificationDTO.getUsers()) {
                userBatchGetDTO.addEntityId(user.getUserId());
                userMap.put(user.getUserId(), user);
            }
        }

        if (notificationDTO.getTeams() != null) {
            for (TeamReceiverDTO team : notificationDTO.getTeams()) {
                if (team.getMemberPrivileges() == null) {
                    team.setMemberPrivileges(configuration.getMemberPrivilegeSet());
                }
                teamList.add(new TeamPrivilegeDTO(team));
            }
        }

        final List<UserReceiverDTO> users = new ArrayList<>();

        // 取得用户信息
        if (userBatchGetDTO.getEntityIDs().size() > 0) {
            userFeignAPI
                .batchGet(userBatchGetDTO)
                .getData()
                .forEach(user -> users.add(userMap.remove(user.getId()).setUser(user)));
        }

        // 取得工作组成员信息
        if (teamList.size() > 0) {
            userFeignAPI
                .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
                .getData()
                .forEach(user -> users.add(new UserReceiverDTO(user)));
        }

        NotificationBatch batch = null;

        // 生成通知记录，并开始发送通知
        if (users.size() > 0) {

            batch = notificationService.saveBatchLogs(
                getContext().getOperator(),
                orgId,
                projectId,
                notificationType,
                notificationDTO.getParameters(),
                users
            );

            notificationService.sendAll(orgId);

        }

        return new JsonObjectResponseBody<>(batch);
    }

    @Operation(description = "查询通知批次")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonListResponseBody<NotificationBatchSummary> batches(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(name = "type", required = false) @Parameter(description = "通知类型") NotificationType notificationType,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            notificationService
                .batches(orgId, projectId, notificationType, pageDTO.toPageable())
        );
    }

    @Operation(description = "取得通知批次详细")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches/{batchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationBatch> batch(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "批次 ID") Long batchId
    ) {
        return new JsonObjectResponseBody<>(
            notificationService.getBatch(orgId, projectId, batchId)
        );
    }

    @Operation(description = "重新发送发送失败的通知")
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches/{batchId}/resend",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody resend(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "批次 ID") Long batchId
    ) {
        notificationService.resend(orgId, projectId, batchId);
        return new JsonResponseBody();
    }

    @Operation(description = "取得通知详细信息")
    @Override
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/notifications/{notificationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<Notification> get(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @PathVariable @Parameter(description = "通知 ID") Long notificationId
    ) {
        return new JsonObjectResponseBody<>(notificationService.get(userId, notificationId));
    }

    @Operation(description = "将通知设置为已读")
    @Override
    @RequestMapping(
        method = PUT,
        value = "/users/{userId}/notifications/{notificationId}/read",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody setAsRead(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @PathVariable @Parameter(description = "通知 ID") Long notificationId
    ) {
        notificationService.setAsRead(userId, notificationId);
        return new JsonResponseBody();
    }

    @Operation(description = "将通知设置为未读")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/users/{userId}/notifications/{notificationId}/read",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody setAsUnread(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @PathVariable @Parameter(description = "通知 ID") Long notificationId
    ) {
        notificationService.setAsUnread(userId, notificationId);
        return new JsonResponseBody();
    }

    @Operation(description = "查询用户的通知")
    @Override
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/notifications",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserNotification> list(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        @RequestParam(required = false) @Parameter(description = "组织 ID（可选）") Long orgId,
        @RequestParam(required = false) @Parameter(description = "项目 ID（可选）") Long projectId,
        @RequestParam(name = "type", required = false) @Parameter(description = "通知类型（可选）") NotificationType notificationType,
        @RequestParam(name = "read", required = false) @Parameter(description = "是否已读（可选）") Boolean readByUser,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(notificationService.search(
            userId, orgId, projectId, notificationType, readByUser, pageDTO.toPageable()
        ));
    }

    @Operation(description = "查询通知信息")
    @Override
    @RequestMapping(
        method = GET ,
        value = "/messages",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<Notification> list(
        NotificationSearchDTO notificationSearchDTO) {

        return new JsonListResponseBody<>(notificationService.search(notificationSearchDTO));
    }

    @Operation(description = "创建消息通知")
    @Override
    @RequestMapping(
        method = POST,
        value = "/messages",
        produces = APPLICATION_JSON_VALUE
    )
//    @WithPrivilege
    public JsonResponseBody create(
        @RequestBody NotificationCreateDTO notificationCreateDTO
    ) {
//        ContextDTO contextDTO = getContext();
        notificationService.create(
//            contextDTO.getOperator().getId(),
//            contextDTO.getOperator().getName(),
            notificationCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 修改消息通知。
     *
     *
     * @param messageId
     * @param notificationUpdateDTO
     * @return
     */
    @Operation(description = "修改消息通知")
    @Override
    @RequestMapping(
        method = PATCH,
        value = "/messages/{messageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "消息通知Id") Long messageId,
        @RequestBody NotificationCreateDTO notificationUpdateDTO
    ) {
        ContextDTO contextDTO = getContext();
        notificationService.update(
            messageId,
            contextDTO.getOperator().getName(),
            contextDTO.getOperator().getId(),
            notificationUpdateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 修改消息状态。
     *
     *
     * @param messageId
     * @param notificationUpdateDTO
     * @return
     */
    @Operation(description = "修改消息状态")
    @Override
    @RequestMapping(
        method = POST,
        value = "/messages/{messageId}/update-status",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateStatus(
        @PathVariable @Parameter(description = "消息通知Id") Long messageId,
        @RequestBody NotificationCreateDTO notificationUpdateDTO
    ) {
        ContextDTO contextDTO = getContext();
        notificationService.updateStatus(
            messageId,
            notificationUpdateDTO
        );

        return new JsonResponseBody();
    }

    @Operation(description = "将通知设置为未读")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/delete-all",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody deleteAll() {
        notificationService.deleteAll();
        return new JsonResponseBody();
    }

}
