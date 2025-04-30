package com.ose.tasks.domain.model.service.bpm;

import java.util.Map;
import java.util.Set;

/**
 * service接口
 */
public interface EntityTypeProcessRelationInterface {


    Set<String> getProcessFuncParts(Long id);

    Map<String, Long> getProcessFuncPartMap(Long id);

}
