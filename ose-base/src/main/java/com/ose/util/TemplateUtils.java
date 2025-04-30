package com.ose.util;

import com.ose.exception.ValidationError;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文本模版工具（基于 Apache Velocity）。
 */
public class TemplateUtils {

    // 初始化模版引擎
    static {
        Velocity.init();
    }

    // 取得模版引擎所需的运行时服务
    private static final RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();

    /**
     * 将参数对象设置到上下文对象中。
     *
     * @param context    上下文对象
     * @param parameters 参数对象
     * @param clazz      参数对象的类型或超类
     */
    private static void setContext(
        final VelocityContext context,
        final Object parameters,
        final Class clazz
    ) {

        // 读取在超类中定义的属性
        if (clazz.getSuperclass() != null) {
            setContext(context, parameters, clazz.getSuperclass());
        }

        // 读取声明的属性
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                context.put(field.getName(), field.get(parameters));
            } catch (IllegalAccessException e) {
                // nothing to do
            }
        }

    }

    /**
     * 将参数对象设置到上下文对象中。
     *
     * @param context    上下文对象
     * @param parameters 参数对象
     */
    private static void setContext(
        final VelocityContext context,
        final Object parameters
    ) {

        if (parameters == null) {
            return;
        }

        // 当参数对象为 Map 的实例时，将 Map 的键值对设置到上下文对象中并结束
        if (parameters instanceof Map) {

            Map<?, ?> map = (Map) parameters;

            map.keySet().forEach(key -> context.put(key + "", map.get(key)));

            return;
        }

        // 否则，当参数对象为 Iterable 的实例时，将参数对象作为名为 items 的参数设置到上下文对象中
        if (parameters instanceof Iterable) {

            List<Object> items = new ArrayList<>();

            for (Object item : (Iterable) parameters) {
                items.add(item);
            }

            context.put("items", items);

            return;
        }

        // 否则，将参数对象的各属性设置到上下文对象中
        setContext(context, parameters, parameters.getClass());
    }

    /**
     * 校验并取得通过校验的模版实例。
     *
     * @param reader Reader
     * @return 模版实例
     */
    private static Template validate(Reader reader) {

        Template template = new Template();

        template.setRuntimeServices(runtimeServices);

        try {
            template.setData(runtimeServices.parse(reader, template));
        } catch (ParseException e) {
            throw new ValidationError(e.getMessage());
        }

        return template;
    }

    /**
     * 校验并取得通过校验的模版实例。
     *
     * @param templateContent 模版内容
     */
    public static void validate(String templateContent) {

        if (templateContent == null) {
            return;
        }

        validate(new StringReader(templateContent));
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName 输出到日志中的模版名称
     * @param templateStream  模版文件输入流
     * @param parameters      参数对象
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final InputStream templateStream,
        final Object parameters
    ) {
        return render(
            templateLogName,
            templateStream,
            parameters,
            null
        );
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName      输出到日志中的模版名称
     * @param templateStream       模版文件输入流
     * @param additionalParameters 附加参数
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final InputStream templateStream,
        final Map<String, Object> additionalParameters
    ) {
        return render(
            templateLogName,
            templateStream,
            null,
            additionalParameters
        );
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName      输出到日志中的模版名称
     * @param templateStream       模版文件输入流
     * @param parameters           参数对象
     * @param additionalParameters 附加参数
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final InputStream templateStream,
        final Object parameters,
        final Map<String, Object> additionalParameters
    ) {
        return render(
            templateLogName,
            new InputStreamReader(templateStream),
            parameters,
            additionalParameters
        );
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName 输出到日志中的模版名称
     * @param templateContent 模版内容
     * @param parameters      参数对象
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final String templateContent,
        final Object parameters
    ) {
        return render(templateLogName, templateContent, parameters, null);
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName 输出到日志中的模版名称
     * @param templateContent 模版内容
     * @param parameters      参数对象
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final String templateContent,
        final Map<String, Object> parameters
    ) {
        return render(templateLogName, templateContent, null, parameters);
    }

    /**
     * 使用参数对象渲染模版。
     * 若 parameters 为 Map 的实例，那么 parameters 的值将通过值的键引用；
     * 否则，若 parameters 为 Iterable 的实例，那么 parameters 将被转为 List，并命名为 items；
     * 否则，parameters 的各属性将被作为参数，并通过属性名称引用。
     *
     * @param templateLogName      输出到日志中的模版名称
     * @param templateContent      模版内容
     * @param parameters           参数对象
     * @param additionalParameters 附加参数
     * @return 渲染结果文本
     */
    public static String render(
        final String templateLogName,
        final String templateContent,
        final Object parameters,
        final Map<String, Object> additionalParameters
    ) {
        return render(
            templateLogName,
            new StringReader(templateContent),
            parameters,
            additionalParameters
        );
    }

    /**
     * 使用参数对象渲染模版。
     *
     * @param templateLogName      输出到日志中的模版名称
     * @param reader               Reader
     * @param parameters           参数对象
     * @param additionalParameters 附加参数
     * @return 渲染结果文本
     */
    private static String render(
        final String templateLogName,
        final Reader reader,
        final Object parameters,
        final Map<String, Object> additionalParameters
    ) {

        VelocityContext context = new VelocityContext();
        setContext(context, parameters);
        setContext(context, additionalParameters);

        StringWriter writer = new StringWriter();

        Template template = validate(reader);
        template.initDocument();
        template.merge(context, writer);

        Velocity.evaluate(context, writer, templateLogName, reader);

        return writer.toString();
    }

}
