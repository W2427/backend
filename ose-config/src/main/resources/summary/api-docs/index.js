'use strict';
(function () {

  if (document.body.className === 'not-compatible') {
    return;
  }

  var API_DOC_TEMPLATE = document.getElementById('api-doc-template').innerText;
  var HTTP_METHODS = ['post', 'get', 'patch', 'put', 'delete'];

  // 取得应用列表
  $wget('/eureka/apps', function (result) {

    var apps = [];

    // 从已注册应用实例中取得运行中的应用实例
    result.applications.application.forEach(function (application) {

      var instance;

      for (var i = 0; i < application.instance.length; i++) {

        instance = application.instance[i];

        if (instance.status === 'UP' && instance.app !== 'OSE-CONFIG') {
          apps.push(instance);
          return;
        }

      }

    });

    if (apps.length === 0) {
      document.write('尚无服务完成注册，请稍后重试。');
      return;
    }

    var docs = [];

    // 设置取得的文档信息
    function reformApiDoc(doc) {

      var controllerMap = {};

      doc.controllers = [];
      doc.routeCount = 0;

      doc.tags.forEach(function (tag) {
        tag.routes = [];
        tag.routeCount = 0;
        controllerMap[tag.name] = tag;
        doc.controllers.push(tag);
      });

      doc.controllers.sort(function (a, b) {
        return a.name.localeCompare(b.name);
      });

      for (var path in doc.paths) {

        if (!doc.paths.hasOwnProperty(path)) {
          continue;
        }

        for (var method in doc.paths[path]) {

          if (!doc.paths[path].hasOwnProperty(method)) {
            continue;
          }

          var route = doc.paths[path][method];
          route.method = method;
          route.path = path;
          route.pathHTML = path.replace(/({.+?})/g, '<code>$1</code>');
          route.weight = path + ':' + HTTP_METHODS.indexOf(method.toLowerCase());

          route.tags.forEach(function (controllerName) {
            var controller;
            if (controller = controllerMap[controllerName]) {
              controller.routeCount++;
              controller.routes.push(route);
            }
          });

          doc.routeCount++;
        }

      }

      doc.controllers.forEach(function (controller) {
        controller.routes.sort(function (a, b) {
          return a.weight.localeCompare(b.weight);
        });
      });

    }

    var routeCountPanel = document.getElementById('route-count');
    var showInterfacesToggle = document.getElementById('show-interfaces');
    var appsPanel = document.querySelector('nav > p');
    var showAppToggles = [];
    var apiDocsPanel = document.getElementById('api-docs');
    var routeCount = 0;
    var initialized = false;

    // 加载已保存的是否显示接口信息的选项
    var showInterfaces = (function () {

      if (!window.localStorage) {
        return null;
      }

      try {
        return JSON.parse(localStorage.getItem('show-interfaces')) === true;
      } catch (e) {
        return null;
      }

    })();

    // 加载已保存的选择的应用列表
    var savedSelectedApps = (function () {

      if (!window.localStorage) {
        return null;
      }

      try {
        return JSON.parse(localStorage.getItem('selected-sub-systems'));
      } catch (e) {
        return null;
      }

    })();

    // 保存视图
    function saveView() {

      if (!window.localStorage) {
        return;
      }

      localStorage.setItem('show-interfaces', JSON.stringify(showInterfacesToggle.checked));

      var selectedSubSystems = [];

      showAppToggles.forEach(function (checkbox) {
        if (checkbox.checked) {
          selectedSubSystems.push(checkbox.id);
        }
      });

      localStorage.setItem('selected-sub-systems', JSON.stringify(selectedSubSystems));
    }

    // 生成 API 文档
    function renderApiDocs() {

      if (!initialized) {

        showInterfacesToggle.checked = showInterfaces === true;

        showAppToggles = [];
        appsPanel.innerHTML = '';
        routeCount = 0;

        docs.sort(function (a, b) {
          return a.host.localeCompare(b.host);
        });

        docs.forEach(function (doc) {

          if (!doc.info) {
            return;
          }

          var checkBoxElement = document.createElement('INPUT');
          checkBoxElement.type = 'checkbox';
          checkBoxElement.id = 'show-' + doc.instance.app;
          checkBoxElement.name = 'show-sub-system';
          checkBoxElement.value = doc.instance.app;
          checkBoxElement.checked = savedSelectedApps === null ? true : (savedSelectedApps.indexOf(checkBoxElement.id) >= 0);
          checkBoxElement.disabled = true;
          showAppToggles.push(checkBoxElement);

          var spanElement = document.createElement('span');
          spanElement.innerText = doc.info.title;

          var labelElement = document.createElement('LABEL');
          labelElement.setAttribute('for', checkBoxElement.id);
          labelElement.appendChild(checkBoxElement);
          labelElement.appendChild(spanElement);

          appsPanel.appendChild(labelElement);

          reformApiDoc(doc);

          routeCount += doc.routeCount;
        });

        routeCountPanel.innerText = '' + routeCount;

        if (apps.length === docs.length) {

          showInterfacesToggle.disabled = false;

          showAppToggles.forEach(function (checkBoxElement) {

            checkBoxElement.addEventListener('change', function () {
              renderApiDocs();
              saveView();
            });

            checkBoxElement.disabled = false;
          });

          initialized = true;
        }

      }

      var selectedApps = [];

      showAppToggles.forEach(function (toggle) {
        if (toggle.checked) {
          selectedApps.push(toggle.value);
        }
      });

      apiDocsPanel.innerHTML = ejs.render(API_DOC_TEMPLATE, {
        docs: docs,
        routeCount: routeCount,
        showInterfaces: showInterfacesToggle.checked,
        selectedApps: selectedApps
      });

      setTimeout(function () {

        var headPanel = document.querySelector('#api-docs > div.head');
        var contentPanel = document.querySelector('#api-docs > div.content');

        contentPanel.addEventListener('scroll', function () {
          headPanel.scrollLeft = this.scrollLeft;
        });

      }, 5);

    }

    var tasks = [];

    // 取得各子系统应用的 API 文档信息
    apps.forEach(function (app) {

      var ipAddr = app.ipAddr;
      var port = app.port.$;

      if (ipAddr.match(/^(10|192\.168|172\.(1[6-9]|2[0-9]|31))\./)
        || ipAddr === '127.0.0.1'
        || ipAddr === 'localhost') {
        ipAddr = location.href.match(/^(https?:\/\/)([^:\/?#]+)/)[2];
      }

      tasks.push(function (next) {
        $wget('//' + ipAddr + ':' + port + '/docs/swagger.json', function (result) {
          result.host = ipAddr + ':' + port;
          result.instance = app;
          docs.push(result);
          renderApiDocs();
          next();
        });
      });

    });

    $queue(tasks);

    showInterfacesToggle.addEventListener('change', function () {
      saveView();
      renderApiDocs();
    });

  });

})();
