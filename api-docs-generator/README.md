# 生成 OSE Web API 文档

0. 执行 src/main/java/ApiDocGenerator.java，生成 ASCII Docs。

0. 通过终端在当前路径执行以下命令

    ```bash
    $ mvn
    ```

    > 初次生成 PDF 需要通过 `gem-maven-plugin` 插件下载 CJK 字体，再次生成时可将 `pom.xml` 文件中的 `gem-maven-plugin` 插件注掉。
