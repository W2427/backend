package com.ose.test.api;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SPM 放行单 接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface SpmReleaseNoteAPI extends SpmReleaseNoteFeignAPI {

}
