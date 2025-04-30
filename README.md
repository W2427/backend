# 代码发布说明

> 操作前说明
> * 详细的服务器环境部署说明请参考当前路径下 `./docs/specs/deployment-azure.xlsx` 中的相关内容
> * 请先查看本文提到的相关脚本的内容

0. 执行以下命令编译源代码

    ```bash
    $ mvn clean install
    ```

0. 执行以下脚本将编译生成的报表模板和 JAR 文件打包

    ```bash
    $ ./release.sh
    ```

    > 打包文件将保存到 `./target/` 下。

0. 执行以下脚本将上述文件上传到生产环境服务器的临时路径下

    ```bash
    $ ./publish_hongkong.sh
    ```

    > 该脚本在上传完成后将执行以下操作
    > * 将报表模版解压到服务器的 `/mnt/mfs/resources/templates/reports/` 下
    > * 将 JAR 文件移动到服务器的 `/mnt/mfs/ose-jars/` 下

0. 确保生产环境数据库中的表、视图、存储过程等已更新

0. 通过浏览器访问[ OSE 服务运行状态一览表](http://ose.ose.com:8801/summary/services/)，根据操作说明停止准备重新启动的服务

    > 必须使用 `super` 用户登录。

    > 若点击【停止】按钮后服务一直处于【准备关闭】的状态，需要登录到相应的服务，并执行以下命令
    >
    > ```bash
    > # cd /var/ose/
    > # ./ose.sh stop tasks
    > ```
    >
    > 其中 `tasks` 为存在更新并准备重新发布的服务代码。
    >
    > 服务代码可选值参考通过执行以下命令返回的说明
    >
    > ```bash
    > # ./ose.sh
    > ```
    >
    > 被停止的服务将会被每分钟执行的定时任务重新启动。

    > 若被关闭的服务一直无法重新启动，则需要调查原因。
    >
    > 期间可尝试通过以下命令手动重启（为避免与定时任务冲突，需要先关闭定时任务服务）
    >
    > ```bash
    > # systemctl stop crond
    > # ./ose.sh start tasks
    > ```
    >
    > 并在服务成功启动后重新启动定时任务服务
    >
    > ```bash
    > # systemctl start crond
    > ```
