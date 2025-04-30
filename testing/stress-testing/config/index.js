/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const path = require('path');
const fs = require('fs');
const misc = require('../utils/misc');
const logger = require('../utils/logger');

/**
 * 加载指定路径下的配置文件的内容。
 * @param dir
 */
const loadConfigs = (dir = __dirname) => {

  let files = fs.readdirSync(dir);

  let filepath;

  for (let filename of files) {

    if (path.extname(filename) !== '.json') {
      continue;
    }

    filepath = path.join(dir, filename);

    filename = filename.slice(0, -5);

    exports[filename] = misc.merge(
      exports[filename] || {},
      require(filepath)
    );

  }

};

logger.begin('配置文件加载开始');

logger.debug(`读取 ${process.env.NODE_ENV} 环境的配置文件 ...`);

// 加载通用配置
loadConfigs();

// 加载运行环境配置
loadConfigs(path.join(__dirname, process.env.NODE_ENV));

logger.end('配置文件加载结束');
