package com.ose.tasks.domain.model.service.setting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.setting.DisciplineRepository;
import com.ose.tasks.entity.setting.Discipline;
import com.ose.util.StringUtils;
import com.ose.vo.RedisKey;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DISCIPLINE 专业服务。
 */
@Component
public class DisciplineService extends StringRedisService implements DisciplineInterface {

    private final DisciplineRepository disciplineRepository;

    /**
     * 构造方法。
     *
     * @param stringRedisTemplate  Redis 模板
     * @param disciplineRepository
     */
    public DisciplineService(StringRedisTemplate stringRedisTemplate,
                             DisciplineRepository disciplineRepository) {
        super(stringRedisTemplate);
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    public List<Discipline> getList(Long projectId) {

        String disciplineKey = String.format(RedisKey.PROJECT_DISCIPLINE.getDisplayName(), projectId.toString());
        String disciplineStr = getRedisKey(disciplineKey);

        if(StringUtils.isEmpty(disciplineStr) || "[]".equals(disciplineStr) ) {
            List<Discipline> disciplines = new ArrayList<>();
            disciplines = disciplineRepository.findByProjectId(projectId);
            if(disciplines != null){
                disciplineStr = StringUtils.toJSON(disciplines);
                setRedisKey(disciplineKey, disciplineStr);
            }
            return disciplines;

        } else {
            return StringUtils.decode(disciplineStr, new TypeReference<List<Discipline>>() {});
        }
    }
}
