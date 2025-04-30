package com.ose.tasks.util;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import com.ose.tasks.dto.BpmnBaseDTO;
import com.ose.tasks.dto.BpmnGatewayDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.dto.bpm.TaskGatewayDTO;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.util.wbs.WorkflowEvaluator;
import com.ose.tasks.vo.BpmnGatewayType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.CryptoUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.impl.util.io.StreamSource;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.collections.MapUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * BPMN 工作流定义文件操作工具。
 */
/*==================================================================
    BpmnModel
        ├- List<Process> processes
        │       └- Process
        │               └- List<FlowElement> flowElementList
        │                                           └- flowElement
        │                                                   ├- category
        │                                                   ├- List<FormProperty> formProperties
        │                                                                           ├- FormProperty
        │                                                                                   ├- name
        │                                                                                   ├- List<FormValue> formValues
        │                                                                                                ├- FormValue (name "KEY2_VALUE", id "KEY2")
        │
        ├- LinkedHashMap<String, GraphicInfo> locationMap
        ├- LinkedHashMap<String, GraphicInfo> labelLocationMap
        └- LinkedHashMap<String, GraphicInfo> flowLocationMap



 */
public class BPMNUtils {

    // 占位图像 Base64 字节数组
    private static final byte[] BPMN_DIAGRAM_PLACEHOLDER = Base64
        .getDecoder()
        .decode("R0lGODlhAQABAPAAAAAAAAAAACH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sOZ2FtbWE9MC40NTQ1NDUALAAAAAABAAEAAAICRAEAOw==");

    // 工作流条件表达式格式（${...}）
    private static final Pattern CONDITION_EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    private static String RUNTIME_GATEWAY_NAME = "RUNTIME_GATEWAY";


    // 实体属性引用表达式格式（ENTITY.property）
    private static final String ENTITY_PROPERTY_REGEXP = "([_a-zA-Z][_a-zA-Z0-9]*)\\.([_a-zA-Z][_a-zA-Z0-9]*)";
    private static final Pattern ENTITY_PROPERTY_PATTERN = Pattern.compile(ENTITY_PROPERTY_REGEXP);
    private static final Pattern VALUES_PATTERN = Pattern.compile("[^\\s+\\-*/!=<>&|()?]+");
    private static final String NUMBER_REGEXP = "^([0-9]+(\\.[0-9]+)?)$";
    private static final String STRING_REGEXP = "^\".*?\"$";
    private static final String KEYWORD_REGEXP = "^(null|true|false)$";
    private static final String METHOD_REGEXP = "^(\"\")?\\.?(equals|name|toString)$";

    private static final String DIAGRAM_PROCESS_KEY = "WORKFLOW_DIAGRAM";

    // 演算用参数
    public static final Map<String, Object> EVALUATION_PARAMETERS_PIPING = new HashMap<>();

    public static final Map<String, Object> EVALUATION_PARAMETERS_STRUCTURE = new HashMap<>();


    /**
     * 创建实体信息，并将其作为工作流演算参数。
     *
     * @param <T>        实体类型范型
     * @param key        参数名
     * @param entityType 实体类型
     */
    private static <T extends BaseVersionedBizEntity> void setEntityParameter(
        String key,
        Class<T> entityType,
        String discipline
    ) {
        try {
            Map<String, Object> parameter = new HashMap<>();
            setEntityParameter(parameter, entityType.newInstance(), entityType);
            if (discipline.equalsIgnoreCase("PIPING")) {
                EVALUATION_PARAMETERS_PIPING.put(key, parameter);
            } else if (discipline.equalsIgnoreCase("STRUCTURE")) {
                EVALUATION_PARAMETERS_STRUCTURE.put(key, parameter);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            // nothing to do
        }
    }


    /**
     * 设置实体参数。
     *
     * @param <T>       实体类型或实体超类类型范型
     * @param parameter 参数 MAP
     * @param entity    实体信息
     * @param type      实体类型或实体超类类型
     */
    private static <T> void setEntityParameter(Map<String, Object> parameter, T entity, Class<T> type) {

        for (Field field : type.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                parameter.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                // nothing to do
            }
        }

        if (type.getSuperclass() != null) {
            setEntityParameter(parameter, entity, type.getSuperclass());
        }

    }

    // 初始化实体演算参数
    static {
        setEntityParameter("ISO", ISOEntity.class, "PIPING");
        setEntityParameter("SPOOL", SpoolEntity.class, "PIPING");
        setEntityParameter("PIPE_PIECE", PipePieceEntity.class, "PIPING");
        setEntityParameter("WELD_JOINT", WeldEntity.class, "PIPING");
        setEntityParameter("COMPONENT", ComponentEntity.class, "PIPING");
        setEntityParameter("TEST_PACKAGE", TestPackageEntityBase.class, "PIPING");
        setEntityParameter("CLEAN_PACKAGE", CleanPackageEntityBase.class, "PIPING");
        setEntityParameter("SUB_SYSTEM", SubSystemEntityBase.class, "PIPING");


    }

