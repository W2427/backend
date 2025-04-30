package com.ose.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spring El 表达式工具。
 */
public class SpElUtils {


    /**
     * 注入单一对象
     *
     * @param expr #{user.id}.#{user.name}.#{user.tel}
     * @param data
     * @return
     */
    public static String readExpr(String expr, Object data) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(expr, new TemplateParserContext());
        return expression.getValue(data, String.class);
    }

    /**
     * 注入多个变量
     *
     * @param expr #{#shopId}.#{#typeId}.#{#paging.page}
     * @param map
     * @return
     */
    public static Boolean readExpr(String expr, Map<String, Object> map) {
//        String str = "#STRUCT_WELD_JOINT.mtRatio > 0";
//        String str = "#{1 > 0}";
        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression1 = parser.parseExpression(str, new TemplateParserContext());

        EvaluationContext context = new StandardEvaluationContext();



        for (Map.Entry<String, Object> entry : map.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
            expr = expr.replaceAll("([^_a-zA-Z]|^)("+entry.getKey()+"\\.)([^_a-zA-Z]{0,1})", "$1#" +entry.getKey()+"\\.$3");

        }
        Expression expression = parser.parseExpression(expr, new TemplateParserContext("${","}"));
        try {
            return expression.getValue(context, Boolean.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * 注入多个变量
     *
     * @param expr #{#shopId}.#{#typeId}.#{#paging.page}
     * @param map
     * @return
     */
    public static Boolean processReadExpr(String expr, Map<String, Object> map) {
//        String str = "#STRUCT_WELD_JOINT.mtRatio > 0";
//        String str = "#{1 > 0}";
        if(StringUtils.isEmpty(expr)) return true;
        String originalExpr = new String(expr);
        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression1 = parser.parseExpression(str, new TemplateParserContext());

        EvaluationContext context = new StandardEvaluationContext();

        boolean isPassed = false;

        try {

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
                expr = expr.replaceAll("([^_a-zA-Z]|^)("+entry.getKey()+")([^_a-zA-Z]{0,1})", "$1#" +entry.getKey()+"$3");
                if(!expr.contains("#")) {
                    isPassed = false;
                } else {
                    Expression expression = parser.parseExpression(expr, new TemplateParserContext("${", "}"));
                    isPassed = expression.getValue(context, Boolean.class);
                }
                if(isPassed) {
                    break;
                }
                expr = new String(originalExpr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPassed;
    }

    public static void main(String[] args) {
//        String str = "#{1 > 0}";
        String abc = null;
        String str = "${RESULT == \"ACCEPT\"}";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("RESULT", "ACCEPT");
        map.put("RESULT1", "TODO");
        EvaluationContext context = new StandardEvaluationContext();
        ExpressionParser parser = new SpelExpressionParser();
        String expr = "${RESULT == \"ACCEPT\"}";

        boolean isPassed = false;

        try {

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
                expr = expr.replaceAll("([^_a-zA-Z]|^)("+entry.getKey()+")([^_a-zA-Z]{0,1})", "$1#" +entry.getKey()+"$3");
                Expression expression = parser.parseExpression(expr, new TemplateParserContext("${","}"));
                isPassed = expression.getValue(context, Boolean.class);
                if(isPassed) {
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }




//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression(expr, new TemplateParserContext());
        Expression expression1 = parser.parseExpression(str, new TemplateParserContext());

//        EvaluationContext context = new StandardEvaluationContext();

//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            context.setVariable(entry.getKey(), entry.getValue());
//        }

        expression1.getValue(context, Boolean.class);

        final Pattern CONDITION_EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");
        String testStr = "${abc1}";
        Matcher matcher = CONDITION_EXPRESSION_PATTERN.matcher(testStr);
        if(matcher.find())
            System.out.println(matcher.group(1));
//    }
//        testStr.replaceAll("\\$\\{(.+?)\\}", $1);

        Person person = new Person("tome", 18);

//        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
//        context.setVariable("person", person);                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
//        ExpressionParser parser = new SpelExpressionParser();
//        parser.parseExpression("#person.name").getValue(context, String.class);       // Tom , 属性访问



    }

    public static class Person {
        private String name;

        private Integer age;

        public Person(){

        }

        public Person(String name, Integer age) {
            this.name = name;

            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

}
