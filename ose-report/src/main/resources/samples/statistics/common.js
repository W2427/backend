'use strict';

// 工具
(() = > {

  let is = window.is = {
    type: (value, type) = > (Object.prototype.toString.call(value) === `[object ${type}]`),
  undefined: value = > is.type(value, 'Undefined'),
  null
:
value =
>
is.type(value, 'Null'),
  number
:
value =
>
is.type(value, 'Number'),
  string
:
value =
>
is.type(value, 'String'),
  boolean
:
value =
>
is.type(value, 'Boolean'),
  function
:
value =
>
is.type(value, 'function'),
  object
:
value =
>
is.type(value, 'Object'),
  array
:
value =
>
is.type(value, 'Array'),
  unset
:
value =
>
(is.undefined(value) || is.null(value)),
  primitive
:
value =
>
(is.number(value) || is.string(value) || is.boolean(value)),
  empty
:
value =
>
(is.unset(value) || ((is.string(value) || is.array(value)) && value.length === 0))
}
;

})
();

// GUI
(() = > {

  let gui = window.gui = {};

let pendingCount = 0;
let maskPanel = null;

/**
 * 显示遮罩。
 */
gui.showMask = () =
>
{

  pendingCount++;

  if (!maskPanel) {

    let circlePanel = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    circlePanel.setAttribute('cx', '24');
    circlePanel.setAttribute('cy', '24');
    circlePanel.setAttribute('r', '16');
    circlePanel.setAttribute('fill', 'none');
    circlePanel.setAttribute('stroke', '#BFBFBF');
    circlePanel.setAttribute('stroke-width', '16');

    let pathPanel = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    pathPanel.setAttribute('d', 'M 24,24 m -16,0 a 16,16 0 1,1 16,16');
    pathPanel.setAttribute('fill', 'none');
    pathPanel.setAttribute('stroke', '#DFDFDF');
    pathPanel.setAttribute('stroke-width', '10');

    let svgPanel = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svgPanel.setAttribute('width', '48');
    svgPanel.setAttribute('height', '48');
    svgPanel.setAttribute('viewBox', '0 0 48 48');
    svgPanel.setAttribute('style', 'display: block; margin: auto;');
    svgPanel.appendChild(circlePanel);
    svgPanel.appendChild(pathPanel);

    maskPanel = document.createElement('div');
    maskPanel.setAttribute('id', '__mask');
    maskPanel.appendChild(svgPanel);

    document.body.appendChild(maskPanel);
  }

  maskPanel.className = 'pending';
}
;

/**
 * 关闭遮罩。
 */
gui.closeMask = () =
>
{

  pendingCount = Math.max(--pendingCount, 0);

  setTimeout(() = > {
    if(pendingCount === 0
)
  {
    maskPanel.className = '';
  }
},
  500
)
  ;

}
;

let messagePanel = null;
let messageContentPanel = null;
let reading = false;
let timeout = false;
let timeoutId = 0;

/**
 * 显示消息。
 * @param {string}  message    消息内容
 * @param {boolean} [richText] 是否为富文本
 */
gui.showMessage = (message, richText = false) =
>
{

  if (!messagePanel) {

    messageContentPanel = document.createElement('div');

    messagePanel = document.createElement('div');
    messagePanel.setAttribute('id', '__message');
    messagePanel.appendChild(messageContentPanel);

    messagePanel.addEventListener('mouseover', () = > {
      reading = true;
    messagePanel.className = 'showing';
  })
    ;

    messagePanel.addEventListener('mouseout', event = > {
      if(event.relatedTarget === event.target
    || event.relatedTarget === (event.target || {}).parentElement
    || (event.relatedTarget || {}).parentElement === event.target
    || (event.relatedTarget || {}).parentElement === (event.target || {}).parentElement
  )
    {
      return;
    }
    reading = false;
    timeout && (messagePanel.className = 'showing closing');
  })
    ;

    document.body.appendChild(messagePanel);
  }

  messageContentPanel[richText ? 'innerHTML' : 'innerText'] = message;
  messagePanel.className = 'showing';

  reading = false;
  timeout = false;

  clearTimeout(timeoutId);

  timeoutId = setTimeout(() = > {
    timeout = true;
  !reading && (messagePanel.className = 'showing closing');
},
  5000
)
  ;

}
;

})
();

