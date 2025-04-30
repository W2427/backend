package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.util.LongUtils;
import com.ose.util.RemoteAddressUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 用户访问令牌内容数据传输对象。
 * <p>
 * 用户访问令牌内容格式：
 * 第 1 行 - 访问令牌 ID
 * 第 2 行 - 用户 ID
 * 第 3 行 - 用户类型
 * 第 4 行 - 用户 LOGO
 * 第 5 行 - 用户姓名
 * 第 6 行 - 用户信息最后更新时间（三十六进制毫秒）
 * 第 7 行 - 登录远程 IP 地址（十六进制）
 * 第 8 行 - 客户端用户代理字符串 ID
 */
public class AccessTokenDTO extends BaseDTO {

    private static final long serialVersionUID = 1489198181330070691L;

    // 用户访问令牌装载数据中各字段之间的分隔符
    private static final String SEPARATOR = "\n";

    @Schema(description = "用户访问令牌在数据库中的 ID")
    private Long id;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "用户类型")
    private String type;

    @Schema(description = "用户头像")
    private String logo;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户信息最后更新时间")
    private Date lastModifiedAt;

    @Schema(description = "初次登录时的 IP 地址")
    private String remoteAddr;

    @Schema(description = "初次登录时的用户代理字符串")
    private Long userAgentId;

    /**
     * 构造方法。
     *
     * @param accessTokenId 访问令牌 ID
     * @param userId        用户 ID
     * @param remoteAddr    远程 IP 地址
     * @param userAgentId   用户代理字符串
     */
    public AccessTokenDTO(
        Long accessTokenId,
        Long userId,
        String remoteAddr,
        Long userAgentId
    ) {
        this.id = accessTokenId;
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.userAgentId = userAgentId;
    }

    /**
     * 构造函数。
     *
     * @param payload JWT 装载的数据
     */
    public AccessTokenDTO(String payload) {

        String[] fields = payload.split(SEPARATOR);

        if (fields.length != 8) {
            throw new AccessTokenInvalidError();
        }

        this.id = LongUtils.parseLong(fields[0]);
        this.userId = LongUtils.parseLong(fields[1]);
        this.type = fields[2];
        this.logo = fields[3];
        this.name = fields[4];
        this.lastModifiedAt = new Date(Long.parseLong(fields[5], 36));
        this.remoteAddr = RemoteAddressUtils.fromHex(fields[6]);
        this.userAgentId = LongUtils.parseLong(fields[7]);
    }

    /**
     * 转为 JWT 中装载的数据。
     *
     * @return JWT 中装载的数据
     */
    @Override
    public String toString() {
        return (new StringBuilder())
            .append(this.id)
            .append(SEPARATOR)
            .append(this.userId)
            .append(SEPARATOR)
            .append(this.type)
            .append(SEPARATOR)
            .append(StringUtils.trim(this.logo))
            .append(SEPARATOR)
            .append(StringUtils.trim(this.name))
            .append(SEPARATOR)
            .append(Long.toString(this.lastModifiedAt.getTime(), 36))
            .append(SEPARATOR)
            .append(RemoteAddressUtils.toHex(this.remoteAddr))
            .append(SEPARATOR)
            .append(this.userAgentId)
            .toString();
    }

    /**
     * 取得用户访问令牌在数据库中的 ID。
     *
     * @return 用户访问令牌在数据库中的 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 取得用户 ID。
     *
     * @return 用户 ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 取得用户类型。
     *
     * @return 用户类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置用户类型。
     *
     * @param type 用户类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 取得用户头像。
     *
     * @return 用户头像
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置用户头像。
     *
     * @param logo 用户头像
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 取得用户姓名。
     *
     * @return 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名。
     *
     * @param name 用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 取得用户信息最后更新时间。
     *
     * @return 用户信息最后更新时间
     */
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * 设置用户信息最后更新时间。
     *
     * @param lastModifiedAt 用户信息最后更新时间
     */
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    /**
     * 取得初次登录时的 IP 地址。
     *
     * @return 初次登录时的 IP 地址
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * 取得初次登录时的用户代理字符串。
     *
     * @return 初次登录时的用户代理字符串
     */
    public Long getUserAgentId() {
        return userAgentId;
    }

}
