package com.ose.test.controller;

import com.ose.controller.BaseController;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.test.domain.model.repository.ColumnEntityRepository;
import com.ose.test.domain.model.repository.PostRepository;
import com.ose.test.domain.model.service.ColumnEntityInterface;
import com.ose.test.entity.Post;
import com.ose.test.entity.Test01Entity;
import com.ose.test.entity.TestDTO;
import com.ose.test.entity.TestEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "Result")
@RestController
public class ResultController extends BaseController {

    private final ColumnEntityRepository columnEntityRepository;

    private final ColumnEntityInterface columnEntityService;

    private final PostRepository postRepository;

    @Autowired
    public ResultController(
        ColumnEntityRepository columnEntityRepository,
        ColumnEntityInterface columnEntityService,
        PostRepository postRepository) {
        this.columnEntityRepository = columnEntityRepository;
        this.columnEntityService = columnEntityService;
        this.postRepository = postRepository;
    }

    @Operation(description = "DEMO")
    @RequestMapping(
        method = POST,
        value = "/test"
    )
    public JsonResponseBody test(@RequestBody TestDTO testDTO
    ) {
        System.out.println("OK");

        //1.0 对比 老的列清单 和 新清单不一致的列
        System.out.println(testDTO.getTest());


        return new JsonResponseBody();
    }

    @Operation(description = "DEMO")
    @RequestMapping(
        method = GET,
        value = "/result"
    )
    public JsonResponseBody getList(
    ) {
        System.out.println("OK");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.checkColumn();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE")
    @RequestMapping(
        method = GET,
        value = "/handle"
    )
    public JsonResponseBody handle(
    ) {
        System.out.println("HANDLE");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.handle();



        return new JsonResponseBody();
    }

    @Operation(description = "DEL TABLE")
    @RequestMapping(
        method = GET,
        value = "/del_table"
    )
    public JsonResponseBody delTable(
    ) {
        System.out.println("DEL_TABLE");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.delTable();



        return new JsonResponseBody();
    }

    @Operation(description = "DEL column")
    @RequestMapping(
        method = GET,
        value = "/del_column"
    )
    public JsonResponseBody delColumn(
    ) {
        System.out.println("DEL_TABLE");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.delColumn();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE DUPLICATE")
    @RequestMapping(
        method = GET,
        value = "/handle-duplicate"
    )
    public JsonResponseBody handleDuplicate(
    ) {
        System.out.println("HANDLE DUPLICATE");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.handleDuplicate();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE RELATED FIELD")
    @RequestMapping(
        method = GET,
        value = "/handle-related"
    )
    public JsonResponseBody handleRelated(
    ) {
        System.out.println("HANDLE RELATED FIELD");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.handleRelatedField();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE PATH FIELD")
    @RequestMapping(
        method = GET,
        value = "/handle-path"
    )
    public JsonResponseBody handlePath(
    ) {
        System.out.println("HANDLE RELATED FIELD");

//        5.1 取得一个 path 字段值，转成数组，
//        5.2 到duplicateKey表中去查找 oldValue，如果存在进行替换。
//        5.3 将每个值 转换为 bigint，
//        5.4 拼接为/的字符串。保存到数据库
        columnEntityService.handlePath();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE bpm assignee_ tenant_")
    @RequestMapping(
        method = GET,
        value = "/handle-bpm-field"
    )
    public JsonResponseBody handleBpmField(
    ) {
        System.out.println("HANDLE RELATED FIELD");

        //1.0 对比 老的列清单 和 新清单不一致的列
        columnEntityService.handleBpmField();



        return new JsonResponseBody();
    }

    @Operation(description = "HANDLE test")
    @RequestMapping(
        method = GET,
        value = "/handle-test"
    )
    public JsonObjectResponseBody<TestEntity> handleTest(
    ) {
        System.out.println("HANDLE Test");

        //1.0 对比 老的列清单 和 新清单不一致的列
        List<Test01Entity> ids = new ArrayList<>();

        List<String> idds = new ArrayList<>();
        idds = columnEntityRepository.getIds("wps");

        Test01Entity test01Entity = new Test01Entity();
        test01Entity.setId(1234567890123456789L);
        ids.add(test01Entity);
        ids.add(test01Entity);
//        ids.add(1234567890123456789L);
//        ids.add(1234567890123456788L);
//        ids.add(1234567890123456787L);

        Long id = 1234567890123456789L;

        TestEntity testEntity = new TestEntity();
        testEntity.setId(id);
        testEntity.setIds(ids);

        return new JsonObjectResponseBody(testEntity);
    }

    @Operation(description = "DEMO")
    @RequestMapping(
        method = GET,
        value = "/test-one"
    )
    public JsonResponseBody testOne(
    ) {
        System.out.println("OK");

        //1.0 对比 老的列清单 和 新清单不一致的列
        System.out.println();
        List<Post> ls = (List<Post>) postRepository.findAll();

        return new JsonResponseBody();
    }

}
