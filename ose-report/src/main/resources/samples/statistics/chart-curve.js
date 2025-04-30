'use strict';

document.querySelector('#root').addEventListener('load', function () {

  let rootPanel = this;

  if (!rootPanel.width.baseVal.value) {
    setTimeout(() = > {
      rootPanel.dispatchEvent(new Event('load'));
  },
    10
  )
    ;
    return;
  }

  /**
   * @type {{
   *   title:      String,
   *   percentage: Boolean,
   *   groups: [
   *     {
   *       title: String,
   *       unit:  String
   *     }
   *   ],
   *   sum: [Number],
   *   accumulation: [Number],
   *   periods: [
   *     {
   *       time: [Number],
   *       values: [Number]
   *     }
   *   ]
   * }|null}
   */
  let data = null;

  // 从查询参数中取得数据数据
  location.search.split('&').forEach(kvp = > {
    let dataKVP = kvp.split('=');
  (dataKVP[0] === 'data') && (data = JSON.parse(decodeURIComponent(dataKVP[1] || '')));
})
  ;

  if (!data) {
    return;
  }

  // 设置标题
  document.querySelector('#title').textContent = data.title;

  /**
   * 取得指定期间的下一个期间。
   * @param {number} year
   * @param {number} [month]
   * @param {number} [day]
   * @param {number} [week]
   * @returns {[number]}
   */
  let nextPeriod = (year, month, day, week) =
>
  {

    // 日次数据时增加一天
    if (day) {

      let nextDate = new Date(year, month - 1, day + 1);

      year = nextDate.getFullYear();
      month = nextDate.getMonth() + 1;
      day = nextDate.getDate();

      // 周次数据时增加一周
    } else if (week) {

      week++;

      let dayOfWeek = (new Date(year, 0, 1)).getDay();
      let sunday = new Date(year, 0, (dayOfWeek === 0 ? 0 : (7 - dayOfWeek + 1)) + (week - 1) * 7);

      if (sunday.getFullYear() > year) {
        year++;
        week = 1;
      }

      // 月次数据时增加一月
    } else if (month) {

      month++;

      if (month > 12) {
        year++;
        month = 1;
      }

    }

    return [year, month, day, week];
  }
  ;

  // 补全期间
  data.periods = [[]].concat(data.periods).reduce((periods, currentPeriod) = > {

    if(periods.length === 0
)
  {
    periods.push(currentPeriod);
    return periods;
  }

  let previousPeriod = periods[periods.length - 1];
  let nextPeriodTime = previousPeriod.time;

  while (true) {

    nextPeriodTime = nextPeriod(...nextPeriodTime
  )
    ;

    if (nextPeriodTime.join() >= currentPeriod.time.join()) {
      break;
    }

    periods.push({
      time: nextPeriodTime,
      values: []
    });
  }

  periods.push(currentPeriod);

  return periods;
})
  ;

  let sum = Math.max.apply(null, data.sum);
  let min = 0;
  let max = 0;

  // 百分比数据时计算累计值的百分比，然后取最大/最小值
  if (data.percentage) {

    data.groups.forEach((group, index) = > {

      let accumulation = data['accumulation'][index] || 0;

    data.periods.forEach(period = > {
      if(typeof period.values[index] === 'number'
  )
    {
      accumulation += period.values[index];
      period.values[index] = (sum === 0 ? 0 : parseFloat((accumulation * 100 / sum)).toFixed(2));
    }
    min = Math.min(min, period.values[index] || 0);
    max = Math.max(max, period.values[index] || 0);
  })
    ;

  })
    ;

    sum = 100;

    // 否则直接取得最大/最小值
  } else {

    data.groups.forEach((group, index) = > {
      data.periods.forEach(period = > {
      min = Math.min(min, period.values[index] || 0);
    max = Math.max(max, period.values[index] || 0);
  })
    ;
  })
    ;

  }

  let STEP_COUNT = 10;
  let step = (max - min) / STEP_COUNT;
  let stepOOM = Math.pow(10, Math.floor(Math.log10(step)));
  let graduation;
  let graduationPanel;

  step = stepOOM === 0 ? (data.groups[0].unit === '%' ? 10 : 0.1) : (Math.ceil(step / stepOOM) * stepOOM);
  min = graduation = Math.floor(min / step) * step;
  max = Math.ceil(max / step) * step;

  let fixed = ((('' + parseFloat(step.toFixed(6))).match(/(\.(\d*))$/) || [])[2] || '').length;

  // 设置水平线标记值
  for (let stepIndex = 0; stepIndex <= STEP_COUNT; stepIndex++) {
    graduationPanel = document.querySelector(`#value-${('0' + stepIndex).slice(-2)}`);
    graduationPanel.textContent = `${graduation.toFixed(fixed)} ${data.groups[0].unit}`;
    graduation += step;
  }

  let VIEW_BOX_WIDTH = rootPanel.viewBox.baseVal.width;
  let VIEW_BOX_HEIGHT = rootPanel.viewBox.baseVal.height;
  let SVG_NAMESPACE = 'http://www.w3.org/2000/svg';
  let FILL_COLORS = ['#FF9F3F', '#3F9FFF', '#009F00', '#9F3FFF'];
  let STROKE_COLORS = ['#FF7F00', '#003FFF', '#005F00', '#7F00FF'];
  let STROKE_OPACITY = ['1', '0.5', '0.5', '0.5'];
  let CHART_MARGIN_TOP = 20;
  let CHART_MARGIN_LEFT = 5;
  let figuresPanel = document.querySelector('#figures');
  let pathsPanel = document.querySelector('#paths');
  let periodsPanel = document.querySelector('#periods');
  let chartHeight = parseInt(pathsPanel.getAttribute('height')) - CHART_MARGIN_TOP - 5;
  let chartViewWidth = parseInt(pathsPanel.getAttribute('width'));
  let chartWidth = chartViewWidth - CHART_MARGIN_LEFT * 2;
  let periodWidth = Math.max((chartWidth * 100 / data.periods.length) / 100, 4);

  let periodValuesPanel = document.querySelector('#period-values');
  let datetimePanel = document.querySelector('#datetime');
  let periodValuePanels = [
    document.querySelector('#period-value-01'),
    document.querySelector('#period-value-02'),
    document.querySelector('#period-value-03'),
    document.querySelector('#period-value-04')
  ];

  periodValuesPanel.setAttribute('height', `${14 * (data.groups.length + 1) + 7}`);

  periodValuePanels.slice(data.groups.length).forEach(panel = > {
    periodValuesPanel.removeChild(panel);
})
  ;

  // 更新期间值面板的位置
  rootPanel.addEventListener('mousemove', event = > {
    let rootWidth = rootPanel.width.baseVal.value;
  let rootHeight = rootPanel.height.baseVal.value;
  let scale = Math.min(rootWidth / VIEW_BOX_WIDTH, rootHeight / VIEW_BOX_HEIGHT);
  let x = (event.clientX - ((rootWidth - VIEW_BOX_WIDTH * scale) / 2)) / scale;
  let y = event.clientY / scale;
  periodValuesPanel.setAttribute('x', `${x + 7}`);
  periodValuesPanel.setAttribute('y', `${y + 7}`);
})
  ;

  /**
   * 格式化时间。
   * @param {number} year
   * @param {number} month
   * @param {number} day
   * @param {number} week
   * @returns {string}
   */
  let formatTime = ([year, month, day, week]) =
>
  {
    return day
      ? `${year}-${('0' + month).slice(-2)}-${('0' + day).slice(-2)}`
      : (week
          ? `${year}/${('0' + week).slice(-2)}`
          : `${year}-${('0' + month).slice(-2)}`
      );
  }
  ;

  // 绘制曲线
  data.groups.forEach((group, groupIndex) = > {

    let figureTextPanel = document.createElementNS(SVG_NAMESPACE, 'text');
  figureTextPanel.setAttribute('x', `${groupIndex * 84}`);
  figureTextPanel.setAttribute('y', `${Math.floor(groupIndex / 4) * 14 + 10}`);
  figureTextPanel.setAttribute('fill', STROKE_COLORS[groupIndex]);
  figureTextPanel.textContent = `■ ${group.title}`;
  figuresPanel.appendChild(figureTextPanel);

  periodValuePanels[groupIndex].setAttribute('fill', FILL_COLORS[groupIndex]);

  let pathPanel = document.createElementNS(SVG_NAMESPACE, 'path');
  let drawCommand = [];
  let startAtX = Math.max(periodWidth / 2, 2);
  let pointX = startAtX + CHART_MARGIN_LEFT;
  let previousPointX;
  let pointY;
  let previousPointY;
  let centerX;

  pathsPanel.appendChild(pathPanel);

  data.periods.forEach(period = > {

    let value = period.values[groupIndex];
  let rectPanel = null;

  if (typeof value === 'number') {

    pointY = (max - min) === 0
      ? (CHART_MARGIN_TOP + chartHeight)
      : (CHART_MARGIN_TOP + (chartHeight - ((value - min) * chartHeight / (max - min))));

    if (drawCommand.length === 0) {
      drawCommand.push(`M ${pointX},${pointY}`);
    } else {
      centerX = ((pointX + previousPointX) / 2).toFixed(2);
      drawCommand.push(`C ${centerX},${previousPointY.toFixed(2)} ${centerX},${pointY.toFixed(2)} ${pointX.toFixed(2)},${pointY.toFixed(2)}`);
    }

    // 水平标线
    let linePanel = document.createElementNS(SVG_NAMESPACE, 'line');
    linePanel.setAttribute('x1', '0');
    linePanel.setAttribute('y1', pointY.toFixed(2));
    linePanel.setAttribute('x2', chartViewWidth.toFixed(2));
    linePanel.setAttribute('y2', pointY.toFixed(2));
    linePanel.setAttribute('stroke', FILL_COLORS[groupIndex]);
    linePanel.setAttribute('stroke-opacity', '0.5');
    linePanel.setAttribute('stroke-width', '1');
    linePanel.setAttribute('stroke-dasharray', '2');
    linePanel.setAttribute('stroke-dashoffset', ['0', '2'][groupIndex % 2]);

    // 标记点
    let circlePanel = document.createElementNS(SVG_NAMESPACE, 'circle');
    circlePanel.setAttribute('cx', pointX.toFixed(2));
    circlePanel.setAttribute('cy', pointY.toFixed(2));
    circlePanel.setAttribute('r', '2');
    circlePanel.setAttribute('stroke', STROKE_COLORS[groupIndex]);
    circlePanel.setAttribute('stroke-width', '1');
    //circlePanel.setAttribute('stroke-dasharray', '0.25');
    //circlePanel.setAttribute('stroke-dashoffset', ['0', '0.25'][groupIndex % 2]);
    circlePanel.setAttribute('stroke-opacity', STROKE_OPACITY[groupIndex % 4]);
    circlePanel.setAttribute('fill', FILL_COLORS[groupIndex]);
    circlePanel.setAttribute('fill-opacity', '0.33');

    // 时间范围方框
    if (!(rectPanel = document.querySelector(`#period-${period.time.join('-')}`))) {
      rectPanel = document.createElementNS(SVG_NAMESPACE, 'rect');
      rectPanel.setAttribute('id', `period-${period.time.join('-')}`);
      rectPanel.setAttribute('x', (pointX - startAtX).toFixed(2));
      rectPanel.setAttribute('y', `${CHART_MARGIN_TOP}`);
      rectPanel.setAttribute('width', `${periodWidth.toFixed(2)}`);
      rectPanel.setAttribute('height', `${chartHeight.toFixed(2)}`);
    }

    let datetime = formatTime(period.time);

    rectPanel.addEventListener('mouseover', () = > {
      circlePanel.setAttribute('r', '4');
    circlePanel.setAttribute('stroke-width', '2');
    linePanel.setAttribute('class', 'show');
    datetimePanel.textContent = datetime;
    let periodValuePanel = periodValuePanels[groupIndex];
    periodValuePanel.textContent = `${value} ${data.groups[groupIndex].unit}`;
    periodValuesPanel.setAttribute('class', 'show');
  })
    ;

    rectPanel.addEventListener('mouseout', () = > {
      circlePanel.setAttribute('r', '2');
    circlePanel.setAttribute('stroke-width', '1');
    linePanel.removeAttribute('class');
    periodValuesPanel.removeAttribute('class');
  })
    ;

    // 时间标记
    let periodPanel = document.createElementNS(SVG_NAMESPACE, 'text');
    periodPanel.setAttribute('x', '0');
    periodPanel.setAttribute('y', `${-1 * (pointX + 6)}`);
    periodPanel.setAttribute('transform', 'rotate(90)');
    periodPanel.textContent = datetime;

    pathsPanel.appendChild(linePanel);
    pathsPanel.appendChild(circlePanel);
    pathsPanel.appendChild(rectPanel);
    periodsPanel.appendChild(periodPanel);

    previousPointX = pointX;
    previousPointY = pointY;
  }

  pointX += periodWidth;
})
  ;

  pathPanel.setAttribute('x', '0');
  pathPanel.setAttribute('y', '0');
  pathPanel.setAttribute('d', drawCommand.join(' '));
  pathPanel.setAttribute('fill', 'none');
  pathPanel.setAttribute('stroke', STROKE_COLORS[groupIndex]);
  pathPanel.setAttribute('stroke-width', '1.5');
  //pathPanel.setAttribute('stroke-dasharray', '0.25');
  //pathPanel.setAttribute('stroke-dashoffset', ['0', '0.25'][groupIndex % 2]);
  pathPanel.setAttribute('stroke-opacity', STROKE_OPACITY[groupIndex % 4]);
})
  ;

});
