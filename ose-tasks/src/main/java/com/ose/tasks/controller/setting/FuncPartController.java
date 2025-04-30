package com.ose.tasks.controller.setting;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.setting.FuncPartAPI;
import com.ose.tasks.domain.model.service.setting.FuncPartInterface;
import com.ose.tasks.dto.funcPart.FuncPartDTO;
import com.ose.tasks.dto.setting.FuncPartCreateDTO;
import com.ose.tasks.dto.setting.FuncPartCriteriaDTO;
import com.ose.tasks.dto.setting.FuncPartUpdateDTO;
import com.ose.tasks.entity.setting.FuncPart;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "功能分块参照表")
@RestController
public class FuncPartController extends BaseController implements FuncPartAPI {


    private ApplicationContext applicationContext;

    private final FuncPartInterface funcPartService;

    @Autowired
    public FuncPartController(ApplicationContext applicationContext,
                              FuncPartInterface funcPartService) {
        this.applicationContext = applicationContext;
        this.funcPartService = funcPartService;
    }

    @Override
    public JsonResponseBody create(Long orgId, Long projectId, FuncPartCreateDTO funcPartCreateDTO) {
        return null;
    }

    @Override
    public JsonListResponseBody<FuncPart> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        FuncPartCriteriaDTO funcPartCriteriaDTO
    ) {
        List<FuncPart> funcParts = funcPartService.getList(projectId);
        return new JsonListResponseBody<>(funcParts);
    }

    @Override
    public JsonObjectResponseBody<FuncPart> get(Long orgId, Long projectId, Long funcPartId) {
        return null;
    }

    @Override
    public JsonResponseBody update(Long orgId, Long projectId, Long funcPartId, FuncPartUpdateDTO funcPartUpdateDTO) {
        return null;
    }

    @Override
    public JsonResponseBody delete(Long orgId, Long projectId, Long funcPartId) {
        return null;
    }

    /**
     * 取得枚举类型的专业代码列表。
     */
    @RequestMapping(
        method = GET,
        value = "/func-parts",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(description = "取得系统业务代码大类型")
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<FuncPartDTO> list() {
        String[] funcParts = new String[]{"abc"};
//        String[] funcParts = FuncPart.values();
        List<FuncPartDTO> funcPartDTOS = new ArrayList<>();
        for (String funcPart : funcParts) {
            FuncPartDTO funcPartDTO = new FuncPartDTO();
            funcPartDTO.setFuncPart(funcPart);
            funcPartDTO.setDescription(funcPart);
            funcPartDTOS.add(funcPartDTO);
        }
        return new JsonListResponseBody<>(funcPartDTOS);
    }

    /**
     * 获取FUNC PART详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param funcPart     FUNC PART
     * @return FUNC PART详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/func-parts/func-part",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<FuncPart> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("funcPart") String funcPart
    ){
        return new JsonObjectResponseBody<>(
            funcPartService.getByname(projectId, funcPart)
        );
    }

}
