package com.ose.tasks.controller.wbs;

import com.ose.controller.BaseController;
import com.ose.exception.BusinessError;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.vo.PressureExtraInfo;
import com.ose.vo.unit.PressureUnit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractWBSEntityController extends BaseController {

    /**
     * 检查上级层级节点。
     *
     * @param parent     上级层级节点
     * @param parentType 上级层级节点的实体类型
     */
    void validateParentHierarchyNode(
        final HierarchyNode parent,
        final String parentType
    ) {
        validateParentHierarchyNode(parent, "PIPING");

        if (parent.getEntityType() != parentType) {
            throw new BusinessError("指定的父级不是" + parentType);
        }
    }

    /**
     * 检查上级层级节点。
     *
     * @param parent        上级层级节点
     * @param hierarchyTypes 上级层级节点所在层级的类型
     * @param parentTypes   上级层级节点的类型
     */
    protected void validateParentHierarchyNode(
        final HierarchyNode parent,
        final List<String> hierarchyTypes,
        final String... parentTypes
    ) {
        validateParentHierarchyNode(parent, null, hierarchyTypes.get(0), parentTypes);
    }

    /**
     * 检查上级层级节点。
     *
     * @param parent        上级层级节点
     * @param rootPath      上级层级节点所在的跟路径
     * @param hierarchyType 上级层级节点所在层级的类型
     * @param parentTypes   上级层级节点的类型
     */
    void validateParentHierarchyNode(
        final HierarchyNode parent,
        final String rootPath,
        final String hierarchyType,
        final String... parentTypes
    ) {
        validateParentHierarchyNode(parent, hierarchyType);

        if (!(new HashSet<>(Arrays.asList(parentTypes))).contains(parent.getEntityType())) {
            throw new BusinessError("指定的父级不是" + parentTypes[0]);
        }

        if (rootPath != null && !parent.getPath().startsWith(rootPath)) {
            throw new BusinessError("指定的父级不在相应的区域下");
        }
    }



    /**
     * 设置设计压力表示值。
     *
     * @param isoEntity         管线实体信息
     * @param pressureExtraInfo 压力附加信息
     * @param designPressure    设计压力
     * @param pressureUnit      压力单位
     */
    void setDesignPressureDisplayText(
        final ISOEntity isoEntity,
        final PressureExtraInfo pressureExtraInfo,
        final Double designPressure,
        final PressureUnit pressureUnit
    ) {

        if (PressureExtraInfo.ATM == pressureExtraInfo) {
            isoEntity.setTestPressureText(PressureExtraInfo.ATM.name());
            isoEntity.setDesignPressure(Double.valueOf("0.0"));
            isoEntity.setDesignPressureUnit(PressureUnit.PSI.toString());

        } else {
            isoEntity.setDesignPressureText(
                (designPressure == null ? "" : designPressure)
                    + (pressureUnit == null ? "" : pressureUnit.toString())
                    + (pressureExtraInfo == null ? "" : ("/" + pressureExtraInfo.toString()))
            );
        }

    }

}
