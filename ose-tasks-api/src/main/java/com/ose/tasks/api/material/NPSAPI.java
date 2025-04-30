package com.ose.tasks.api.material;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.NPSCHDTO;
import com.ose.tasks.dto.material.NPSCriteriaDTO;
import com.ose.tasks.dto.material.NPSDTO;
import com.ose.tasks.dto.material.NPSThicknessSearchDTO;
import com.ose.tasks.entity.material.NPS;
import com.ose.tasks.entity.material.NPSch;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/nps/")
public interface NPSAPI {

    /**
     * 创建
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<NPS> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody NPSDTO dto
    );

    /**
     * 修改
     */
    @RequestMapping(method = POST, value = "/{id}")
    JsonObjectResponseBody<NPS> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody NPSDTO dto
    );

    /**
     * 获取列表
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<NPS> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        NPSCriteriaDTO criteriaDTO,
        PageDTO page
    );

    /**
     * 获取详情
     */
    @RequestMapping(method = GET, value = "/{id}")
    JsonObjectResponseBody<NPS> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    /**
     * 删除
     */
    @RequestMapping(method = DELETE, value = "/{id}", produces = APPLICATION_JSON_VALUE)
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 创建npsch
     */
    @RequestMapping(method = POST, value = "/{npsId}/npsch")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<NPSch> createNPSCH(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("npsId") Long npsId,
        @RequestBody NPSCHDTO dto
    );

    /**
     * 修改npsch
     */
    @RequestMapping(method = POST, value = "/{npsId}/npsch/{id}")
    JsonObjectResponseBody<NPSch> editNPSCH(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("npsId") Long npsId,
        @PathVariable("id") Long id,
        @RequestBody NPSCHDTO dto
    );

    /**
     * 获取npsch列表
     */
    @RequestMapping(method = GET, value = "/{npsId}/npsch")
    @ResponseStatus(OK)
    JsonListResponseBody<NPSch> listNPSCH(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("npsId") Long npsId,
        PageDTO page
    );

    /**
     * 获取npsch详情
     */
    @RequestMapping(method = GET, value = "/{npsId}/npsch/{id}")
    JsonObjectResponseBody<NPSch> detailNPSCH(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("npsId") Long npsId,
        @PathVariable("id") Long id
    );


    /**
     * 删除npsch
     */
    @RequestMapping(method = DELETE, value = "/{npsId}/npsch/{id}", produces = APPLICATION_JSON_VALUE)
    JsonResponseBody deleteNPSCH(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("npsId") Long npsId,
        @PathVariable("id") Long id
    );

    /**
     * 查询壁厚值
     */
    @RequestMapping(method = GET, value = "/thickness")
    JsonObjectResponseBody<NPSch> searchThickness(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        NPSThicknessSearchDTO searchDTO
    );
}
