package com.ose.tasks.util;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.util.StringUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.impl.util.io.StreamSource;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BPMN 工作流定义文件操作工具。
 */
public class BpmnCommonUtils {


    private static final byte[] BPMN_DIAGRAM_PLACEHOLDER = Base64
        .getDecoder()
        .decode("R0lGODlhAQABAPAAAAAAAAAAACH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sOZ2FtbWE9MC40NTQ1NDUALAAAAAABAAEAAAICRAEAOw==");


    private static final Pattern CONDITION_EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    private static String RUNTIME_GATEWAY_NAME = "RUNTIME_GATEWAY";



    private static final String ENTITY_PROPERTY_REGEXP = "([_a-zA-Z][_a-zA-Z0-9]*)\\.([_a-zA-Z][_a-zA-Z0-9]*)";
    private static final Pattern ENTITY_PROPERTY_PATTERN = Pattern.compile(ENTITY_PROPERTY_REGEXP);
    private static final Pattern VALUES_PATTERN = Pattern.compile("[^\\s+\\-*/!=<>&|()?]+");
    private static final String NUMBER_REGEXP = "^([0-9]+(\\.[0-9]+)?)$";
    private static final String STRING_REGEXP = "^\".*?\"$";
    private static final String KEYWORD_REGEXP = "^(null|true|false)$";
    private static final String METHOD_REGEXP = "^(\"\")?\\.?(equals|name|toString)$";

    private static final String DIAGRAM_PROCESS_KEY = "WORKFLOW_DIAGRAM";


    public static final Map<String, Object> EVALUATION_PARAMETERS_PIPING = new HashMap<>();

    public static final Map<String, Object> EVALUATION_PARAMETERS_STRUCTURE = new HashMap<>();

    public static final Map<String, Object> EVALUATION_PARAMETERS_DESIGN = new HashMap<>();


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
        String funcPart
    ) {
        try {
            Map<String, Object> parameter = new HashMap<>();
            setEntityParameter(parameter, entityType.newInstance(), entityType);
            if (funcPart == "PIPING") {
                EVALUATION_PARAMETERS_PIPING.put(key, parameter);
            } else if (funcPart == "STRUCTURE") {
                EVALUATION_PARAMETERS_STRUCTURE.put(key, parameter);
            } else {
                EVALUATION_PARAMETERS_DESIGN.put(key, parameter);
            }

        } catch (InstantiationException | IllegalAccessException e) {

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

            }
        }

        if (type.getSuperclass() != null) {
            setEntityParameter(parameter, entity, type.getSuperclass());
        }

    }


    static {
        setEntityParameter("ISO", ISOEntity.class, "PIPING");
        setEntityParameter("SPOOL", SpoolEntity.class, "PIPING");
        setEntityParameter("PIPE_PIECE", PipePieceEntity.class, "PIPING");
        setEntityParameter("WELD_JOINT", WeldEntity.class, "PIPING");
        setEntityParameter("COMPONENT", ComponentEntity.class, "PIPING");
        setEntityParameter("PRESSURE_TEST_PACKAGE", PressureTestPackageEntityBase.class, "PIPING");
        setEntityParameter("CLEAN_PACKAGE", CleanPackageEntityBase.class, "PIPING");
        setEntityParameter("SUB_SYSTEM", SubSystemEntityBase.class, "PIPING");


        setEntityParameter("DOC", Drawing.class, "ENGINEERING");

    }

    public static void validateConditionExpressions(final File bpmnFile, final String funcPart) {

        List<ValidationError> errors = new ArrayList<>();

        try {


            Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(bpmnFile.getAbsolutePath());


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
                        "条件【%s】的表达式【%s】F格式不正确",
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
                    if (funcPart == "PIPING") {
                        variableMap = BpmnCommonUtils.EVALUATION_PARAMETERS_PIPING;
                    } else if (funcPart == "STRUCTURE") {
                        variableMap = BpmnCommonUtils.EVALUATION_PARAMETERS_STRUCTURE;
                    } else {
                        variableMap = BpmnCommonUtils.EVALUATION_PARAMETERS_DESIGN;
                    }

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
            throw new BusinessError();
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


            Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(bpmnFile.getAbsolutePath());


            NodeList nodes = doc.getElementsByTagName("process");


            if (nodes.getLength() != 1) {
                throw new ValidationError("error.bpmn.only-one-process-should-be-defined");
            }


            nodes
                .item(0)
                .getAttributes()
                .getNamedItem("id")
                .setNodeValue(processDefinitionKey);


            TransformerFactory
                .newInstance()
                .newTransformer()
                .transform(new DOMSource(doc), new StreamResult(bpmnFile));

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new BusinessError();
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



        return (new DefaultProcessDiagramGenerator())

            .generateDiagram(bpmnModel, "jpg", Collections.<String>emptyList(), Collections.<String>emptyList(), "宋体", "微软雅黑", "黑体", null, 1.0);




    }



    /**
     * 根据bpmn文件流 返回BpmnModel
     * @param fileStream    bpmn文件流
     * @return              BpmnModel
     */
    public static BpmnModel readBpmnFile(InputStream fileStream){

        StreamSource xmlSource = new InputStreamSource(fileStream);
        return new BpmnXMLConverter().convertToBpmnModel(xmlSource,
            false, false, "utf-8");
    }



}
