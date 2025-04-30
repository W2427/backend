;(function () {
  'use strict';

  if (navigator.platform === 'Win32') {
    document.body.className = document.body.className
      ? (document.body.className + ' windows')
      : 'windows';
  }

  // 显示/关闭索引
  function toggleIndex() {

    var classNames = document.body.className
      ? document.body.className.split(/\s+/)
      : [];

    var classNameIndex = classNames.indexOf('show-index');

    if (classNameIndex < 0) {
      classNames.push('show-index');
    } else {
      classNames.splice(classNameIndex, 1);
    }

    document.body.className = classNames.join(' ');
  }

  // 关闭索引
  function closeIndex() {

    var classNames = document.body.className
      ? document.body.className.split(/\s+/)
      : [];

    var classNameIndex = classNames.indexOf('show-index');

    if (classNameIndex >= 0) {
      classNames.splice(classNameIndex, 1);
    } else {
      return;
    }

    document.body.className = classNames.join(' ');
  }

  document.querySelector('#show-index').addEventListener('click', toggleIndex);
  document.querySelector('#index').addEventListener('click', closeIndex);

  var renderer = new marked.Renderer();

  // 重新设置链接生成逻辑以阻止 marked 根据 Markdown 的文本内容自动生成链接
  renderer.link = function (href, title, text) {

    if (/^mailto:/i.test(href)) {
      return text;
    }

    if (href === text && !title) {
      return href;
    }

    return '<a href="' + href + '"' + (title ? (' title="' + title + '"') : '') + '>' + text + '</a>';
  };

  // Markdown 转 HTML
  document.querySelector('#content').innerHTML = marked(
    document.querySelector('#source').innerText,
    {renderer: renderer}
  );

  // 代码高亮渲染
  hljs.initHighlightingOnLoad();

  /**
   * 为 HTML 元素设置锚地。
   * @param {HTMLElement} element HTML 元素
   * @returns {string} 跳转链接
   */
  function setAnchor(element) {
    var text = element.innerText;
    var id = encodeURIComponent(text).replace(/%/g, '').toLowerCase();
    element.innerHTML = '<a id="' + id + '"></a>' + text;
    return '<a href="#' + id + '">' + text + '</a>';
  }

  var headerElements = document.querySelectorAll('h2, h3');
  var headerElement;
  var indexPanel = document.createElement('ul');
  var itemPanel = null;
  var subItemsPanel;
  var subItemPanel;

  // 生成索引
  for (var i = 0; i < headerElements.length; i++) {

    headerElement = headerElements[i];

    if (headerElement.tagName === 'H2') {
      subItemsPanel = document.createElement('ul');
      itemPanel = document.createElement('li');
      itemPanel.innerHTML = setAnchor(headerElement);
      itemPanel.appendChild(subItemsPanel);
      indexPanel.appendChild(itemPanel);
    } else {
      subItemPanel = document.createElement('li');
      subItemPanel.innerHTML = setAnchor(headerElement);
      subItemsPanel.appendChild(subItemPanel);
    }

  }

  document.querySelector('#index').appendChild(indexPanel);

  // 重新定位到页面的指定位置
  if (location.hash) {
    location.href = location.hash;
  }

})();