    public static void validateConditionExpressions(final File bpmnFile, final String discipline) {

        List<ValidationError> errors = new ArrayList<>();

        try {

            // 取得 DOM 实例
            Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(bpmnFile.getAbsolutePath());

            // 取得表达式节点
            NodeList condExprNodes = doc.getElementsByTagName("conditionExpression");

            int count = condExprNodes.getLength();

            if (count == 0) {
                return;
            }

            Node condExprNode;
            Node seqFlowNode;
            String conditionName;
            Node exprNode;
            String condExpr;
            Matcher exprMatcher;
            Matcher entityPropertyMatcher;
            String entityType;
            Object properties;
            String propertyName;

            for (int i = 0; i < count; i++) {

                condExprNode = condExprNodes.item(i);
                exprNode = condExprNode.getFirstChild();

                if (exprNode == null) {
                    continue;
                }

                condExpr = exprNode.getNodeValue();
                seqFlowNode = condExprNode.getParentNode();

                if (StringUtils.isEmpty(condExpr)) {
                    continue;
                }

                if (seqFlowNode == null) {
                    errors.add(new ValidationError(String.format(
                        "表达式【%s】无所属流程", condExpr
                    )));
                    continue;
                }

                if (seqFlowNode.getAttributes() == null
                    || seqFlowNode.getAttributes().getNamedItem("name") == null) {
                    conditionName = seqFlowNode.getNodeName();
                } else {
                    conditionName = seqFlowNode.getAttributes().getNamedItem("name").getNodeValue();
                }

                exprMatcher = CONDITION_EXPRESSION_PATTERN.matcher(condExpr);

                if (!exprMatcher.matches()) {
                    errors.add(new ValidationError(String.format(
                        "条件【%s】的表达式【%s】格式不正确",
                        conditionName, condExpr
                    )));
                    continue;
                }

                String expression = exprMatcher
                    .group(1)
                    .replaceAll("\\\\+\"", "")
                    .replaceAll("\".+?\"", "");

                exprMatcher = ENTITY_PROPERTY_PATTERN.matcher(expression);

                String entityProperty;

                while (exprMatcher.find()) {

                    entityProperty = exprMatcher.group();
                    entityPropertyMatcher = ENTITY_PROPERTY_PATTERN.matcher(entityProperty);

                    if (!entityPropertyMatcher.matches()) {
                        continue;
                    }

                    entityType = entityPropertyMatcher.group(1);
                    propertyName = entityPropertyMatcher.group(2);
                    Map<String, Object> variableMap = new HashMap<>();
                    if (discipline.equalsIgnoreCase("PIPING")) variableMap = BPMNUtils.EVALUATION_PARAMETERS_PIPING;
                    if (discipline.equalsIgnoreCase("STRUCTURE")) variableMap = BPMNUtils.EVALUATION_PARAMETERS_STRUCTURE;

                    if ((properties = variableMap.get(entityType)) == null) {
                        errors.add(new ValidationError(String.format(
                            "条件【%s】的表达式【%s】中的实体类型【%s】无效",
                            conditionName, condExpr, entityType
                        )));
                        continue;
                    }

                    if (!((Map) properties).containsKey(propertyName)) {
                        errors.add(new ValidationError(String.format(
                            "条件【%s】的表达式【%s】中的实体类型【%s】无属性【%s】",
                            conditionName, condExpr, entityType, propertyName
                        )));
                    }

                }

                expression = expression.replaceAll(ENTITY_PROPERTY_REGEXP, "");

                exprMatcher = VALUES_PATTERN.matcher(expression);

                String value;

                while (exprMatcher.find()) {
                    value = exprMatcher.group();
                    if (!(
                        value.matches(NUMBER_REGEXP)
                            || value.matches(STRING_REGEXP)
                            || value.matches(KEYWORD_REGEXP)
                            || value.matches(METHOD_REGEXP)
                    )
                        ) {
                        errors.add(new ValidationError(String.format(
                            "条件【%s】的表达式【%s】中的变量或值【%s】无效",
                            conditionName, condExpr, value
                        )));
                    }
                }

            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new BusinessError(); // TODO
        }

        if (errors.size() > 0) {
            throw new ValidationError(errors);
        }

    }

    /**
     * 将工作流定义文件的主工序的定义 KEY 设置为指定的值。
     *
     * @param bpmnFile             工作流定义文件实例
     * @param processDefinitionKey 工序定义 KEY
     */
    public static void setProcessDefinitionKey(
        final File bpmnFile,
        String processDefinitionKey
    ) {

        processDefinitionKey = processDefinitionKey.replaceAll("[^._0-9a-zA-Z]+", "_");

        try {

            // 取得 DOM 实例
            Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(bpmnFile.getAbsolutePath());

            // 取得工序节点列表
            NodeList nodes = doc.getElementsByTagName("process");

            // 必须定义一个且仅可定义一个工序
            if (nodes.getLength() != 1) {
                throw new ValidationError("error.bpmn.only-one-process-should-be-defined"); // TODO
            }

            // 设置主工序节点的 ID 为指定的工序定义 KEY
            nodes
                .item(0)
                .getAttributes()
                .getNamedItem("id")
                .setNodeValue(processDefinitionKey);

            // 保存 XML 文件
            TransformerFactory
                .newInstance()
                .newTransformer()
                .transform(new DOMSource(doc), new StreamResult(bpmnFile));

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new BusinessError(); // TODO
        }

    }


    /**
     * 生成 BPMN 工作流图像。
     *
     * @param bpmnModel      BPMN 模型
     * @return 图像数据流
     */
    public static InputStream generateProcessDiagram(
        final BpmnModel bpmnModel
    ) {

        // 根据 BPMN 模型生成图像

        return (new DefaultProcessDiagramGenerator())
//            .generateDiagram(bpmnModel, "jpg", Collections.<String>emptyList(), Collections.<String>emptyList());
            .generateDiagram(bpmnModel, "jpg", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "微软雅黑", "黑体", null, 1.0);

//            .generateJpgDiagram(bpmnModel, 1.0);
//        .generateDiagram(bpmnModel, "png", executedActivityIdList,
//            flowIds, "宋体", "微软雅黑", "黑体", null, 2.0);
    }

    /**
     * 解析 BPMN流程文件
     * @param bpmnModel                    bpmn文件流
     * @param orgId                         orgId
     * @param projectId                     projectId
     * @param moduleProcessDefinitionId    模块文件保存部署信息
     * @return                              流程解析文集集合
     */
    public static List<BpmnTaskRelation> resolveBpmnTaskRelation(BpmnModel bpmnModel,
                                                                 Long orgId,
                                                                 Long projectId,
                                                                 Long moduleProcessDefinitionId) {


        List<FlowElement> flowElements = (List<FlowElement>) bpmnModel.getProcesses().get(0).getFlowElements();
        Process bpmnProcess = bpmnModel.getProcesses().get(0);
        FlowNode startNode = null;
        Map<String, Set<String>> gateWayRelatedIdsMap = new HashMap<>();
//        Stack<String> gateWayNameStack = new Stack<>();
        Map<String, Set<String>> gateWayOutgoingMap = new HashMap<>();
        Map<String, String> nodeGateWayMap = new HashMap<>();// flowNode -> gateWay

        for(FlowElement flowElement:flowElements) {
            if(flowElement instanceof StartEvent) {
                startNode = (FlowNode) flowElement;
                break;
            }
        }

        if(startNode == null) {
            throw new BusinessError("BPMN FILE is WRONG");
        }

        FlowNode flowNode = startNode;
        List<BpmnTaskRelation> bpmnTaskRelations = new ArrayList<>();

        Set<String> todoNodeIds = new HashSet<>();
        Set<String> finishedNodeIds = new HashSet<>();

        resolveGateWays(bpmnProcess, flowElements, gateWayOutgoingMap, gateWayRelatedIdsMap, nodeGateWayMap);

        //从 startNode，startEvent开始遍历 整个 bpmn流程图。遍历到EndEvent节点结束
        while (true){
            BpmnTaskRelation bpmnTaskRelation = new BpmnTaskRelation(moduleProcessDefinitionId);
            List<BpmnSequenceNodeDTO> predecessorNodes;
            List<BpmnSequenceNodeDTO> successorNodes;
            //存储前一个 后一个节点路径 gateway+userTask
            Map<String, BpmnSequenceNodeDTO> pathMap = new HashMap<>();

            Map<String, String> parentPathMap = new HashMap<>(); // path -> parentPath

            //输出流程线 SequenceFlow
            Map<String, SequenceFlow> outSequenceFlowMap = new HashMap<>();//输出端 对应的 路径->SequenceFlow

            Map<String, SequenceFlow> incomeSequenceFlowMap = new HashMap<>();//输入端 对应的 路径->SequenceFlow
            String rootPath = "/";

            //收集 输入流程线
            flowNode.getIncomingFlows().forEach(incomeSequenceFlow ->
                incomeSequenceFlowMap.put(rootPath + incomeSequenceFlow.getId() + "/",
                    BeanUtils.clone(incomeSequenceFlow, SequenceFlow.class)));

            //收集 输出流程线
            flowNode.getOutgoingFlows().forEach(outSequenceFlow ->
                outSequenceFlowMap.put(rootPath + outSequenceFlow.getId() + "/",
                    BeanUtils.clone(outSequenceFlow, SequenceFlow.class)));


            predecessorNodes = getPredecessors(incomeSequenceFlowMap, parentPathMap, pathMap);//,
//                gateWayTypeStack, gateWayNameStack, gateWayOutgoingMap, nodeGateWayMap);
            predecessorNodes = expensionNodes(predecessorNodes);

            successorNodes = getSuccessors(outSequenceFlowMap, parentPathMap, pathMap, todoNodeIds,
                gateWayOutgoingMap, nodeGateWayMap, gateWayRelatedIdsMap);
            successorNodes = expensionNodes(successorNodes);

            Set<FlowElement> successGateWays = new HashSet<>();

            flowNode.getOutgoingFlows().forEach(flowElement -> {
                if (flowElement.getTargetFlowElement() instanceof ExclusiveGateway
                        || flowElement.getTargetFlowElement() instanceof InclusiveGateway
                        || flowElement.getTargetFlowElement() instanceof ParallelGateway) {
                    successGateWays.add(flowElement.getTargetFlowElement());
                }
            });

            List<TaskGatewayDTO> taskGatewayDTOs = new ArrayList<>();
            successGateWays.forEach(successGateWay -> {
                taskGatewayDTOs.addAll(getTaskGateWayDepth(successGateWay, 0));

            });


            bpmnTaskRelation.setTaskGateWays(StringUtils.toJSON(taskGatewayDTOs));
            bpmnTaskRelation.setNodeId(flowNode.getId());
            bpmnTaskRelation.setNodeName(flowNode.getName());
            bpmnTaskRelation.setJsonPredecessorNodes(predecessorNodes);
            bpmnTaskRelation.setJsonSuccessorNodes(successorNodes);
            Map<String, String> formValueMap = new HashMap<>();
            if(flowNode instanceof StartEvent) {
                bpmnTaskRelation.setNodeType("STARTEVENT");
            } else if(flowNode instanceof EndEvent) {
                bpmnTaskRelation.setNodeType("ENDEVENT");

            } else if(flowNode instanceof UserTask) {
                bpmnTaskRelation.setNodeType("USERTASK");
                List<FormProperty> formProperties = ((UserTask) flowNode).getFormProperties();
                formProperties.forEach(formProperty -> {
                    if(formProperty.getFormValues() == null) {return;}
                    formProperty.getFormValues().forEach(formValue -> {
                        formValueMap.put(formValue.getId(), formValue.getName());
                    });
                });
            }
            bpmnTaskRelation.setOrgId(orgId);
            bpmnTaskRelation.setProjectId(projectId);
            bpmnTaskRelation.setStatus(EntityStatus.ACTIVE);
            bpmnTaskRelation.setDeleted(false);
            bpmnTaskRelation.strFormValueMap(formValueMap);
            if(!(flowNode instanceof StartEvent || flowNode instanceof EndEvent)) {
                bpmnTaskRelation.setCategory(((UserTask) flowNode).getCategory());
                bpmnTaskRelation.setTaskType(((UserTask) flowNode).getFormKey());

            }

            //处理 stage/process/entityType stage/process/entityType/entitySubType1:entitySubType2
            List<BpmnTaskRelation> addedBpmnTaskRelations = expensionNode(bpmnTaskRelation, BpmnTaskRelation.class);


            bpmnTaskRelations.add(bpmnTaskRelation);
            bpmnTaskRelations.addAll(addedBpmnTaskRelations);


            finishedNodeIds.add(flowNode.getId());

            todoNodeIds.removeAll(finishedNodeIds);
            if(CollectionUtils.isEmpty(todoNodeIds)) {
                break;
            }
            String nextId = todoNodeIds.iterator().next();
            flowNode =(FlowNode) bpmnModel.getProcesses().get(0).getFlowElement(nextId);
        }

        //优化 前置任务的 关系
        //- 如果 前置任务 的 wbsEntityType 相同，则 只保留 entitySubType 相同的 前置任务
//        bpmnTaskRelations.forEach(btr ->{
//            optimizePredecessor(btr);
//        });
        return bpmnTaskRelations;

    }


    //优化 前置任务的 关系
    //- 如果 前置任务 的 wbsEntityType 相同，则 只保留 entitySubType 相同的 前置任务
    private static void optimizePredecessor(BpmnTaskRelation btr) {
        String category = btr.getCategory();
        if(StringUtils.isEmpty(category)){
            return;
        }

        String[] categoryArr = category.split("/");
        if(categoryArr.length != 4) {
            return;
        }
        String entityType = categoryArr[2];
        String entitySubType = categoryArr[3];
        if(StringUtils.isEmpty(entityType) || StringUtils.isEmpty(entitySubType)) {
            return;
        }

        List<BpmnSequenceNodeDTO> predecessorNodes = btr.getJsonPredecessorNodes();
        List<BpmnSequenceNodeDTO> newPredecessorNodes = new ArrayList<>();
        if(CollectionUtils.isEmpty(predecessorNodes)) {
            return;
        }

        //entityType -> Set<entitySubType> MAP
        Map<String, Set<String>> preEntityTypeRelationMap = new HashMap<>();

        predecessorNodes.forEach(pdn -> {
            String preCategory = pdn.getCategory();
            if(StringUtils.isEmpty(preCategory)){
                return;
            }

            String[] preCategoryArr = preCategory.split("/");
            if(preCategoryArr.length != 4) {
                return;
            }

            String preEntityType = preCategoryArr[2];
            String preKey = preCategoryArr[0] + "/" + preCategoryArr[1] + "/" + preCategoryArr[2];
            String preEntitySubType = preCategoryArr[3];

            Set<String> preEntitySubTypes = preEntityTypeRelationMap.get(preKey);
            if(preEntitySubTypes == null) {
                preEntitySubTypes = new HashSet<>();
            }

            if(entityType.equalsIgnoreCase(preEntityType) && entitySubType.equalsIgnoreCase(preEntitySubType)) {
                newPredecessorNodes.add(pdn);
            } else if(!entityType.equalsIgnoreCase(preEntityType)){
                preEntitySubTypes.add(preEntitySubType);
                preEntityTypeRelationMap.put(preKey, preEntitySubTypes);
                newPredecessorNodes.add(pdn);
            }

        });

        btr.setJsonPredecessorNodes(newPredecessorNodes);
        btr.strPreEntityTypeRelationMap(preEntityTypeRelationMap);

    }

    /**
     * 根据bpmn文件流 返回BpmnModel
     * @param fileStream    bpmn文件流
     * @return              BpmnModel
     */
    public static BpmnModel readBpmnFile(InputStream fileStream){
//        InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream("");
        StreamSource xmlSource = new InputStreamSource(fileStream);
        return new BpmnXMLConverter().convertToBpmnModel(xmlSource,
            false, false, "utf-8");
    }


    private static String getGatewayType(Gateway gateway) {
        String bpmnGatewayType = null;
        if(gateway instanceof ParallelGateway) {
            bpmnGatewayType = BpmnGatewayType.PARALLEL.name();
        } else if(gateway instanceof ExclusiveGateway) {
            bpmnGatewayType = BpmnGatewayType.EXCLUSIVE.name();
        } else if(gateway instanceof InclusiveGateway) {
            bpmnGatewayType = BpmnGatewayType.INCLUSIVE.name();
        } else if(gateway instanceof EventGateway) {
            bpmnGatewayType = BpmnGatewayType.EVENT.name();
        }
        return bpmnGatewayType;
    }

    private static BpmnGatewayDTO getBpmnGateway(Gateway gateway, SequenceFlow sequenceElement) {
        BpmnGatewayDTO bpmnGatewayDTO = new BpmnGatewayDTO();
        String conditionStr = sequenceElement.getConditionExpression();
        bpmnGatewayDTO.setGateWayCondition(conditionStr);
        bpmnGatewayDTO.setGateWayId(gateway.getId());
        bpmnGatewayDTO.setGateWayName(gateway.getName());
        String bpmnGatewayType = getGatewayType(gateway);
        bpmnGatewayDTO.setGatewayType(bpmnGatewayType);
        if(BpmnGatewayType.PARALLEL.name().equalsIgnoreCase(bpmnGatewayType)) {
            //如果是并行网关，判断是结束并行网关还是开始并行网关
            if(gateway.getIncomingFlows().size() > 1) {
                boolean isEndGateway = true;
                for(SequenceFlow incomingFlow: gateway.getIncomingFlows()) {
                    if(!StringUtils.isEmpty(incomingFlow.getConditionExpression())) {
                        isEndGateway = false;
                        break;
                    }
                }
                if(isEndGateway) {
                    Set<String> incomingNodes = new HashSet<>();
                    incomingNodes = calcIncomingNodes(gateway, incomingNodes);
                    bpmnGatewayDTO.setJsonIncomingNodes(new ArrayList<>(incomingNodes));
                }
            }
        }
        return  bpmnGatewayDTO;
    }

    /**
     * 设置节点信息
     * @param sequenceNodeDTO   节点信息
     * @param flowNode          流程节点
     * @return                  节点信息
     */
    private static BpmnSequenceNodeDTO setBpmnSequenceNode(BpmnSequenceNodeDTO sequenceNodeDTO, FlowNode flowNode){

        sequenceNodeDTO.setNodeName(flowNode.getName());
        sequenceNodeDTO.setCategory(((UserTask) flowNode).getCategory());
        sequenceNodeDTO.setNodeType("USERTASK");
        sequenceNodeDTO.setNodeId(flowNode.getId());
        sequenceNodeDTO.setTaskType(((UserTask) flowNode).getFormKey());

        return sequenceNodeDTO;
    }

    /**
     * 判断是不是 Gateway节点
     * @param flowNode 当前的 流程节点
     * @return          Boolean
     */
    private static boolean isGateway(FlowNode flowNode) {
        return (flowNode instanceof ExclusiveGateway) ||
            (flowNode instanceof InclusiveGateway) ||
            (flowNode instanceof ParallelGateway) ||
            (flowNode instanceof EventGateway);
    }


    /**
     * 取得节点的全部前置任务
     * @param incomeSequenceFlowMap 输入的流程线 当前流程线路径path -> 流程线 sequenceFlow
     * @param parentPathMap         path -> parentPath
     * @param pathMap               当前流程线路径 path-> BpmnSequenceNode 节点
//     * @param gateWayNameStack      存储 parallel/inclusive gate way 名称的 堆栈
//     * @param gateWayTypeStack      存储 parallel/inclusive gate way 类型的堆栈
//     * @param gateWayOutgoingMap    存储 parallel/inclusive gate way 出线 的 map
     * @return                      后置任务集合 BpmnSequenceNode 集合
     */
    private static List<BpmnSequenceNodeDTO> getPredecessors(Map<String, SequenceFlow> incomeSequenceFlowMap,
                                                             Map<String, String> parentPathMap,
                                                             Map<String, BpmnSequenceNodeDTO> pathMap
                                                            ) {
        //遍历完需要移除的节点path集合
        List<String> removedFlowNodeKeys = new ArrayList<>();
        Set<String> handledNodeIds = new HashSet<>();
        //记录后续的节点
        Map<String, FlowNode> incomeFlowNodeMap = new HashMap<>();

        List<BpmnSequenceNodeDTO> predecessorNodes = new ArrayList<>();


        do {
            //将 输入线的源节点加入到 incomeFlowNodes集合


            incomeSequenceFlowMap.forEach((incomeSequenceFlowPath, incomeSequenceElement) -> {
                FlowNode currentFlowNode = (FlowNode) incomeSequenceElement.getSourceFlowElement();
                if(handledNodeIds.contains(currentFlowNode.getId())) return;
                handledNodeIds.add(currentFlowNode.getId());
                BpmnSequenceNodeDTO predecessorNode = new BpmnSequenceNodeDTO();

                //如果当前 路径节点没有 bpmnSequenceNodeDTO，则新建一个
                String parentPath = parentPathMap.get(incomeSequenceFlowPath);

                List<BpmnGatewayDTO> gateways = initNode(parentPath,
                    parentPathMap,
                    incomeSequenceFlowPath,
                    predecessorNode,
                    pathMap);


                //父级节点的 predecessorNode 只能用一次，下次需要新建
                parentPathMap.remove(parentPath);

                if (currentFlowNode instanceof UserTask) { //如果是任务节点

                    predecessorNode = setBpmnSequenceNode(predecessorNode, currentFlowNode);
                    predecessorNode.setOptional(checkOptional(predecessorNode));

                    predecessorNodes.add(predecessorNode);
                } else if (isGateway(currentFlowNode)) { //如果是 网关节点

                    predecessorNode.setGateways(getNodeGateways(
                        (Gateway) currentFlowNode,
                        gateways,
                        incomeSequenceElement
                    ));

                } else if (currentFlowNode instanceof StartEvent) { //如果是开始节点
                    predecessorNode.setNodeName(currentFlowNode.getName());
                    predecessorNode.setNodeType("STARTEVENT");
                    predecessorNode.setNodeId(currentFlowNode.getId());
                    predecessorNode.setOptional(checkOptional(predecessorNode));
                    predecessorNodes.add(predecessorNode);
                }
                pathMap.put(incomeSequenceFlowPath, predecessorNode);

                incomeFlowNodeMap.put(incomeSequenceFlowPath,
                    (FlowNode) incomeSequenceElement.getSourceFlowElement());

            });

//          incomeSequenceFlows.clear();
            incomeSequenceFlowMap.clear();
            //如果当前的 输入节点集合中的 节点 不是用户任务（应该改为不是任务）也不是开始节点，则将这些节点的输入线 加入到输入线集合，再去下一轮遍历
            incomeFlowNodeMap.forEach((incomeFlowPath, incomeFlowNode) -> {
                if (isGateway(incomeFlowNode)) {
                    incomeFlowNode.getIncomingFlows().forEach(subIncomeFlow -> {
                        incomeSequenceFlowMap.put(incomeFlowPath + subIncomeFlow.getId() + "/", subIncomeFlow);
                        parentPathMap.put(incomeFlowPath + subIncomeFlow.getId() + "/", incomeFlowPath);
                    });
                    removedFlowNodeKeys.add(incomeFlowPath);
                }
            });
            //移除 节点集合中的 Gateway节点
            removedFlowNodeKeys.forEach(incomeFlowNodeMap::remove);

        } while (!MapUtils.isEmpty(incomeSequenceFlowMap));

        removedFlowNodeKeys.clear();

        return predecessorNodes;
    }


    /**
     * 取得节点的全部后置任务
     * @param outSequenceFlowMap    输入的流程线 当前流程线路径path -> 流程线 sequenceFlow
     * @param parentPathMap         path -> parentPath
     * @param pathMap               当前流程线路径 path-> BpmnSequenceNode 节点
     * @param todoNodeIds           待处理的流程节点 Ids
     * @return                      后置任务集合 BpmnSequenceNode 集合
     */
    private static List<BpmnSequenceNodeDTO> getSuccessors(Map<String, SequenceFlow> outSequenceFlowMap,
                                                           Map<String, String> parentPathMap,
                                                           Map<String, BpmnSequenceNodeDTO> pathMap,
                                                           Set<String> todoNodeIds,
                                                           Map<String, Set<String>> gateWayOutgoingMap,
                                                           Map<String, String> nodeGateWayMap,
                                                           Map<String, Set<String>> gateWayRelatedIdsMap) {
        //遍历完需要移除的节点path集合
        List<String> removedFlowNodeKeys = new ArrayList<>();
        Set<String> handledNodeIds = new HashSet<>();

        //记录后续节点
        Map<String, FlowNode> outFlowNodeMap = new HashMap<>();

        List<BpmnSequenceNodeDTO> successorNodes = new ArrayList<>();
        Set<String> removedGateWays = new HashSet<>();

        do {
            outSequenceFlowMap.forEach((outSequenceFlowPath, outSequenceElement) -> {
                FlowNode currentFlowNode = (FlowNode) outSequenceElement.getTargetFlowElement();
                if(handledNodeIds.contains(currentFlowNode.getId())) return;
                handledNodeIds.add(currentFlowNode.getId());
                FlowNode sourceFlowNode = (FlowNode) outSequenceElement.getSourceFlowElement();
                List<SequenceFlow> incomeSequenceFlows = currentFlowNode.getIncomingFlows();
                Set<String> incomeIds = incomeSequenceFlows.stream().map(BaseElement::getId).collect(Collectors.toSet());
                for(Map.Entry<String, Set<String>> entry : gateWayOutgoingMap.entrySet()) {
                    Set<String> ids = new HashSet<>();//BeanUtils.clone(entry.getValue(), HashSet.class);
                    entry.getValue().forEach(id -> {
                        ids.add(id);
                    });
                    ids.retainAll(incomeIds);
                    if(ids.equals(entry.getValue())) {
                        removedGateWays.add(entry.getKey());
                    }
                }

                BpmnSequenceNodeDTO successorNode;
                //如果当前 路径节点没有 bpmnSequenceNodeDTO，则新建一个
                String parentPath = parentPathMap.get(outSequenceFlowPath);
                successorNode = new BpmnSequenceNodeDTO();


                List<BpmnGatewayDTO> gateways = initNode(parentPath,
                    parentPathMap,
                    outSequenceFlowPath,
                    successorNode,
                    pathMap);


                //如果 流程线 上有 条件表达式，更新到之前的 gateway上，最后一个gateway
                if(outSequenceElement.getConditionExpression() != null &&
                    !CollectionUtils.isEmpty(successorNode.getGateways())) {
                    String conditionStr = outSequenceElement.getConditionExpression();
//                    if(!StringUtils.isEmpty(conditionStr)) {
//                        conditionStr = getConditionElSp(conditionStr);
//                    }
                    successorNode.getGateways().get(successorNode.getGateways().size()-1).setGateWayCondition(
                        conditionStr
                    );
                }

                if (currentFlowNode instanceof UserTask) { //如果是任务节点
                    successorNode = setBpmnSequenceNode(successorNode, currentFlowNode);
                    successorNode.setOptional(checkOptional(successorNode));
                    todoNodeIds.add(currentFlowNode.getId());
                    String gateWayId = nodeGateWayMap.get(sourceFlowNode.getId());
                    if(gateWayId != null && removedGateWays.contains(gateWayId)){
                        if(successorNode.getRelatedNodeIds() == null) successorNode.setRelatedNodeIds(new HashSet<>());
                        successorNode.getRelatedNodeIds().addAll(gateWayRelatedIdsMap.get(gateWayId));

                    }
                    successorNodes.add(successorNode);
                } else if (isGateway(currentFlowNode)) { //如果是 网关节点

                    successorNode.setGateways(getNodeGateways(
                        (Gateway) currentFlowNode,
                        gateways,
                        outSequenceElement
                    ));



                } else if (currentFlowNode instanceof EndEvent) { //如果是结束节点
                    successorNode.setNodeName(currentFlowNode.getName());
                    successorNode.setNodeType("ENDEVENT");
                    successorNode.setNodeId(currentFlowNode.getId());
                    successorNode.setOptional(checkOptional(successorNode));
                    String gateWayId = nodeGateWayMap.get(sourceFlowNode.getId());
                    if(gateWayId != null && removedGateWays.contains(gateWayId)){
                        if(successorNode.getRelatedNodeIds() == null) successorNode.setRelatedNodeIds(new HashSet<>());
                        successorNode.getRelatedNodeIds().addAll(gateWayRelatedIdsMap.get(gateWayId));
                    }
                    successorNodes.add(successorNode);
                    todoNodeIds.add(currentFlowNode.getId());
                }


                pathMap.put(outSequenceFlowPath, successorNode);

                outFlowNodeMap.put(outSequenceFlowPath,
                    (FlowNode) outSequenceElement.getTargetFlowElement());

            });

            outSequenceFlowMap.clear();
            outFlowNodeMap.forEach((outFlowPath, outFlowNode) -> {
                if (isGateway(outFlowNode)) {

                    outFlowNode.getOutgoingFlows().forEach(subOutFlow -> {
                        outSequenceFlowMap.put(outFlowPath + subOutFlow.getId() + "/", subOutFlow);
                        parentPathMap.put(outFlowPath + subOutFlow.getId() + "/", outFlowPath);
                    });

                    removedFlowNodeKeys.add(outFlowPath);

                }

            });

            //移除 节点集合中的 Gateway节点
            removedFlowNodeKeys.forEach(outFlowNodeMap::remove);
        } while (!MapUtils.isEmpty(outSequenceFlowMap));

        return successorNodes;
    }


    private static List<BpmnGatewayDTO> initNode(String parentPath,
                                                 Map<String, String> parentPathMap,

                                                 String sequenceFlowPath,
                                                 BpmnSequenceNodeDTO bpmnSequenceNode,
                                                 Map<String, BpmnSequenceNodeDTO> pathMap
    ) {
        List<BpmnGatewayDTO> gateways = new ArrayList<>();
        if(parentPath == null) {
            parentPath = "/";
            parentPathMap.put(sequenceFlowPath, parentPath);

        } else {
            bpmnSequenceNode.setNodeId(pathMap.get(parentPath).getNodeId());
            bpmnSequenceNode.setNodeType(pathMap.get(parentPath).getNodeType());
            if(pathMap.get(parentPath).getGateways()!=null) {
                for(BpmnGatewayDTO gw : pathMap.get(parentPath).getGateways()) {
                    gateways.add(BeanUtils.clone(gw, BpmnGatewayDTO.class));
                }
            }
            bpmnSequenceNode.setGateways(gateways);
            bpmnSequenceNode.setCategory(pathMap.get(parentPath).getCategory());
            bpmnSequenceNode.setNodeName(pathMap.get(parentPath).getNodeName());
//            gateways = bpmnSequenceNode.getGateways();
        }
        return gateways;
    }

    private static List<BpmnGatewayDTO> getNodeGateways(Gateway currentFlowNode,
                                                        List<BpmnGatewayDTO> gateways,
                                                        SequenceFlow sequenceElement) {
        BpmnGatewayDTO bpmnGatewayDTO = getBpmnGateway(currentFlowNode, sequenceElement);

        List<BpmnGatewayDTO> nodeGateways = new ArrayList<>();
        for(BpmnGatewayDTO gw : gateways) {
            nodeGateways.add(BeanUtils.clone(gw, BpmnGatewayDTO.class));
        }
        nodeGateways.add(bpmnGatewayDTO);
        return nodeGateways;
    }

    private static boolean checkOptional(BpmnSequenceNodeDTO sequenceNode) {
        return !CollectionUtils.isEmpty(sequenceNode.getGateways()) &&
//            sequenceNode.getGateways().size() == 1 &&
            sequenceNode.getGateways().stream().map(BpmnGatewayDTO::getGateWayName).collect(Collectors.toSet()).contains(RUNTIME_GATEWAY_NAME);
//            sequenceNode.getGateways().get(0).getGateWayName().equalsIgnoreCase(RUNTIME_GATEWAY_NAME);
    }


    //将 stage/process/entityType/entitySubType1:entitySubType2的数据展开,对于节点集合
    public static List<BpmnSequenceNodeDTO> expensionNodes(List<BpmnSequenceNodeDTO> sequenceNodes) {
        List<BpmnSequenceNodeDTO> addedSequenceNodes = new ArrayList<>();

        sequenceNodes.forEach(sequenceNodeDTO -> {
            addedSequenceNodes.addAll(expensionNode(sequenceNodeDTO, BpmnSequenceNodeDTO.class));

        });

        sequenceNodes.addAll(addedSequenceNodes);
        return sequenceNodes;
    }


    //处理 bpmnTaskRelation 任务节点 stage/process/entityType stage/process/entityType/entitySubType1:entitySubType2
    public static <T extends BpmnBaseDTO> List<T> expensionNode(T node, Class<T> clazz) {
        //处理 stage/process/entityType stage/process/entityType/entitySubType1:entitySubType2
        List<T> addedBpmnNodes = new ArrayList<>();

        String category = node.getCategory();
        if(!StringUtils.isEmpty(category)) {
            String[] categoryArr = category.split("/");

            if (categoryArr.length == 3) {
                node.setCategory(category + "/" + categoryArr[2]);

            } else if (categoryArr.length == 4 && categoryArr[3].contains(":")) {
                String[] entitySubTypeArr = categoryArr[3].split(":");
                int i = 0;
                for (String entitySubType : entitySubTypeArr) {
                    //第一个节点 更新原有的 bpmnTaskRelation 的 category
                    String newCategory = categoryArr[0] + "/" + categoryArr[1] + "/" + categoryArr[2] + "/" + entitySubType;
                    if (i++ == 0) {
                        node.setCategory(newCategory);
                    } else {
                        T addedBpmnNode = BeanUtils.clone(node, clazz);
                        addedBpmnNode.setId(CryptoUtils.uniqueDecId());
                        addedBpmnNode.setCategory(newCategory);
                        addedBpmnNodes.add(addedBpmnNode);
                    }

                }
            }
        }
        return addedBpmnNodes;
    }

    public static String getConditionElSp(String condition) {
        Matcher matcher = CONDITION_EXPRESSION_PATTERN.matcher(condition);
        if(matcher.find())
            return matcher.group(1);
        return condition;
    }


    public static void main(String[] args) throws FileNotFoundException {
//        String heatNo = "1234/56";
//        System.out.println(heatNo.indexOf("/"));
//        heatNo = heatNo.substring(0,heatNo.indexOf("/"));
//
//        String tmp = "{\"id\":1585020420360790847,\"nodeName\":\"PIPE_PIECE_TRANSPORT\",\"nodeId\":\"usertask2\",\"nodeType\":\"USERTASK\",\"category\":\"CUTTING/PIPE_PIECE_TRANSPORT/PIPE_PIECE/PIPE_PIECE\",\"status\":\"ACTIVE\",\"deleted\":false,\"orgId\":1584974049236114088,\"projectId\":1584974049645223435,\"moduleProcessDefinitionId\":1585020412529922092,\"predecessorNodes\":[{\"id\":1585020420361319592,\"nodeName\":\"CUTTING\",\"nodeId\":\"usertask1\",\"nodeType\":\"USERTASK\",\"category\":\"CUTTING/CUTTING/PIPE_PIECE/PIPE_PIECE\",\"optional\":false}],\"successorNodes\":[{\"id\":1585020420361371430,\"nodeName\":\"FITUP\",\"nodeId\":\"usertask3\",\"nodeType\":\"USERTASK\",\"category\":\"FABRICATION/FITUP/WELD_JOINT/SBW\",\"optional\":false},{\"id\":1585020420361453613,\"nodeName\":\"FITUP\",\"nodeId\":\"usertask3\",\"nodeType\":\"USERTASK\",\"category\":\"FABRICATION/FITUP/WELD_JOINT/SSFW\",\"optional\":false},{\"id\":1585020420361481439,\"nodeName\":\"FITUP\",\"nodeId\":\"usertask3\",\"nodeType\":\"USERTASK\",\"category\":\"FABRICATION/FITUP/WELD_JOINT/SSPW\",\"optional\":false}]}";
//
//        StringUtils.decode(tmp, new TypeReference<BpmnTaskRelation>() {});


        String fileName = "/var/www/OVERALL.bpmn";
//        String fileName = "/Users/Macbook/eclipse-cms/test/test.bpmn";
        File file = new File(fileName);

//        String strr = "_WELD_JOINT A WELD_JOINT";
//        String str = "WELD_JOINT";
//        strr = strr.replaceAll("([^_a-zA-Z]|^)("+str+")([^_a-zA-Z]{0,1})", "$1#" +str+"$3");
        InputStream fileStream = new FileInputStream(file);

        try {
//            List<String> entityTypeNames = WBSEntityType.getEntityTypeNames();
//            entityTypeNames.sort(new SortByLengthComparator());
            BpmnModel bpmnModel = readBpmnFile(fileStream);
            List<BpmnTaskRelation> bpmnTaskRelations = resolveBpmnTaskRelation(bpmnModel,
                0L, 1L, 0L);

            BpmnTaskRelation bpmnTaskRelation = null;

            for(BpmnTaskRelation bpmnTaskRelation1 : bpmnTaskRelations) {
                if(bpmnTaskRelation1.getNodeId().equalsIgnoreCase("usertask102")){
                    bpmnTaskRelation = bpmnTaskRelation1;
                    break;
                }
            }



            Map<String, Object> variables = new HashMap<>();

            //-----------------
            String strel = "${#STRUCT_WELD_JOINT.mtRatio > 0}";

            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(strel, new TemplateParserContext("${","}"));
//        Expression expression1 = parser.parseExpression(str, new TemplateParserContext());

            EvaluationContext context = new StandardEvaluationContext();



            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            expression.getValue(context, Boolean.class);
            //-----------------

            WorkflowEvaluator.evaluate(bpmnTaskRelation, variables, true);

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 递归取网关出口信息
     *
     * @param targetEl
     * @param depth
     * @return
     */
    private static List<TaskGatewayDTO> getTaskGateWayDepth(FlowElement targetEl, int depth) {
        List<TaskGatewayDTO> gateWays = new ArrayList<>();
        depth++;
        if (depth > 2) {
            return gateWays;
        }
        List<SequenceFlow> sequenceFlowList;
        if (targetEl instanceof ExclusiveGateway
            || targetEl instanceof InclusiveGateway) {
            sequenceFlowList = ((Gateway) targetEl).getOutgoingFlows();// 流出信息
            for (SequenceFlow flow : sequenceFlowList) {
                String name = flow.getName();
//              System.out.println("name=" + name);
                String condition = flow.getConditionExpression();
                TaskGatewayDTO gateWay = new TaskGatewayDTO();
                if (targetEl instanceof ExclusiveGateway) {
                    if (condition != null) {
                        condition = condition.replace(" ", "").replace("{", "").replace("}", "").replace("$", "")
                            .replace("\"", "");//.replace(strEXCLUSIVE_GATEWAY_RESULT, "").replace("=", "");
                        if (condition.contains("==")) {
                            String[] conditionArr = condition.split("==");
                            gateWay.setKey(conditionArr[0]);
                            gateWay.setCondition(conditionArr[1]);
                        } else {
                            gateWay.setCondition(true);
                            gateWay.setKey(condition);
                        }
                        gateWay.setName(name);
                        gateWays.add(gateWay);
                    }
                } else {
                    if (condition != null && name != null) {
                        gateWay.setName(name);
                        condition = condition.replace("${", "").replace("}", "");
                        gateWay.setCondition(condition);
                        gateWay.setMutiSelectFlag(true);
                        gateWays.add(gateWay);
                    }
                }
                FlowElement targetElSub = flow.getTargetFlowElement();
                gateWay.setItems(getTaskGateWayDepth(targetElSub, depth));
            } // ExclusiveGateway
        }  else if(targetEl instanceof ParallelGateway) {
            sequenceFlowList = ((Gateway) targetEl).getOutgoingFlows();// 流出信息
            for (SequenceFlow flow : sequenceFlowList) {
                FlowElement targetElSub = flow.getTargetFlowElement();
                gateWays.addAll(getTaskGateWayDepth(targetElSub, depth));
            }
        }
        return gateWays;
    }

    /**
     * 取得 网关信息，
     * @param bpmnProcess
     * @param successGateWay
     * @param gateWayOutgoingMap
     * @param nodeGateWayMap
     * @param gateWayRelatedIdsMap
     * @param nodeUsedTimes
     * @param parentGateWay
     * @param checkedGateWays
     */
    private static void getGateWayInfo(Process bpmnProcess,
                                       FlowElement successGateWay,
                                       Map<String,Set<String>> gateWayOutgoingMap,
                                       Map<String,String> nodeGateWayMap,
                                       Map<String, Set<String>> gateWayRelatedIdsMap,
                                       Map<String, Integer> nodeUsedTimes,
                                       String parentGateWay,
                                       Set<String> checkedGateWays) {

        if(checkedGateWays.contains(successGateWay.getId())) return;
        checkedGateWays.add(successGateWay.getId());
        List<SequenceFlow> sequenceFlowList = ((Gateway) successGateWay).getOutgoingFlows();// 流出信息
        Set<FlowElement> newFlowElements = new HashSet<>();
        Set<SequenceFlow> checkedSequenceFlows = new HashSet<>();
        Integer branchCount = sequenceFlowList.size();
        String commonNodeId = null;
        Set<String> outGoingIds = new HashSet<>();

        do {
//            Set<String> outGoingIds = new HashSet<>();//sequenceFlowList.stream().map(BaseElement::getId).collect(Collectors.toSet());
//            Set<String> nodeIds = new HashSet<>();//sequenceFlowList.stream().map();;;;
//            sequenceFlowList.forEach(sf -> {
//                outGoingIds.add(sf.getId());
//                nodeIds.add(sf.getTargetRef());
//            });
//            gateWayOutgoingMap.put(successGateWay.getId(), outGoingIds);

            for (SequenceFlow sf : sequenceFlowList) {
                FlowElement targetFlowElement = sf.getTargetFlowElement();
                if (targetFlowElement instanceof ParallelGateway
                    || targetFlowElement instanceof InclusiveGateway) {
                    getGateWayInfo(bpmnProcess, targetFlowElement, gateWayOutgoingMap, nodeGateWayMap, gateWayRelatedIdsMap,
                        nodeUsedTimes, successGateWay.getId(), checkedGateWays);
                    branchCount = branchCount + ((Gateway)targetFlowElement).getOutgoingFlows().size() - 1;
                    gateWayOutgoingMap.computeIfAbsent(successGateWay.getId(), k->new HashSet<>()).addAll(gateWayOutgoingMap.get(targetFlowElement.getId()));
                    gateWayRelatedIdsMap.computeIfAbsent(successGateWay.getId(), k -> new HashSet<>()).addAll(gateWayRelatedIdsMap.get(targetFlowElement.getId()));
//                    checkedGateWays.add(targetFlowElement.getId());
                } else {
                    if(targetFlowElement instanceof UserTask || targetFlowElement instanceof StartEvent || targetFlowElement instanceof EndEvent) {
                        gateWayRelatedIdsMap.computeIfAbsent(successGateWay.getId(), k -> new HashSet<>()).add(sf.getTargetRef());
                        nodeGateWayMap.put(sf.getTargetRef(), successGateWay.getId());
                        nodeUsedTimes.put(sf.getTargetRef(), nodeUsedTimes.computeIfAbsent(sf.getTargetRef(), k -> 0) + 1);
                    }
                    newFlowElements.add(targetFlowElement);

                }
                if(nodeUsedTimes.computeIfAbsent(sf.getTargetRef(), k->0).equals(branchCount)) {
                    commonNodeId = sf.getTargetRef();
//                    gateWayRelatedIdsMap.get(successGateWay.getId()).remove(sf.getTargetRef());
//                    FlowElement fe = sf.getTargetFlowElement();
//                    Set<String> removedIds = new HashSet<>();
//                    removedIds.add(sf.getTargetRef());
//                    ((FlowNode)fe).getOutgoingFlows().forEach(of -> removedIds.add(of.getTargetRef()));
//                    gateWayRelatedIdsMap.get(successGateWay.getId()).removeAll(removedIds);
                    nodeGateWayMap.remove(sf.getTargetRef());
                    if(parentGateWay != null) {
                        patchParentGateWay(parentGateWay, sf.getTargetFlowElement(), nodeGateWayMap);
                    }
//                    FlowElement fn = sf.getTargetFlowElement();
//                    outGoingIds = ((FlowNode) fn).getIncomingFlows().stream().map(BaseElement::getId).collect(Collectors.toSet());
//                    gateWayOutgoingMap.computeIfAbsent(successGateWay.getId(), k->new HashSet<>()).addAll(outGoingIds);
                    Map<String, Set<String>> info = getNodeBetweenInfo(bpmnProcess, successGateWay.getId(), commonNodeId);
                    gateWayOutgoingMap.put(successGateWay.getId(), info.get("outGoingIds"));
                    gateWayRelatedIdsMap.put(successGateWay.getId(), info.get("relatedIds"));
                    break;
                }
            }
            checkedSequenceFlows.addAll(sequenceFlowList);
//            gateWayOutgoingMap.get(successGateWay.getId()).removeAll(sequenceFlowList.stream().map(BaseElement::getId).collect(Collectors.toSet()));
            sequenceFlowList = new ArrayList<>();
            for (FlowElement fe : newFlowElements) {
                sequenceFlowList.addAll(((FlowNode) fe).getOutgoingFlows());
            }
            sequenceFlowList.removeAll(checkedSequenceFlows);
            newFlowElements = new HashSet<>();

        } while(commonNodeId == null && !CollectionUtils.isEmpty(sequenceFlowList));



//        do {
//            Set<String> outGoingIds = new HashSet<>();//sequenceFlowList.stream().map(BaseElement::getId).collect(Collectors.toSet());
//            Set<String> nodeIds = new HashSet<>();//sequenceFlowList.stream().map();;;;
//            sequenceFlowList.forEach(sf -> {
//                outGoingIds.add(sf.getId());
//                nodeIds.add(sf.getTargetRef());
//            });
//            gateWayOutgoingMap.put(successGateWay.getId(), outGoingIds);
//            gateWayRelatedIdsMap.computeIfAbsent(successGateWay.getId(), k->new HashSet<>()).addAll(nodeIds);
//
//
//
//            for (SequenceFlow sf : sequenceFlowList) {
//                FlowElement targetFlowElement = sf.getTargetFlowElement();
//                if (targetFlowElement instanceof ParallelGateway
//                    || targetFlowElement instanceof InclusiveGateway) {
//                    getGateWayInfo(targetFlowElement, gateWayOutgoingMap, nodeGateWayMap, gateWayRelatedIdsMap);
//                    gateWayOutgoingMap.get(successGateWay.getId()).addAll(gateWayOutgoingMap.get(targetFlowElement.getId()));
//                    gateWayRelatedIdsMap.computeIfAbsent(successGateWay.getId(), k -> new HashSet<>()).addAll(gateWayOutgoingMap.get(targetFlowElement.getId()));
//                } else {
//                    newFlowElements.add(targetFlowElement);
//                    nodeGateWayMap.put(sf.getTargetRef(), successGateWay.getId());
//                }
//            }
//            checkedSequenceFlows.addAll(sequenceFlowList);
//            sequenceFlowList = new ArrayList<>();
//            if(newFlowElements.size() == 1) {
//                gateWayRelatedIdsMap.get(successGateWay.getId()).remove(newFlowElements.iterator().next().getId());
//                nodeGateWayMap.remove(newFlowElements.iterator().next().getId());
//                break;
//            }
//            for (FlowElement fe : newFlowElements) {
//                sequenceFlowList.addAll(((FlowNode) fe).getOutgoingFlows());
//            }
//            newFlowElements = new HashSet<>();
//
//        } while(true);
    }

    private static void patchParentGateWay(String parentGateWay, FlowElement targetFlowElement, Map<String, String> nodeGateWayMap) {

        Set<SequenceFlow> newSfs = new HashSet<>();
        Set<SequenceFlow> checkedSfs = new HashSet<>();
        List<SequenceFlow> sequenceFlows = ((FlowNode)targetFlowElement).getIncomingFlows();
        do {
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
                if (sourceFlowElement instanceof UserTask || sourceFlowElement instanceof StartEvent) {
                    nodeGateWayMap.put(sourceFlowElement.getId(), parentGateWay);
                } else if (sourceFlowElement instanceof Gateway) {
                    newSfs.addAll(((Gateway) sourceFlowElement).getIncomingFlows());
                }
                checkedSfs.add(sequenceFlow);

            }
            sequenceFlows = new ArrayList<>();
            sequenceFlows.addAll(newSfs);
            sequenceFlows.removeAll(checkedSfs);
            newSfs = new HashSet<>();
            if(CollectionUtils.isEmpty(sequenceFlows)) break;
        } while(true);
    }

    private static Set<String> calcIncomingNodes(Gateway gateway, Set<String> incomingNodes){
        Set<SequenceFlow> newSfs = new HashSet<>();
        Set<SequenceFlow> checkedSfs = new HashSet<>();
        List<SequenceFlow> sequenceFlows = gateway.getIncomingFlows();
//        List<FlowElement> newFlowElements = new ArrayList<>();
        do {
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                if(!StringUtils.isEmpty(sequenceFlow.getConditionExpression())) continue;
                FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
                if(incomingNodes.contains(sourceFlowElement.getId())) continue;

                if (sourceFlowElement instanceof EndEvent || sourceFlowElement instanceof StartEvent ||
                    sourceFlowElement instanceof ParallelGateway) {
                    continue;
                }
                newSfs.addAll(((FlowNode) sourceFlowElement).getIncomingFlows());
                incomingNodes.add(sourceFlowElement.getId());

            }
            checkedSfs.addAll(sequenceFlows);


            sequenceFlows = new ArrayList<>();
            sequenceFlows.addAll(newSfs);
            sequenceFlows.removeAll(checkedSfs);
            newSfs = new HashSet<>();
            if(CollectionUtils.isEmpty(sequenceFlows)) break;
        } while(true);

        return incomingNodes;
    }

    private static void resolveGateWays(Process bpmnProcess, List<FlowElement> flowElements, Map<String, Set<String>> gateWayOutgoingMap,
                                        Map<String, Set<String>> gateWayRelatedIdsMap, Map<String, String> nodeGateWayMap) {

        Map<String, Integer> nodeUsedTimes = new HashMap<>();
        Set<String> checkedGateWays = new HashSet<>();
        for(FlowElement flowElement : flowElements) {
            if(!(flowElement instanceof InclusiveGateway || flowElement instanceof ParallelGateway)) continue;
            getGateWayInfo(bpmnProcess, flowElement, gateWayOutgoingMap, nodeGateWayMap, gateWayRelatedIdsMap, nodeUsedTimes, null, checkedGateWays);
//            checkedFes.add(flowElement.getId())
        }

    }

    private static Map<String, Set<String>> getNodeBetweenInfo(Process bpmnProcess, String sourceId, String targetId) {

        FlowElement sourceFlowElement = bpmnProcess.getFlowElement(sourceId);
        Set<String> relatedIds = new HashSet<>();
        Set<String> outGoingIds = new HashSet<>();
        Set<SequenceFlow> sequenceFlows = new HashSet<>();
        sequenceFlows.addAll(((FlowNode)sourceFlowElement).getOutgoingFlows());
        Set<SequenceFlow> checkedSfs = new HashSet<>();
        Set<SequenceFlow> newSfs = new HashSet<>();

        do {
            for (SequenceFlow sf : sequenceFlows) {
                FlowElement targetFlowElement = sf.getTargetFlowElement();
                if (targetFlowElement instanceof UserTask || targetFlowElement instanceof StartEvent || targetFlowElement instanceof EndEvent) {
                    relatedIds.add(targetFlowElement.getId());
                }
                if (targetFlowElement.getId().equals(targetId)) {
                    outGoingIds.add(sf.getId());
                } else {
                    newSfs.addAll(((FlowNode) targetFlowElement).getOutgoingFlows());
                }
                checkedSfs.add(sf);
            }

            sequenceFlows = new HashSet<>();
            sequenceFlows.addAll(newSfs);
            sequenceFlows.removeAll(checkedSfs);
            newSfs = new HashSet<>();
            if(CollectionUtils.isEmpty(sequenceFlows)) break;


        } while(true);
        relatedIds.remove(targetId);
        Map<String, Set<String>> info = new HashMap<>();
        info.put("relatedIds", relatedIds);
        info.put("outGoingIds", outGoingIds);
        return info;

    }

}
