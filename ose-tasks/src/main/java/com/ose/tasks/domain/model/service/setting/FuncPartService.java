package com.ose.tasks.domain.model.service.setting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.setting.FuncPartRepository;
import com.ose.tasks.entity.setting.FuncPart;
import com.ose.util.StringUtils;
import com.ose.vo.RedisKey;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Func Part 专业服务。
 */
@Component
public class FuncPartService extends StringRedisService implements FuncPartInterface {

    private final FuncPartRepository funcPartReposiroty;

    /**
     * 构造方法。
     *
     * @param stringRedisTemplate  Redis 模板
     * @param funcPartReposiroty
     */
    public FuncPartService(StringRedisTemplate stringRedisTemplate,
                           FuncPartRepository funcPartReposiroty) {
        super(stringRedisTemplate);
        this.funcPartReposiroty = funcPartReposiroty;
    }

    @Override
    public List<FuncPart> getList(Long projectId) {

        String funcPartKey = String.format(RedisKey.PROJECT_FUNC_PART.getDisplayName(), projectId.toString());
        String funcPartStr = getRedisKey(funcPartKey);

        if(StringUtils.isEmpty(funcPartStr) || "[]".equals(funcPartStr) ) {
            List<FuncPart> funcParts = new ArrayList<>();
            funcParts = funcPartReposiroty.findByProjectId(projectId);
            if(funcParts != null){
                funcPartStr = StringUtils.toJSON(funcParts);
                setRedisKey(funcPartKey, funcPartStr);
            }
            return funcParts;

        } else {
            return StringUtils.decode(funcPartStr, new TypeReference<List<FuncPart>>() {});
        }
    }

    public FuncPart getByname(Long projectId,String name) {
        String funcPartKey = String.format(RedisKey.PROJECT_FUNC_PART_KEY.getDisplayName(), projectId.toString(), name);
        String funcPartStr = getRedisKey(funcPartKey);

        FuncPart funcPart;
        if(funcPartStr == null || funcPartStr.equalsIgnoreCase("null")) {
            funcPart = funcPartReposiroty.findByProjectIdAndNameEn(
                projectId, name
            );
            if(funcPart != null) {
                setRedisKey(funcPartKey, StringUtils.toJSON(funcPart));
            }

        } else {
            funcPart = StringUtils.decode(funcPartStr, new TypeReference<FuncPart>() {});
        }
        return funcPart;
    }
}
