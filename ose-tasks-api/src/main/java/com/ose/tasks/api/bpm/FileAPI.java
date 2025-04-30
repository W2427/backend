package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.bpm.TaskTemporaryFileDTO;

/**
 * 文件操作接口。
 */
public interface FileAPI {

    /**
     * 上传文档文件。
     */
    @RequestMapping(method = POST, value = "models/zip-file", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TaskTemporaryFileDTO> upload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam MultipartFile file
    );

}
