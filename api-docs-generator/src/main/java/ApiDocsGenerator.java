import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * OSE 系统 API 文档生成工具。
 */
public class ApiDocsGenerator {

    /**
     * 生成 API 文档。
     * @param args 命令参数
     * @throws MalformedURLException URL 无效错误
     */
    public static void main(String[] args) throws MalformedURLException {
        generateApiDocs("auth",          "http://ose.ose.com:8811/docs/swagger.json");
        generateApiDocs("docs",          "http://ose-doc.ose.com:8821/docs/swagger.json");
        generateApiDocs("notifications", "http://ose.ose.com:8831/docs/swagger.json");
        generateApiDocs("report",        "http://ose.ose.com:8841/docs/swagger.json");
        generateApiDocs("material",      "http://ose.ose.com:8851/docs/swagger.json");
        generateApiDocs("issues",        "http://ose.ose.com:8861/docs/swagger.json");
        generateApiDocs("bpm",           "http://ose-task.ose.com:8881/docs/swagger.json");
        generateApiDocs("tasks",         "http://ose-task.ose.com:8891/docs/swagger.json");
    }

    // Swagger2Markup 配置：生成 ASCII DOC
    private static final Swagger2MarkupConfig CONFIG = (new Swagger2MarkupConfigBuilder())
        .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
        .withOutputLanguage(Language.ZH)
        .build();

    /**
     * 生成 ASCII Doc。
     * @param code       服务代码
     * @param swaggerURL Swagger JSON 文件 URL
     * @throws MalformedURLException URL 无效错误
     */
    private static void generateApiDocs(
        final String code,
        final String swaggerURL
    ) throws MalformedURLException {
        System.out.println(String.format(
            "Generating %s API docs from %s",
            ("“" + code + "”             ").substring(0, 15),
            swaggerURL
        ));
        Swagger2MarkupConverter
            .from(new URL(swaggerURL))
            .withConfig(CONFIG)
            .build()
            .toFolder(Paths.get("api-docs-generator/src/docs/asciidoc/" + code));
    }

}
