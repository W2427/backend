package com.ose.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ose.annotation.InternalAccessOnly;
import com.ose.annotation.WithSuperPrivilege;
import com.ose.aspect.RequestCountAspect;
import com.ose.constant.JsonFormatPattern;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 控制器基类。
 */
@Tag(name = "服务管理接口")
@RestController
public class ServerController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
    // 应用上下文
    private static ConfigurableApplicationContext context = null;

    /**
     * 设置应用上下文。
     *
     * @param applicationContext 应用上下文
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 退出 Spring 应用（关闭服务）。
     */
    private static void exit() {

        if (context == null) {
            return;
        }

        try {
            System.exit(SpringApplication.exit(context, (ExitCodeGenerator) () -> 0));
        } catch (Exception e) {
            e.printStackTrace(System.out);
            logger.error("ServerController throw exception ", e);
            try {
                context.close();
            } catch (Exception e2) {
                e2.printStackTrace(System.out);
                logger.error("ServerController throw exception ", e2);
            }
            System.exit(0);
        }

    }

    /**
     * 暂停服务。
     */
    @Operation(
        summary = "暂停服务",
        description = "将当前服务状态更新为【暂停】，不再接受客户端请求（新的请求将被拒绝，并返回【503 Service Unavailable】错误）。"
    )
    @RequestMapping(method = POST, value = "/server/suspend")
    @InternalAccessOnly
    @WithSuperPrivilege
    public JsonResponseBody suspend() {
        RequestCountAspect.suspend();
        return new JsonResponseBody();
    }

    /**
     * 关闭服务。
     */
    @Operation(
        summary = "关闭服务",
        description = "将当前服务状态更新为【准备关闭】，不再接受客户端请求（新的请求将被拒绝，并返回【503 Service Unavailable】错误）。当所有处理中的请求完成响应后关闭当前服务。"
    )
    @RequestMapping(method = POST, value = "/server/shutdown")
    @InternalAccessOnly
    @WithSuperPrivilege
    public JsonResponseBody shutdown(
        @RequestParam(required = false, defaultValue = "false")
        @Parameter(description = "是否强制关闭，默认为否（将等待所有客户端请求处理完成）")
            Boolean force
    ) {
        if (force) {
            exit();
        } else {
            RequestCountAspect.close(ServerController::exit);
        }
        return new JsonResponseBody();
    }

    /**
     * 恢复当前服务。
     */
    @Operation(description = "恢复当前服务")
    @RequestMapping(method = POST, value = "/server/restore")
    @InternalAccessOnly
    @WithSuperPrivilege
    public JsonResponseBody restore() {
        RequestCountAspect.restore();
        return new JsonResponseBody();
    }

    /**
     * 查看当前服务状态。
     */
    @Operation(description = "查看当前服务状态")
    @RequestMapping(method = GET, value = "/server/status")
    public JsonObjectResponseBody<ServerStatusDTO> status() {
        return new JsonObjectResponseBody<>(new ServerStatusDTO(
            getContext(),
            RequestCountAspect.isSuspended(),
            RequestCountAspect.isClosing(),
            RequestCountAspect.getRequestCount()
        ));
    }

    /**
     * 服务状态数据传输对象。
     */
    @SuppressWarnings({"WeakerAccess", "CheckStyle", "unused"})
    public static class ServerStatusDTO extends BaseDTO {

        private static final long serialVersionUID = -8022795503265274212L;

        @Schema(description = "客户端远程 IP 地址")
        private String remoteAddr;

        @Schema(description = "客户端用户代理字符串")
        private String userAgent;

        @Schema(description = "是否被暂停")
        private Boolean suspended;

        @Schema(description = "是否准备关闭")
        private Boolean closing;

        @Schema(description = "处理中的请求数量")
        private long requestCount;

        @Schema(description = "服务器时间")
        @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = JsonFormatPattern.ISO_DATE
        )
        private Date serverTime;

        public ServerStatusDTO() {
            setServerTime(new Date());
        }

        public ServerStatusDTO(
            final ContextDTO context,
            final Boolean suspended,
            final Boolean closing,
            final long requestCount
        ) {
            this();
            setRemoteAddr(context.getRemoteAddr());
            setUserAgent(context.getUserAgent());
            setSuspended(suspended);
            setClosing(closing);
            setRequestCount(requestCount);
        }

        public String getRemoteAddr() {
            return remoteAddr;
        }

        public void setRemoteAddr(String remoteAddr) {
            this.remoteAddr = remoteAddr;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public Boolean getSuspended() {
            return suspended;
        }

        public void setSuspended(Boolean suspended) {
            this.suspended = suspended;
        }

        public Boolean getClosing() {
            return closing;
        }

        public void setClosing(Boolean closing) {
            this.closing = closing;
        }

        public long getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(long requestCount) {
            this.requestCount = requestCount;
        }

        public Date getServerTime() {
            return serverTime;
        }

        public void setServerTime(Date serverTime) {
            this.serverTime = serverTime;
        }
    }

}
