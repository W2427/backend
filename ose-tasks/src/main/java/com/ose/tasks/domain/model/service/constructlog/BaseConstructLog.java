package com.ose.tasks.domain.model.service.constructlog;

import com.ose.util.SpringContextUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseConstructLog {

    public List<ConstructLogInterface> getConstructLogClazzs(String proxyNames) {

        String[] clazzStrings = proxyNames.split(",");
        List<ConstructLogInterface> constructLogClazzs = new ArrayList<>();

        if (clazzStrings != null
            && clazzStrings.length > 0) {
            for (int i = 0; i < clazzStrings.length; i++) {
                try {
                    Class clazz = Class.forName(clazzStrings[i]);
                    ConstructLogInterface delegate = (ConstructLogInterface) SpringContextUtils.getBean(clazz);
                    constructLogClazzs.add(delegate);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return constructLogClazzs;
    }


}
