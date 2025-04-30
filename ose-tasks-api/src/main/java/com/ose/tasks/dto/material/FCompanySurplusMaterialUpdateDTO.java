package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import java.util.HashMap;
import java.util.Map;

public class FCompanySurplusMaterialUpdateDTO extends BaseDTO {
    private static final long serialVersionUID = -4047272708644874718L;

    private Map<String, Integer> updateMap = new HashMap<>();

    private Long relationshipId;

    public Map<String, Integer> getUpdateMap() {
        return updateMap;
    }

    public void setUpdateMap(Map<String, Integer> updateMap) {
        this.updateMap = updateMap;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

}
