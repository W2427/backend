'use strict';
(function() {

  if (document.body.className === 'not-compatible') {
    return;
  }

  var agents = {};

  (navigator.userAgent.match(/[^\s]+\/\d+(\.\d+)*/g) || []).forEach(function(agent) {
    var nameAndVersion = agent.match(/^([^\s]+)\/(\d+(\.\d+)*)$/);
    agents[nameAndVersion[1]] = nameAndVersion[2];
  });

  var agentClassNames = [];

  if (agents['AppleWebKit']) {
    agentClassNames.push('webkit');
  }

  if (agents['Chrome']) {
    agentClassNames.push('chrome');
  }

  if (agents['Edge']) {
    agentClassNames.push('ms-edge');
  }

  if (agents['Trident']) {

    agentClassNames.push('ms-ie');

    var msie = navigator.userAgent.match(/rv:\d+|msie\s+\d+/gi);

    if (msie) {
      agents['MSIE'] = msie[0].match(/\d+/)[0];
      agentClassNames.push('ms-ie-' + agents['MSIE']);
    }

  }

  if (!agents['Chrome'] && agents['Safari']) {
    agentClassNames.push('safari');
  }

  document.body.parentElement.setAttribute('class', agentClassNames.join(' '));

  var initialized = false;

  (function() {

    /**
     * 显示/隐藏路由列表。
     */
    window.showRoutes = function(event) {

      var controllerNamePanel = event.target;

      if (controllerNamePanel.tagName !== 'A') {
        controllerNamePanel = controllerNamePanel.parentElement;
      }

      var controllerPanel = controllerNamePanel.parentElement;

      controllerPanel.showRoutes = !controllerPanel.showRoutes;

      if (controllerPanel.showRoutes) {
        controllerPanel.className = 'show-routes';
      } else {
        controllerPanel.className = 'hide-routes';
      }

    };

    var API_DOCS_TEMPLATE = document.getElementById('api-docs-template').innerText;

    var DATA_STRUCTURE_TEMPLATE = document.getElementById('data-structure-template').innerText;

    var DATA_TYPES = {
      string: '字符串',
      number: '数值',
      integer: '整数',
      boolean: '布尔',
      array: '数组',
      object: '对象'
    };

    var xhr = new XMLHttpRequest();

    // 取得 API 文档信息
    xhr.addEventListener('load', function() {

      var apiDocs = JSON.parse(xhr.response || xhr.responseText);

      /**
       * 设置数据类型表示值及有效值列表。
       */
      function setDataType(parameter) {

        parameter.dataType = DATA_TYPES[parameter.type] || parameter.type;

        var validValues = (parameter.enum || (parameter.items && parameter.items.enum) || []);

        validValues.forEach(function(item, index) {
          validValues[index] = '<code>' + item + '</code>';
        });

        parameter.validValues = validValues.join('、');
      }

      /**
       * 渲染数据结构信息表格。
       */
      function renderDataStructure(data, parametersKey, depth) {

        depth = depth || 0;

        if (depth > 2) {
          return '';
        }

        var parameters, schemaName, schema;

        if (data === null
            || (parametersKey && (parameters = data[parametersKey]) === null)) {
          return '';
        }

        if (!(parameters instanceof Array) || !parametersKey) {

          schemaName = data.$ref
            || (data.schema || {}).$ref
            || (data.items || {}).$ref
            || ((parameters || {}).schema || {}).$ref
            || ((parameters || {}).items || {}).$ref;

          if (!schemaName || !(schema = apiDocs.definitions[schemaName.split('/').pop()])) {
            return '';
          }

          parameters = [];

          var parameter;

          for (var property in schema.properties) {

            if (!schema.properties.hasOwnProperty(property)) {
              continue;
            }

            parameter = schema.properties[property];
            parameter.name = property;

            parameters.push(parameter);
          }

        }

        return ejs.render(DATA_STRUCTURE_TEMPLATE, {
          context: apiDocs,
          schemaName: (!parametersKey && schemaName)
            ? schemaName.replace(/«/g, '<').replace(/»/g, '>').replace(/^.+\/([^\/]+)$/, '$1')
            : null,
          renderDataStructure: renderDataStructure,
          setDataType: setDataType,
          parameters: parameters,
          depth: depth
        });

      }

      apiDocs.tags = apiDocs.tags || [];
      apiDocs.info = apiDocs.info || {};
      apiDocs.paths = apiDocs.paths || {};
      apiDocs.routeCount = 0;
      apiDocs.controllers = [];
      apiDocs.renderDataStructure = renderDataStructure;

      var controllerMap = {};
      var controllerNames = [];

      // 设置控制器列表
      apiDocs.tags.forEach(function(controller) {
        controller.routeCount = 0;
        controller.routes = [];
        controller.routePaths = [];
        controllerMap[controller.name] = controller;
        controllerNames.push(controller.name);
        apiDocs.controllers.push(controller);
      });

      var route;

      // 设置控制器的路由列表
      for (var path in apiDocs.paths) {

        if (!apiDocs.paths.hasOwnProperty(path)) {
          continue;
        }

        for (var method in apiDocs.paths[path]) {

          if (!apiDocs.paths[path].hasOwnProperty(method)) {
            continue;
          }

          route = apiDocs.paths[path][method];
          route.method = method;
          route.path = path;
          route.pathHTML = path.replace(/({.+?})/g, '<code>$1</code>');
          route.routeId = '/' + route.tags[0] + '/' + route.operationId;
          route.pathVariables = [];
          route.searchParameters = [];
          route.requestBody = null;
          route.responseBody = route.responses['200'] || route.responses['201'];

          route.tags.forEach(function(tagName) {

            var controller = controllerMap[tagName];

            if (!controller) {
              return;
            }

            controller.routeCount++;
            controller.routes.push(route);
            controller.routePaths.push(route.path);
          });

          apiDocs.routeCount++;

          (route.parameters || []).forEach(function(parameter) {
            switch (parameter.in) {
              case 'path':
                route.pathVariables.push(parameter);
                break;
              case 'query':
                route.searchParameters.push(parameter);
                break;
              case 'body':
                route.requestBody = parameter;
                break;
            }
          });

        }

      }

      controllerNames.sort();

      apiDocs.controllers.sort(function(a, b) {
        return controllerNames.indexOf(a) - controllerNames.indexOf(b);
      });

      var METHODS = ['post', 'get', 'patch', 'put', 'delete'];

      apiDocs.controllers.forEach(function(controller) {

        controller.routePaths.sort();

        controller.routes.sort(function(a, b) {

          if (controller.routePaths.indexOf(a.path) === controller.routePaths.indexOf(b.path)) {
            return METHODS.indexOf(a.method) - METHODS.indexOf(b.method);
          }

          return controller.routePaths.indexOf(a.path) - controller.routePaths.indexOf(b.path);
        });

      });

      /**
       * 将接口信息输出到控制台。
       */
      var printApiList = window.printApiList = function() {

        var text = '%c' + apiDocs.info.title + '\t\t\t\t%c ' + apiDocs.routeCount + ' \r\n';

        var args = [
          'font-size: 18px; font-weight: bold; color: #000000;',
          'font-size: 18px; color: #FFFFFF; background: #0000FF;'
        ];

        apiDocs.controllers.forEach(function(controller) {

          text += '%c\t' + controller.description + '\t\t\t%c ' + controller.routeCount + ' \r\n';

          args = args.concat([
            'font-size: 14px; font-weight: bold; color: #000000;',
            'font-size: 14px; color: #FFFFFF; background: #0000FF;'
          ]);

          controller.routes.forEach(function(route) {

            text += '%c\t\t' + route.summary + '\t' + route.method.toUpperCase() + ' ' + route.path + ' \r\n';

            args = args.concat([
              'font-size: 12px; color: #7F7F7F7;'
            ]);

          });

        });

        args.unshift(text);

        console.log.apply(null, args);
      };

      //printApiList();

      document.body.innerHTML = ejs.render(API_DOCS_TEMPLATE, apiDocs);

      var controllersPanel = document.getElementById('controllers');
      var controllerPanels = document.querySelectorAll('div.controller');
      var locating = false;

      /**
       * 切换控制器/路由。
       */
      window.onhashchange = function() {

        if (locating) {
          return;
        }

        var selected = location.hash.match(/[_\-0-9a-zA-Z]+/g) || [];
        var selectedController = selected[0] || (apiDocs.tags[0] || {}).name;
        var selectedRoute = selected[1];
        var controllerPanel;

        // 显示指定的控制器面板
        for (var controllerPanelIndex = 0; controllerPanelIndex < controllerPanels.length; controllerPanelIndex++) {

          controllerPanel = controllerPanels[controllerPanelIndex];

          if (controllerPanel.id === selectedController) {
            controllerPanel.style.display = 'block';
          } else {
            controllerPanel.style.display = 'none';
          }

        }

        // 初始展开菜单栏项目
        if (!initialized) {

          var currentMenuItems = document.querySelectorAll('li.show-routes');

          for (var mi = 0; mi < currentMenuItems.length; mi++) {
            currentMenuItems[mi].className = 'hide-routes';
          }

          var targetMenuItem = document.querySelector('#menu-item-' + selectedController);

          if (targetMenuItem) {
            targetMenuItem.className = 'show-routes';
            setTimeout(function() {
              document.querySelector('nav').scrollTop = targetMenuItem.offsetTop;
            }, 10);
          }

          initialized = true;
        }

        // 未指定路由时回到页面顶部
        if (!selectedRoute) {

          controllersPanel.scrollTop = 0;

        // 否则滚动到指定路由说明
        } else {

          var hash = location.hash;

          locating = true;

          location.href = '#/';

          setTimeout(function() {
            location.href = hash;
            setTimeout(function() { locating = false; }, 10);
          }, 10);

        }

      };

      window.onhashchange();
    });

    xhr.open('GET', './swagger.json');

    xhr.send();

  })();

})();
