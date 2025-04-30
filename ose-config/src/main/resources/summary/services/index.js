'use strict';
(function () {

  if (document.body.className === 'not-compatible') {
    return;
  }

  var accessToken = localStorage.getItem('access-token');

  function gotoSignInPage(statusCode) {
    location.href = '/summary/sign-in/?target=' + encodeURIComponent(location.href);
  }

  if (!accessToken) {
    return gotoSignInPage(401);
  }

  var progressBar = document.getElementById('progress');
  var MINUTE = 60000;
  var SERVICE_TEMPLATE = document.getElementById('service-template').innerText;

  /**
   * 刷新倒计时。
   */
  function countDown() {

    var startAt = Date.now();
    var from = (startAt + 30000) % MINUTE;
    var timeout = MINUTE - from;

    var interval = setInterval(function () {

      var elapsed = Date.now() - startAt;

      if (elapsed > timeout) {
        clearInterval(interval);
        //refresh();
        location.reload(true);
        return;
      }

      var cursorPosition = Math.max((from + elapsed) * 100 / MINUTE, 2);

      progressBar.style.background = 'linear-gradient(90deg, #3F9FFF, #005FFF, '
        + (cursorPosition - 1).toFixed(2)
        + '%, #FFFFFF,'
        + cursorPosition.toFixed(2)
        + '%, #000000,'
        + cursorPosition.toFixed(2)
        + '%, #000000)';

    }, 50);

  }

  /**
   * 刷新服务列表。
   */
  var refresh = window.$refresh = function () {

    // 取得应用列表
    $wget('/eureka/apps', function (result) {

      var SERVICES = {
        'OSE-AUTH': '认证/授权/组织管理',
        'OSE-DOCS': '文档上传与查询',
        'OSE-NOTIFICATIONS': '通知',
        'OSE-REPORT': '报表/统计',
        'OSE-MATERIALSPM': '材料管理',
        'OSE-ISSUES': '问题管理',
        'OSE-BPM': '工作流',
        'OSE-TASKS': '项目计划管理',
      };

      var apps = (result['applications'] || {})['application'] || [];
      var appIndex;
      var app;
      var instances;
      var instanceIndex;
      var instance;
      var serviceName;
      var allInstances = [];

      for (appIndex = 0; appIndex < apps.length; appIndex++) {

        app = apps[appIndex];
        app.code = app.name;
        instances = app.instances = app['instance'] || [];
        instances.sort(function (a, b) {
          return (a.ipAddr + a.port.$).localeCompare(b.ipAddr + b.port.$);
        });
        app.port = ((instances[0] || {})['port'] || {})['$'];
        app.instanceCount = instances.length;
        delete app['instance'];

        for (instanceIndex = 0; instanceIndex < instances.length; instanceIndex++) {
          instance = instances[instanceIndex];
          instance.$id = instance['ipAddr'] + ':' + instance['port']['$'];
          instance.$_id = instance.$id.replace(/\W/g, '-');
          instance.$requestCountDomId = 'instance-request-count-' + instance.$_id;
          instance.$serverTimeDomId = 'instance-server-time-' + instance.$_id;
          instance.$statusDomId = 'instance-status-' + instance.$_id;
        }

        allInstances = allInstances.concat(instances);

        serviceName = SERVICES[app.code];

        if (serviceName) {
          app.name = serviceName;
          delete SERVICES[app.code];
        }

      }

      var serviceCodes = $keys(SERVICES);

      if (serviceCodes.length > 0) {
        for (appIndex = 0; appIndex < serviceCodes.length; appIndex++) {
          apps.push({
            code: serviceCodes[appIndex],
            name: SERVICES[serviceCodes[appIndex]],
            instanceCount: 0,
            instances: [{
              port: {},
              leaseInfo: {}
            }]
          });
        }
      }

      // 将服务根据端口号排序
      apps.sort(function (a, b) {
        return a.port - b.port;
      });

      // 生成服务列表
      document.getElementById('services').innerHTML = ejs.render(
        SERVICE_TEMPLATE,
        {apps: apps}
      );

      // 取得实例状态
      for (instanceIndex = 0; instanceIndex < allInstances.length; instanceIndex++) {
        (function (instance) {
          $wget(
            '/server/status?hosts=' + encodeURIComponent(instance.$id),
            function (result, statusCode) {

              if (statusCode === 401 || statusCode === 403) {
                return gotoSignInPage();
              }

              var status = result[instance.$id].data || {};
              var serverTime = (new Date(status.serverTime)).getTime();
              var statusCell = document.getElementById(instance.$statusDomId);

              if (status.closing === false && status.suspended === false) {
                statusCell.className = 'normal';
                statusCell.innerHTML = '正常';
              } else if (status.closing === true) {
                statusCell.className = 'closing';
                statusCell.innerHTML = '准备关闭';
              } else if (status.suspended === true) {
                statusCell.className = 'suspending';
                statusCell.innerHTML = '暂停';
              } else {
                statusCell.className = 'unknown';
                statusCell.innerHTML = '未知';
              }

              document.getElementById(instance.$requestCountDomId).innerHTML = isNaN(status.requestCount)
                ? '&nbsp;'
                : status.requestCount;

              document.getElementById(instance.$serverTimeDomId).innerHTML = isNaN(serverTime)
                ? '&nbsp;'
                : ($datetime(serverTime).slice(11) + '.' + ('00' + (serverTime % 1000)).slice(-3));

            }
          );
        })(allInstances[instanceIndex]);
      }

      countDown();
    });

  };

  window.$instance = {
    restore: function (host) {
      $post('/server/restore?hosts=' + encodeURIComponent(host), function (result, statusCode) {
        if (statusCode === 401 || statusCode === 403) {
          return gotoSignInPage(statusCode);
        }
        if ((result[host] || {}).success && window.$reqCount === 0) {
          location.reload(true);
        }
      });
    },
    suspend: function (host) {
      $post('/server/suspend?hosts=' + encodeURIComponent(host), function (result, statusCode) {
        if (statusCode === 401 || statusCode === 403) {
          return gotoSignInPage(statusCode);
        }
        if ((result[host] || {}).success && window.$reqCount === 0) {
          location.reload(true);
        }
      });
    },
    shutdown: function (host) {
      $post('/server/shutdown?hosts=' + encodeURIComponent(host), function (result, statusCode) {
        if (statusCode === 401 || statusCode === 403) {
          return gotoSignInPage(statusCode);
        }
        if ((result[host] || {}).success && window.$reqCount === 0) {
          location.reload(true);
        }
      });
    }
  };

  refresh();
})();
