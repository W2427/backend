'use strict';
(function () {

  var $reqCount = window.$reqCount = 0;
  var accessToken = localStorage.getItem('access-token');

  /**
   * 发送 GET 请求。
   * @param url      请求 URL
   * @param callback 回调函数
   */
  window.$wget = function (url, callback) {

    $reqCount++;

    var xhr = new XMLHttpRequest();

    xhr.addEventListener('load', function () {
      $reqCount--;
      callback(JSON.parse(xhr.response || xhr.responseText), xhr.status);
    });

    xhr.open('GET', url);
    accessToken && xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.send();
  };

  /**
   * 发送 POST 请求。
   * @param url      请求 URL
   * @param callback 回调函数
   */
  window.$post = function (url, callback) {

    $reqCount++;

    var xhr = new XMLHttpRequest();

    xhr.addEventListener('load', function () {
      $reqCount--;
      callback(JSON.parse(xhr.response || xhr.responseText), xhr.status);
    });

    xhr.open('POST', url);
    accessToken && xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.send();
  };

  /**
   * 执行任务队列。
   * @param tasks    任务队列
   * @param callback 回调函数
   */
  var $queue = window.$queue = function (tasks, callback) {

    if (tasks.length === 0) {
      return callback ? callback() : undefined;
    }

    var task = tasks.shift();

    task(function () {
      $queue(tasks);
    });
  };

  /**
   * 格式化时间。
   * @param time 时间
   * @returns {string} 时间字符串
   */
  window.$datetime = function (time) {
    if (isNaN(time)) {
      return '';
    }
    var date = new Date(time);
    return date.getFullYear()
      + '-' + ('0' + (date.getMonth() + 1)).slice(-2)
      + '-' + ('0' + date.getDate()).slice(-2)
      + ' ' + ('0' + date.getHours()).slice(-2)
      + ':' + ('0' + date.getMinutes()).slice(-2)
      + ':' + ('0' + date.getSeconds()).slice(-2);
  };

  /**
   * 取得对象的所有属性。
   */
  window.$keys = Object.keys || function (object) {
    var keys = [];
    for (var key in object) {
      if (!object.hasOwnProperty(key)) {
        continue;
      }
      keys.push(key);
    }
    return keys;
  };

})();