// HTTP 客户端
(() = > {

  const ACCESS_TOKEN_KEY = 'access-token';

let http = window.http = {};

let defaultCallback = () =
>
{
}
;

/**
 * 将对象转为查询字符串的键值对。
 * @param {*}      value
 * @param {string} [key]
 * @returns {[string]} key-value pairs
 */
let toKVPs = (value, key) =
>
{

  let kvps = [];

  if (is.empty(value)) {
    return kvps;
  }

  if (is.primitive(value)) {
    kvps.push(`${key}=${encodeURIComponent(value)}`);
  } else if (is.array(value)) {
    value.forEach(item = > {
      kvps = kvps.concat(toKVPs(item, key));
  })
    ;
  } else if (is.object(value)) {
    key = key ? `${key}.` : '';
    Object.keys(value).forEach(childKey = > {
      kvps = kvps.concat(toKVPs(value[childKey], `${key}${encodeURIComponent(childKey)}`));
  })
    ;
  }

  return kvps;
}
;

/**
 * 将对象转为查询字符串。
 * @param {object|[object]} query
 * @returns {string} query string
 */
http.querystring = query =
>
{

  if (is.object(query)) {

    let kvps = toKVPs(query);

    return kvps.length === 0 ? '' : `?${kvps.join('&')}`;

  } else if (is.array(query)) {

    let kvps = [];

    query.forEach(o = > {
      let querystring = http.querystring(o);
    querystring && kvps.push(querystring.replace(/^\?/, ''));
  })
    ;

    return kvps.length === 0 ? '' : `?${kvps.join('&')}`;
  }

  return '';
}
;

/**
 * 发送 HTTP 请求。
 * @param {string}   [host]
 * @param {number}   [port]
 * @param {string}   method
 * @param {string}   url
 * @param {object}   [params]
 * @param {object}   [query]
 * @param {object}   [body]
 * @param {function} [callback]
 */
let send = http.send = ({host, port, method, url, params, query, body}, callback) =
>
{

  gui.showMask();

  host = host || location.hostname;
  port = port || location.port;
  url = `//${host}:${port}${url}`;

  // set path variables
  if (typeof(params) === 'object') {
    url = url.replace(/{(.+?)}/g, (placeholder, pathVariableName) = > {
      return params[pathVariableName];
  })
    ;
  }

  // set query parameters
  url += http.querystring(query);

  let xhr = new XMLHttpRequest();
  let accessToken = localStorage.getItem(ACCESS_TOKEN_KEY);

  xhr.addEventListener('load', () = > {

    let result = JSON.parse(xhr.responseText);

  if (xhr.status >= 400) {

    if (xhr.status === 401) {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
    }

    gui.showMessage(result.error && result.error.message);
    alert('ERROR: ' + (new Date()).toISOString());
  }

  if (result.accessToken) {
    localStorage.setItem(ACCESS_TOKEN_KEY, result.accessToken);
  }

  gui.closeMask();

  (callback || defaultCallback)(result);
})
  ;

  xhr.open(method.toUpperCase(), url);

  if (accessToken) {
    xhr.setRequestHeader('Authorization', `Bearer ${accessToken}`);
  }

  if (body) {
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(body));
  } else {
    xhr.send();
  }

}
;

/**
 * 发送 GET 请求。
 * @param {string}   [host]
 * @param {number}   [port]
 * @param {string}   url
 * @param {object}   [params]
 * @param {object}   [query]
 * @param {function} [callback]
 */
http.get = ({host, port, url, params, query}, callback) =
>
{
  send({host, port, method: 'GET', url, params, query}, callback);
}
;

/**
 * 发送 POST 请求。
 * @param {string}   [host]
 * @param {number}   [port]
 * @param {string}   url
 * @param {object}   [params]
 * @param {object}   [query]
 * @param {object}   [body]
 * @param {function} [callback]
 */
http.post = ({host, port, url, params, query, body}, callback) =
>
{
  send({host, port, method: 'POST', url, params, query, body}, callback);
}
;

/**
 * 通过 FORM 元素发送 HTTP 请求。
 * @param {string|object} formElement
 * @param {string}        [host]
 * @param {number}        [port]
 * @param {object}        [params]
 * @param {object}        [query]
 * @param {function}      [callback]
 */
http.sendForm = (formElement, {host, port, params, query}, callback) =
>
{

  if (typeof(formElement) === 'string') {
    formElement = document.querySelector(formElement);
  }

  formElement.addEventListener('submit', event = > {

    event.preventDefault();

  let body = {};
  let formData = new FormData(formElement);
  let keys = formData.keys();
  let nextKey;
  let key;

  while (true) {

    nextKey = keys.next();

    if (nextKey.done) {
      break;
    }

    key = nextKey.value;

    body[key] = formData.get(key);
  }

  http.send(
    {
      host,
      port,
      method: formElement.method,
      url: formElement.getAttribute('action'),
      body
    },
    callback
  );

  return false;
})
  ;

}
;

})
();
