/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const is = require('../utils/is');
const http = require('http');
const HOST_NAME_PATTERN = /^(([^\/:?#]+:)?\/\/)?([^\/:?#]+?)(:(\d+))?([\/?#]|$)/;
const REQUEST_HOST = Symbol.for('host');
const REQUEST_PORT = Symbol.for('port');
const REQUEST_METHOD = Symbol.for('method');
const REQUEST_PATH = Symbol.for('path');
const REQUEST_PARAMS = Symbol.for('params');
const REQUEST_QUERY = Symbol.for('query');
const REQUEST_HEADERS = Symbol.for('headers');
const REQUEST_BODY = Symbol.for('body');
const REQ_START_AT = Symbol.for('startAt');

/**
 * 设置键值对对象。
 * @param {*} value
 * @param {[string]} namespace
 * @param {object} kvps
 * @returns {object}
 */
const toKVPs = (value, namespace = [], kvps = {}) => {

  if (!is.string(value) && is.empty(value)) {
    return kvps;
  }

  // 将日期转为 ISO 格式
  if (is.date(value)) {
    value = value.toISOString();
  }

  // 若为原始数据类型则设置到键值对对象中
  if (is.primitive(value)) {
    let key = encodeURIComponent(namespace.join('.'));
    kvps[key] = kvps[key] || [];
    kvps[key].push(encodeURIComponent(value));
  // 若为数组则递归处理数组的元素
  } else if (is.array(value)) {
    value.forEach(item => {
      toKVPs(item, namespace, kvps);
    });
  // 若为对象则递归处理对象的属性
  } else if (is.object(value)) {
    Object.keys(value).forEach(key => {
      toKVPs(value[key], namespace.concat(key), kvps);
    });
  }

  return kvps;
};

/**
 * 将对象转为查询字符串。
 * @param {object} object
 * @returns {string}
 */
const toQueryString = object => {

  const kvps = toKVPs(object);
  const array = [];

  Object.keys(kvps).forEach(key => kvps[key].forEach(value => array.push(`${key}=${value}`)));

  if (array.length === 0) {
    return '';
  }

  return '?' + array.join('&');
};

/**
 * 发送 HTTP 请求。
 * @param host 服务器地址
 * @param port 服务器端口号
 * @param method 请求方法
 * @param path 请求路径
 * @param [params] 路径参数
 * @param [query] 查询参数
 * @param [headers] 请求头
 * @param [body] 请求数据
 * @returns {Promise<ServerResponse>}
 */
const sendHttpRequest = (host, port, method, path, params = {}, query = {}, headers = {}, body) => {

  // 设置路径参数
  is.object(params) && Object.keys(params).forEach(key => {
    path = path.replace(new RegExp(`{${key}}`, 'g'), encodeURIComponent(params[key]));
  });

  // 设置查询参数
  is.object(query) && (path += toQueryString(query));

  headers['User-Agent'] = headers['User-Agent'] || 'Mozilla/5.0 (OSE_Stress_Test/1.0)';
  headers['Content-Type'] = 'application/json';

  // 发送 HTTP 请求
  return new Promise(resolve => {

    const json = body ? JSON.stringify(body) : null;

    const startAt = Date.now();

    const req = http.request(
      {host, method, port, path, headers},
      res => {
        res[REQ_START_AT] = startAt;
        //res.setEncoding('utf8');
        resolve(res);
      }
    );

    if (json) {
      req.write(json);
      req.end();
    }

  });

};

/**
 * HTTP 客户端。
 */
class HttpClient {

  /**
   * 构造函数。
   * @param host 服务器地址
   * @param [port=80] 服务器端口号
   */
  constructor(host, port = 80) {

    const hostname = host.match(HOST_NAME_PATTERN);

    if (!hostname) {
      throw new Error('服务器地址格式不正确');
    }

    this[REQUEST_HOST] = hostname[3];
    this[REQUEST_PORT] = hostname[5] || port;
    this[REQUEST_METHOD] = 'GET';
    this[REQUEST_PATH] = '/';
    this[REQUEST_PARAMS] = {};
    this[REQUEST_QUERY] = {};
    this[REQUEST_HEADERS] = {};
    this[REQUEST_BODY] = {};
  }

  /**
   * 设置请求方法。
   * @param method
   * @returns {HttpClient}
   */
  method(method) {
    this[REQUEST_METHOD] = method.toUpperCase();
    return this;
  }

  /**
   * 设置请求路径。
   * @param path
   * @param [params]
   * @returns {HttpClient}
   */
  path(path, params) {
    this[REQUEST_PATH] = path;
    this[REQUEST_PARAMS] = is.object(params) ? params : null;
    return this;
  }

  /**
   * 设置查询参数。
   * @param query
   * @returns {HttpClient}
   */
  query(query) {
    this[REQUEST_QUERY] = is.object(query) ? query : null;
    return this;
  }

  /**
   * 设置请求头。
   * @param key 请求头名称
   * @param [value] 请求头值
   * @returns {HttpClient|string}
   */
  header(key, value) {

    if (is.null(value)) {
      delete this[REQUEST_HEADERS][key];
      return this;
    }

    if (is.undefined(value)) {
      return this[REQUEST_HEADERS][key];
    }

    this[REQUEST_HEADERS][key] = is.date(value) ? value.toUTCString() : ('' + value);

    return this;
  }

  /**
   * 设置认证信息。
   * @param {string} accessToken 访问令牌
   * @param {string} [type=Bearer] 访问令牌类型
   * @returns {HttpClient}
   */
  authorization(accessToken, type = 'Bearer') {
    return this.header('Authorization', `${type ? `${type} ` : ''}${accessToken}`);
  }

  /**
   * 设置请求数据。
   * @param body
   * @returns {HttpClient}
   */
  body(body) {
    this[REQUEST_BODY] = is.object(body) ? body : null;
    return this;
  }

  /**
   * 设置请求。
   * @param {string} method 请求方法
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @param {object} [body] 请求数据
   * @returns {HttpClient}
   */
  request(method, path, params, query, body) {
    this.method(method);
    this.path(path, params);
    this.query(query);
    this.body(body);
    return this;
  }

  /**
   * 发送 GET 请求。
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @returns {HttpClient}
   */
  get(path, params, query) {
    return this.request('GET', path, params, query);
  }

  /**
   * 发送 POST 请求。
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @param {object} [body] 请求数据
   * @returns {HttpClient}
   */
  post(path, params, query, body) {
    return this.request('POST', path, params, query, body);
  }

  /**
   * 发送 PUT 请求。
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @param {object} [body] 请求数据
   * @returns {HttpClient}
   */
  put(path, params, query, body) {
    return this.request('PUT', path, params, query, body);
  }

  /**
   * 发送 PATCH 请求。
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @param {object} [body] 请求数据
   * @returns {HttpClient}
   */
  patch(path, params, query, body) {
    return this.request('PATCH', path, params, query, body);
  }

  /**
   * 发送 DELETE 请求。
   * @param {string} path 请求路径
   * @param {object} [params] 路径参数
   * @param {object} [query] 查询参数
   * @param {object} [body] 请求数据
   * @returns {HttpClient}
   */
  delete(path, params, query, body) {
    return this.request('DELETE', path, params, query, body);
  }

  /**
   * 发送 HTTP 请求。
   * @returns {Promise<ServerResponse>}
   */
  send() {
    return sendHttpRequest(
      this[REQUEST_HOST],
      this[REQUEST_PORT],
      this[REQUEST_METHOD],
      this[REQUEST_PATH],
      this[REQUEST_PARAMS],
      this[REQUEST_QUERY],
      this[REQUEST_HEADERS],
      this[REQUEST_BODY]
    );
  }

}

module.exports = HttpClient;
