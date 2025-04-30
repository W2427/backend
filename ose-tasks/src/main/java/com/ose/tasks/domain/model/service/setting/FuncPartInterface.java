package com.ose.tasks.domain.model.service.setting;

import com.ose.tasks.entity.setting.FuncPart;

import java.util.List;

public interface FuncPartInterface {


    List<FuncPart> getList(Long projectId);

    FuncPart getByname(Long projectId,String name);
}
