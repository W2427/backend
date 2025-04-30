package com.ose.tasks.domain.model.service.plan;


public interface P6GenerateInterface {

    void createP6Wbs(Integer p6WbsId, Long projectId);

    void createFullP6Wbs(String p6WbsId, Long projectId);

    void createMcP6Wbs(String p6WbsCode, Long projectId);

}
