package com.ose.tasks.controller.setting;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.setting.DisciplineAPI;
import com.ose.tasks.domain.model.service.setting.DisciplineInterface;
import com.ose.tasks.dto.discipline.DisciplineCodeDTO;
import com.ose.tasks.dto.setting.DiscCreateDTO;
import com.ose.tasks.dto.setting.DiscCriteriaDTO;
import com.ose.tasks.dto.setting.DiscUpdateDTO;
import com.ose.tasks.entity.setting.Discipline;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "专业代码参照表")
@RestController
public class DisciplineController extends BaseController implements DisciplineAPI {

    private ApplicationContext applicationContext;

    private final DisciplineInterface disciplineService;

    @Autowired
    public DisciplineController(ApplicationContext applicationContext,
                                DisciplineInterface disciplineService) {
        this.applicationContext = applicationContext;
        this.disciplineService = disciplineService;
    }

    @Override
    public JsonResponseBody create(Long orgId, Long projectId, DiscCreateDTO discCreateDTO) {
        return null;
    }

    /**
     * 获取DISCIPLINE列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param discCriteriaDTO 查询条件
     * @return DISCIPLINE列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/disciplines",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<Discipline> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DiscCriteriaDTO discCriteriaDTO
    ) {
        List<Discipline> disciplines = disciplineService.getList(projectId);
        return new JsonListResponseBody<>(disciplines);

    }

    @Override
    public JsonObjectResponseBody<Discipline> get(Long orgId, Long projectId, Long disciplineId) {
        return null;
    }

    @Override
    public JsonResponseBody update(Long orgId, Long projectId, Long disciplineId, DiscUpdateDTO itpUpdateDTO) {
        return null;
    }

    @Override
    public JsonResponseBody delete(Long orgId, Long projectId, Long disciplineId) {
        return null;
    }

    /**
     * 取得枚举类型的专业代码列表。
     */
    @RequestMapping(
        method = GET,
        value = "/discipline-codes",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "取得系统业务代码大类型")
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<DisciplineCodeDTO> list() {

//        List<Discipline> disciplines = disciplineService.getList(proje);
//        String[] disciplineCodes = new String[]{"abc"};
//        List<DisciplineCodeDTO> disciplineCodeDTOS = new ArrayList<>();
//        for (String disciplineCode : disciplineCodes) {
//            DisciplineCodeDTO disciplineCodeDTO = new DisciplineCodeDTO();
//            disciplineCodeDTO.setDisciplineCode(disciplineCode);
//            disciplineCodeDTO.setDescription(disciplineCode);
//            disciplineCodeDTOS.add(disciplineCodeDTO);
//        }
        return null;// new JsonListResponseBody<>(disciplineCodeDTOS);
    }

    @RequestMapping(
        method = GET,
        value = "/bean-list",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<DisciplineCodeDTO> beanList() {
        Arrays.asList(applicationContext.getBeanDefinitionNames());
        return new JsonListResponseBody<>();
    }

}
