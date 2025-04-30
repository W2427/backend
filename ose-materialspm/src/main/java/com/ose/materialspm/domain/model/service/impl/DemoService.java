package com.ose.materialspm.domain.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ose.materialspm.domain.model.repository.DemoRepository;
import com.ose.materialspm.domain.model.service.DemoInterface;
import com.ose.materialspm.entity.Demo;

@Component
public class DemoService implements DemoInterface {


    /**
     * 工序  操作仓库
     */
    private final DemoRepository demoRepository;

    /**
     * 构造方法
     *
     * @param demoRepository Demo管理 操作仓库
     */
    @Autowired
    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    @Override
    public List<Demo> getByName(String name) {
        // TODO Auto-generated method stub
        Demo demo = new Demo();
        demo.setName(name);
        demo.setProjectId(1L);
        demo.setOrgId(2L);
        demoRepository.save(demo);
        demoRepository.search("3L");
        return demoRepository.findByName(name);
    }


}
