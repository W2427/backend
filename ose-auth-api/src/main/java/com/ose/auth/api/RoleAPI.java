package com.ose.auth.api;

import com.ose.auth.dto.RoleCriteriaDTO;
import com.ose.auth.entity.Role;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 角色接口信息。
 */
public interface RoleAPI extends RoleFeignAPI {

    /**
     * 获取角色列表。
     *
     * @param organizationId  组织ID
     * @param roleCriteriaDTO 查询参数
     * @param page            分页参数
     * @return 角色列表
     */
    @RequestMapping(
        value = "/roles",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Role> search(Long organizationId, RoleCriteriaDTO roleCriteriaDTO, PageDTO page);
}
