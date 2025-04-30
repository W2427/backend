package com.ose.tasks.domain.model.service.bpm;

import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.EntitySubTypeProcessRelationRepository;
import com.ose.tasks.entity.bpm.BpmEntityTypeProcessRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EntityTypeProcessRelationService extends StringRedisService implements EntityTypeProcessRelationInterface {

    private final EntitySubTypeProcessRelationRepository entitySubTypeProcessRelationRepository;




    /**
     * 构造方法
     */
    @Autowired
    public EntityTypeProcessRelationService(StringRedisTemplate stringRedisTemplate,
                                            EntitySubTypeProcessRelationRepository entitySubTypeProcessRelationRepository) {
        super(stringRedisTemplate);
        this.entitySubTypeProcessRelationRepository = entitySubTypeProcessRelationRepository;
    }


    @Override
    public Set<String> getProcessFuncParts(Long projectId) {
        return getProcessFuncPartMap(projectId).keySet();
    }

    @Override
    public Map<String, Long> getProcessFuncPartMap(Long projectId) {
        //("process + funcPart" -> processId
        Set<BpmEntityTypeProcessRelation> etprs = entitySubTypeProcessRelationRepository.findProcessFuncParts(projectId);
        Map<String, Long> processFuncPartMap = new HashMap<>();
        etprs.forEach(etpr ->{
            processFuncPartMap.put(etpr.getProcess().getNameEn()+"-"+etpr.getFuncPart(), etpr.getProcess().getId());
        });

        return processFuncPartMap;
    }
}
