package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.SacsUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.SacsUploadHistory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface SacsAPI {

    /**
     * 上传历史记录
     */
    @RequestMapping(method = GET, value = "sacs-upload-histories")
    @ResponseStatus(OK)
    JsonListResponseBody<SacsUploadHistory> externalInspectionUploadHistories(
        @PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId, SacsUploadHistorySearchDTO pageDTO);

    /**
     * 上传报告文件
     */
    @RequestMapping(method = POST, value = "upload-sacs")
    JsonObjectResponseBody<BpmExInspConfirmResponseDTO> uploadSacs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    ) throws FileNotFoundException;
}
