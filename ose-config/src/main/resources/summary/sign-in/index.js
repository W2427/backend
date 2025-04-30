'use strict';
(function () {

  if (document.body.className === 'not-compatible') {
    return;
  }

  var messagePanel = document.getElementById('message');

  var kvps = (location.href.split('?')[1] || '').split('&');
  var kvp;
  var target = null;

  for (var i = 0; i < kvps.length; i++) {
    kvp = kvps[i].split('=');
    if (kvp[0] === 'target') {
      target = decodeURIComponent(kvp[1]);
      break;
    }
  }

  window.$signIn = function () {

    messagePanel.innerHTML = '';

    $wget('/eureka/apps', function (result) {

      var apps = ((result || {}).applications || {}).application || [];
      var instances;
      var hosts = [];

      for (var appIndex = 0; appIndex < apps.length; appIndex++) {
        if (apps[appIndex].name !== 'OSE-AUTH') {
          continue;
        }
        instances = apps[appIndex].instance || [];
        for (var instanceIndex = 0; instanceIndex < instances.length; instanceIndex++) {
          hosts.push(instances[instanceIndex].ipAddr + ':' + instances[instanceIndex].port.$);
        }
      }

      if (hosts.length === 0) {
        messagePanel.innerHTML = '尚无认证服务实例注册，请稍候';
        return;
      }

      var xhr = new XMLHttpRequest();

      xhr.addEventListener('load', function (response) {
        var result = JSON.parse(xhr.response || xhr.responseText);
        if (result.success && result.accessToken) {
          localStorage.setItem('access-token', result.accessToken);
          target && (location.href = target);
        } else {
          messagePanel.innerHTML = (result.error || {}).message || '登录失败';
        }
      });

      xhr.open('POST', '/authorizations?hosts=' + encodeURIComponent(hosts.join(',')));
      xhr.setRequestHeader('Content-Type', 'application/json');
      xhr.setRequestHeader('Accept', 'application/json');
      xhr.send(JSON.stringify({
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
      }));

    });

  };

})();
