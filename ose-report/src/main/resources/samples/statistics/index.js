'use strict';

(() = > {

  // 检查用户是否已登录
  if(
!localStorage.getItem('access-token')
)
{
  location.href = './sign-in.html?target=' + encodeURIComponent(location.href);
  return;
}

// 当前日期
let currentDate = ((now) = > `${now.getFullYear()}-${('0' + (now.getMonth() + 1)).slice(-2)}-${('0' + now.getDate()).slice(-2)}`
)
(new Date());

// 注销按钮
document.querySelector('#sign-out').addEventListener('click', () = > {
  localStorage.removeItem('access-token');
localStorage.removeItem('user-id');
location.reload();
})
;

// 定义对象的属性，并将字段值存储于本地存储中
let defineProperty = (object, name) =
>
{
  Object.defineProperty(object, name, {
    enumerable: true,
    get: () = > localStorage.getItem(name),
    set
:
  value =
>
  {
    if (value === null) {
      localStorage.removeItem(name);
    } else {
      localStorage.setItem(name, value);
    }

  }
})
  ;
}
;

// 归档查询参数
let context = {};
defineProperty(context, 'userId');
defineProperty(context, 'orgId');
defineProperty(context, 'projectId');
defineProperty(context, 'archiveType');
defineProperty(context, 'scheduleType');
defineProperty(context, 'archiveTime');

// 对象数据起止时间
let dateRange = {};
defineProperty(dateRange, 'dateFrom');
defineProperty(dateRange, 'dateUntil');

// 归档数据聚合参数
let groupKeys = {};
defineProperty(groupKeys, 'module');
defineProperty(groupKeys, 'pressureTestPackage');
defineProperty(groupKeys, 'subSystem');
defineProperty(groupKeys, 'stage');
defineProperty(groupKeys, 'process');
defineProperty(groupKeys, 'weldType');
defineProperty(groupKeys, 'issueType');
defineProperty(groupKeys, 'entityNps');
defineProperty(groupKeys, 'entityLength');
defineProperty(groupKeys, 'entityMaterial');
defineProperty(groupKeys, 'subcontractorId');
defineProperty(groupKeys, 'departmentId');
defineProperty(groupKeys, 'welderId');
defineProperty(groupKeys, 'managerId');

// 数据过滤条件页面元素
let projectsPanel = document.querySelector('#projects');
let archiveTypesPanel = document.querySelector('#archive-type');
let scheduleTypesPanel = document.querySelector('#schedule-type');
let archiveTimesPanel = document.querySelector('#archive-time');

// 归档按钮
let archiveDataButton = document.querySelector('#archive-data');
let showLogsButton = document.querySelector('#show-logs');

// 对象数据起止时间
let dateFromPanel = document.querySelector('#date-from');
let dateUntilPanel = document.querySelector('#date-until');

// 数据聚合单位页面元素
let areasPanel = document.querySelector('#areas');
let modulesPanel = document.querySelector('#modules');
let pressureTestPackagesPanel = document.querySelector('#pressure-test-packages');
let subSystemsPanel = document.querySelector('#sub-systems');
let stagesPanel = document.querySelector('#stages');
let processesPanel = document.querySelector('#processes');
let weldTypesPanel = document.querySelector('#weld-types');
let issueLevelsPanel = document.querySelector('#issue-levels');
let issueTypesPanel = document.querySelector('#issue-types');
let entityNpsesPanel = document.querySelector('#entity-npses');
let entityLengthsPanel = document.querySelector('#entity-lengths');
let entityMaterialsPanel = document.querySelector('#entity-materials');
let subcontractorsPanel = document.querySelector('#subcontractors');
let departmentsPanel = document.querySelector('#departments');
let weldersPanel = document.querySelector('#welders');
let managersPanel = document.querySelector('#managers');

// 归档记录列表
let logsPanel = document.querySelector('#logs');
let logEntriesPanel = document.querySelector('#log-entries');

// 图表容器
let chartsPanel = document.querySelector('#charts');

// 定义归档数据类型配置
let ARCHIVE_TYPE_CONFIGS = {
  // 1./2. WBS 完成率/工时
  WBS_PROGRESS: {
    groupKeys: ['module', 'stage', 'process'],
    charts: [
      {
        title: 'WBS 进度',
        groups: [
          {title: '计划进度', unit: '%', field: 'totalScore'},
          {title: '完成进度', unit: '%', field: 'finishedScore'}
        ],
        percentage: true
      },
      {
        title: 'WBS 工时',
        groups: [
          {title: '计划工时', unit: 'Hr.', field: 'estimatedManHours'},
          {title: '实际工时', unit: 'Hr.', field: 'actualManHours'}
        ]
      }
    ]
  },
  // 10. 任务统计
  WBS_ACTIVITIES_PROGRESS: {
    groupKeys: ['module', 'stage', 'process'],
    charts: [
      {
        title: '合格',
        groups: [
          {title: '已完成', unit: '', field: 'finished_activities'},
          {title: '总数', unit: '', field: 'total_activities'}
        ]
      }
    ]
  },
  // 11. 焊接完成量
  WBS_WELD_PROGRESS: {
    groupKeys: ['module', 'weldType', 'entityMaterial', 'subcontractorId'],
    charts: []
  },
  // 12. 下料完成量
  WBS_FIT_UP_PROGRESS: {
    groupKeys: ['module'],
    charts: []
  },
  // 13. 单管施工完成量
  WBS_SPOOL_PROGRESS: {
    groupKeys: ['module', 'stage', 'process'],
    charts: []
  },
  // 14. 管线施工完成量
  WBS_ISO_PROGRESS: {
    groupKeys: [],
    charts: []
  },
  // 15. 试压施工完成量
  WBS_PRESSURE_TEST_PROGRESS: {
    groupKeys: ['module', 'pressureTestPackage'],
    charts: []
  },
  // 16. 清洁施工完成量
  WBS_CLEAN_PROGRESS: {
    groupKeys: ['module'],
    charts: []
  },
  // 20. 遗留问题处理量
  WBS_ISSUE_PROGRESS: {
    groupKeys: ['issueLevel', 'area', 'pressureTestPackage', 'subSystem', 'departmentId'],
    charts: [
      {
        title: '合格',
        groups: [
          {title: '> 4 周', unit: '', field: 'issueBefore4Weeks'},
          {title: '1~4 周', unit: '', field: 'issueBetween1WeekAnd4Weeks'},
          {title: '< 1 周', unit: '', field: 'issueInOneWeek'},
          {title: '已完成', unit: '', field: 'issueFinished'}
        ]
      }
    ]
  },
  // 21. 焊工合格率
  WBS_WELDER_RATE: {
    groupKeys: ['module', 'stage', 'entityMaterial', 'weldType'],
    charts: [
      {
        title: '合格',
        groups: [
          {title: '合格寸径', unit: 'in', field: 'nps_qualified'},
          {title: '合格个数', unit: '', field: 'qualified_count'}
        ]
      },
      {
        title: '不合格',
        groups: [
          {title: '不合格寸径', unit: 'in', field: 'nps_failed'},
          {title: '不合格个数', unit: '', field: 'failed_count'}
        ]
      }
    ]
  },
  // 22. 报验合格率
  WBS_PASS_RATE_PROGRESS: {
    groupKeys: ['module', 'stage', 'process'],
    charts: [
      {
        title: '内检',
        groups: [
          {title: '合格数', unit: 'in', field: 'qualified_internal_inspection'},
          {title: '总数', unit: '', field: 'internal_inspection'}
        ]
      },
      {
        title: '外检',
        groups: [
          {title: '合格数', unit: 'in', field: 'qualified_external_inspection'},
          {title: '总数', unit: '', field: 'external_inspection'}
        ]
      }
    ]
  }
};

// 聚合单位对应的下拉框
let GROUP_KEY_CONFIGS = {
  area: {
    dropdown: areasPanel,
    pathVariableName: 'areas'
  },
  module: {
    dropdown: modulesPanel,
    pathVariableName: 'modules'
  },
  pressureTestPackage: {
    dropdown: pressureTestPackagesPanel,
    pathVariableName: 'pressure-test-packages'
  },
  subSystem: {
    dropdown: subSystemsPanel,
    pathVariableName: 'sub-systems'
  },
  stage: {
    dropdown: stagesPanel,
    pathVariableName: 'stages'
  },
  process: {
    dropdown: processesPanel,
    pathVariableName: 'processes'
  },
  weldType: {
    dropdown: weldTypesPanel,
    pathVariableName: 'weld-types'
  },
  issueLevel: {
    dropdown: issueLevelsPanel,
    pathVariableName: 'issue-levels'
  },
  issueType: {
    dropdown: issueTypesPanel,
    pathVariableName: 'issue-types'
  },
  entityNps: {
    dropdown: entityNpsesPanel,
    pathVariableName: 'entity-npses'
  },
  entityLength: {
    dropdown: entityLengthsPanel,
    pathVariableName: 'entity-lengths'
  },
  entityMaterial: {
    dropdown: entityMaterialsPanel,
    pathVariableName: 'entity-materials'
  },
  subcontractorId: {
    dropdown: subcontractorsPanel,
    pathVariableName: 'subcontractors'
  },
  departmentId: {
    dropdown: departmentsPanel,
    pathVariableName: 'departments'
  },
  welderId: {
    dropdown: weldersPanel,
    pathVariableName: 'welders'
  },
  managerId: {
    dropdown: managersPanel,
    pathVariableName: 'managers'
  }
};

/*----------------------------------------------------------------------------
  取得项目组列表
 ---------------------------------------------------------------------------*/
http.get(
  {
    port: 8810,
    url: '/users/{userId}/top-project-orgs',
    params: context
  },
  result = > {

  result.data.forEach(org = > {
  let projectPanel = document.createElement('option');
projectPanel.innerText = org.name;
projectPanel.value = org.id;
(org.id === context.orgId) && (projectPanel.selected = true);
projectsPanel.appendChild(projectPanel);
})
;

projectsPanel.dispatchEvent(new Event('change'));
}
)
;

/*----------------------------------------------------------------------------
  切换项目组时取得项目信息
 ---------------------------------------------------------------------------*/
projectsPanel.addEventListener('change', () = > {

  context.orgId = projectsPanel.value;

http.get(
  {
    port: 8890,
    url: '/orgs/{orgId}/projects',
    params: context
  },
  result = > {

  let project = result.data[0];

if (!project) {
  context.projectId = null;
  return;
}

context.projectId = project.id;

archiveTypesPanel.dispatchEvent(new Event('change'));
}
)
;

})
;

/*----------------------------------------------------------------------------
  切换归档数据类型时触发归档计划类型切换事件
 ---------------------------------------------------------------------------*/
archiveTypesPanel.addEventListener('change', () = > {

  context.archiveType = archiveTypesPanel.value;

let archiveTypeGroupKeys = ARCHIVE_TYPE_CONFIGS[context.archiveType].groupKeys;

for (let groupKey in GROUP_KEY_CONFIGS) {
  if (archiveTypeGroupKeys.indexOf(groupKey) >= 0) {
    GROUP_KEY_CONFIGS[groupKey].dropdown.parentNode['style'].display = 'inline-block';
  } else {
    GROUP_KEY_CONFIGS[groupKey].dropdown.parentNode['style'].display = 'none';
  }
}

scheduleTypesPanel.dispatchEvent(new Event('change'));
})
;

context.archiveType && (archiveTypesPanel.value = context.archiveType);
context.scheduleType && (scheduleTypesPanel.value = context.scheduleType);

/**
 * 格式化日期。
 * @param {number} year           年
 * @param {number} [month]        月
 * @param {number} [day]          日
 * @param {number} [week]         周
 * @param {string} [scheduleType] 计划类型
 * @returns {string}
 */
let formatDate = ({year, month, day, week, scheduleType}) =
>
{

  if (!scheduleType) {
    if (day) {
      scheduleType = 'DAILY';
    } else if (week) {
      scheduleType = 'WEEKLY';
    } else if (month) {
      scheduleType = 'MONTHLY';
    }
  }

  switch (scheduleType) {
    case 'DAILY':
      return `${year}-${('0' + month).slice(-2)}-${('0' + day).slice(-2)}`;
    case 'WEEKLY':
      return `${year}/${('0' + week).slice(-2)}`;
    case 'MONTHLY':
      return `${year}-${('0' + month).slice(-2)}`;
  }

  return '';
}
;

/*----------------------------------------------------------------------------
  切换归档计划类型时取得归档时间列表
 ---------------------------------------------------------------------------*/
scheduleTypesPanel.addEventListener('change', () = > {

  if(context.scheduleType !== scheduleTypesPanel.value
)
{
  dateRange.dateFrom = null;
  dateRange.dateUntil = null;
}

context.scheduleType = scheduleTypesPanel.value;
archiveTimesPanel.innerHTML = '';

http.get(
  {
    url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/archive-dates',
    params: context
  },
  result = > {

  result.data = result.data || [];

result.data.forEach(time = > {

  time.value = formatDate(time);

let archiveTimePanel = document.createElement('option');
archiveTimePanel.innerText = `${time.value} (${('' + time.week).replace(/^(\d{4})(\d{2})$/, '$1年第$2周')})`;
archiveTimePanel.value = time.value;

(context.archiveTime === time.value) && (archiveTimePanel.selected = true);

archiveTimesPanel.appendChild(archiveTimePanel);
})
;

if (result.data.length === 0) {
  let archiveTimePanel = document.createElement('option');
  archiveTimePanel.innerText = '';
  archiveTimePanel.value = currentDate;
  archiveTimesPanel.appendChild(archiveTimePanel);
}

archiveTimesPanel.dispatchEvent(new Event('change'));
}
)
;

})
;

/**
 * 取得归档数据期间列表。
 */
let getPeriods = () =
>
{

  let charts = ARCHIVE_TYPE_CONFIGS[context.archiveType].charts;

  http.get(
    {
      url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveTime}',
      params: context,
      query: [groupKeys, dateRange, {scheduleType: context.scheduleType}]
    },
    result = > {

    result.data = result.data || {};
  result.data.sum = result.data.sum || {};
  result.data.accumulation = result.data.accumulation || {};
  result.data.periods = result.data.periods || [];

  chartsPanel.innerHTML = '';

  charts.forEach(chart = > {

    let charData = {
      title: chart.title,
      groups: chart.groups,
      percentage: chart.percentage,
      sum: [],
      accumulation: [],
      periods: []
    };

  chart.groups.forEach(group = > {
    charData.sum.push(result.data.sum[group.field] || 0);
  charData.accumulation.push(result.data.accumulation[group.field] || 0);
})
  ;

  result.data.periods.forEach(period = > {

    let periodData = {
      time: [
        period['groupYear'],
        period['groupMonth'],
        period['groupDay'],
        period['groupWeek']
      ],
      values: []
    };

  chart.groups.forEach(group = > {
    periodData.values.push(period[group.field] || 0);
})
  ;

  charData.periods.push(periodData);
})
  ;

  let chartPanel = document.createElement('iframe');
  chartPanel.src = `./chart-curve.svg?_${Date.now().toString(36).toUpperCase()}&data=${encodeURIComponent(JSON.stringify(charData))}`;
  chartsPanel.appendChild(chartPanel);
})
  ;

}
)
  ;

}
;

/*----------------------------------------------------------------------------
  更新聚合单位下拉框
 ---------------------------------------------------------------------------*/

let isGroupKeyDropDownInitialized = false;

let updateGroupDropDowns = () =
>
{

  let archiveTypeGroupKeys = ARCHIVE_TYPE_CONFIGS[context.archiveType].groupKeys;
  let archiveTypeGroupKeyPathVariableNames = [];

  archiveTypeGroupKeys.forEach(groupKey = > {
    archiveTypeGroupKeyPathVariableNames.push(GROUP_KEY_CONFIGS[groupKey].pathVariableName);
})
  ;

  isGroupKeyDropDownInitialized = false;

  if (archiveTypeGroupKeyPathVariableNames.length > 0) {

    http.get(
      {
        url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveTime}/'
        + archiveTypeGroupKeyPathVariableNames.join(','),
        params: context,
        query: [groupKeys, {scheduleType: context.scheduleType}]
      },
      result = > {

      result.data = result.data || {};
    result.data.periods = result.data.periods || [];

    if (result.data.periods.length > 0) {
      !dateRange.dateFrom && (dateRange.dateFrom = formatDate(result.data.periods[0]));
      !dateRange.dateUntil && (dateRange.dateUntil = formatDate(result.data.periods[result.data.periods.length - 1]));
    }

    // 设置起始时间列表
    [dateFromPanel, dateUntilPanel].forEach(dropdown = > {

      let selected = false;

    dropdown.innerHTML = '';

    result.data.periods.forEach(period = > {

      let option = document.createElement('option');
    option.value = formatDate(period);
    option.innerText = option.value
      .replace(/^(\d+)-(\d+)$/, '$1年$2月')
      .replace(/^(\d+)\/(\d+)$/, '$1年第$2周');

    if ((dropdown.getAttribute('id') === 'date-from' && dateRange['dateFrom'] === option.value)
      || (dropdown.getAttribute('id') === 'date-until' && dateRange['dateUntil'] === option.value)) {
      option.selected = true;
      selected = true;
    }

    if ((dropdown.getAttribute('id') === 'date-from' && dateRange.dateUntil && option.value > dateRange.dateUntil)
      || (dropdown.getAttribute('id') === 'date-until' && dateRange.dateFrom && option.value < dateRange.dateFrom)) {
      option.disabled = true;
    }

    dropdown.appendChild(option);
  })
    ;

    if (!selected && result.data.periods.length > 0) {
      if (dropdown.getAttribute('id') === 'date-from') {
        document.querySelector('#date-from > option:first-child').selected = true;
      } else if (dropdown.getAttribute('id') === 'date-until') {
        document.querySelector('#date-until > option:last-child').selected = true;
      }
    }

    dropdown.dispatchEvent(new Event('change'));
  })
    ;

    // 设置聚合单位列表
    ((result.data || {}).keys || []).forEach(groupKeyType = > {

      if(
    !GROUP_KEY_CONFIGS[groupKeyType.type]
  )
    {
      console.log(`groupKeyType.type: ${groupKeyType.type}`);
      return;
    }

    let dropdown = GROUP_KEY_CONFIGS[groupKeyType.type].dropdown;
    let option = document.createElement('option');

    option.innerText = '全部';
    option.value = '';
    dropdown.innerHTML = '';
    dropdown.appendChild(option);

    groupKeyType.values.forEach(groupKey = > {

      let option = document.createElement('option');
    option.innerText = groupKey;
    option.value = groupKey;
    (groupKeys[groupKeyType.type] === option.value) && (option.selected = true);

    dropdown.appendChild(option);
  })
    ;

    dropdown.dispatchEvent(new Event('change'));
  })
    ;

    isGroupKeyDropDownInitialized = true;

    getPeriods();
  }
  )
    ;

  } else {
    getPeriods();
  }

}
;

/*----------------------------------------------------------------------------
  切换归档时间
 ---------------------------------------------------------------------------*/
archiveTimesPanel.addEventListener('change', () = > {
  context.archiveTime = archiveTimesPanel.value;
updateGroupDropDowns();
})
;

/*----------------------------------------------------------------------------
  手动归档数据
 ---------------------------------------------------------------------------*/
archiveDataButton.addEventListener('click', () = > {
  http.post(
  {
    url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}',
    params: context
  },
  result = > {
  console.log('[归档数据] ' + JSON.stringify(result));
}
)
;
})
;

/*----------------------------------------------------------------------------
  查看归档记录
 ---------------------------------------------------------------------------*/
let formatTime = date =
>
{

  if (!date) {
    return '';
  }

  return `${date.getFullYear()}-${
    ('0' + (date.getMonth() + 1)).slice(-2)
    }-${
    ('0' + date.getDate()).slice(-2)
    } ${
    ('0' + date.getHours()).slice(-2)
    }:${
    ('0' + date.getMinutes()).slice(-2)
    }:${
    ('0' + date.getSeconds()).slice(-2)
    }`;
}
;

let searchLogs = callback =
>
{
  http.get(
    {
      port: 8890,
      url: '/projects/{projectId}/scheduled-task-logs',
      params: context,
      query: {code: `ARCHIVE_${context.archiveType}`}
    },
    result = > {

    logEntriesPanel.innerHTML = '';

  (result.data || []).forEach(logEntry = > {

    let logPanel = document.createElement('tr');

  let namePanel = document.createElement('td');
  namePanel.innerText = logEntry.name;
  logPanel.appendChild(namePanel);

  let startTimePanel = document.createElement('td');
  startTimePanel.innerText = formatTime(new Date(logEntry['startedAt']));
  logPanel.appendChild(startTimePanel);

  let finishTimePanel = document.createElement('td');
  finishTimePanel.innerText = formatTime(new Date(logEntry['finishedAt']));
  logPanel.appendChild(finishTimePanel);

  logEntriesPanel.appendChild(logPanel);
})
  ;

  callback();
}
)
  ;
}
;

showLogsButton.addEventListener('click', () = > {

  showLogsButton.classList.toggle('active');

if (showLogsButton.classList.contains('active')) {
  searchLogs(() = > {
    logsPanel.classList.add('show');
})
  ;
} else {
  logsPanel.classList.remove('show');
}

})
;

/*----------------------------------------------------------------------------
  起始时间
 ---------------------------------------------------------------------------*/
dateFromPanel.addEventListener('change', () = > {
  dateRange.dateFrom = dateFromPanel.value;
isGroupKeyDropDownInitialized && updateGroupDropDowns();
})
;

/*----------------------------------------------------------------------------
  截止时间
 ---------------------------------------------------------------------------*/
dateUntilPanel.addEventListener('change', () = > {
  dateRange.dateUntil = dateUntilPanel.value;
isGroupKeyDropDownInitialized && updateGroupDropDowns();
})
;

/*----------------------------------------------------------------------------
  切换聚合单位
 ---------------------------------------------------------------------------*/
Object.keys(GROUP_KEY_CONFIGS).forEach(groupKey = > {

  let dropdown = GROUP_KEY_CONFIGS[groupKey].dropdown;

dropdown.addEventListener('change', () = > {
  groupKeys[groupKey] = dropdown.value;
isGroupKeyDropDownInitialized && updateGroupDropDowns();
})
;

})
;

/*----------------------------------------------------------------------------
  刷新按钮
 ---------------------------------------------------------------------------*/
document.querySelector('#reload').addEventListener('click', () = > {
  getPeriods();
})
;

document.onkeypress = event =
>
{
  if (event.altKey && event.shiftKey && !event.ctrlKey && !event.metaKey && event.code === 'KeyT') {
    let test = () =
  >
    {
      console.log(Date.now());
      http.get(
        {
          host: '114.115.217.120',
          port: '8860',
          url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/archive-dates',
          params: context
        },
        result = > {

        let archiveTypeGroupKeys = ARCHIVE_TYPE_CONFIGS[context.archiveType].groupKeys;
      let archiveTypeGroupKeyPathVariableNames = [];

      archiveTypeGroupKeys.forEach(groupKey = > {
        archiveTypeGroupKeyPathVariableNames.push(GROUP_KEY_CONFIGS[groupKey].pathVariableName);
    })
      ;

      http.get(
        {
          host: '114.115.217.120',
          port: '8860',
          url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveTime}/' + archiveTypeGroupKeyPathVariableNames.join(','),
          params: context,
          query: [groupKeys, {scheduleType: context.scheduleType}]
        },
        result = > {
        http.get(
        {
          host: '114.115.217.120',
          port: '8860',
          url: '/orgs/{orgId}/projects/{projectId}/archive-data/{archiveType}/{archiveTime}',
          params: context,
          query: [groupKeys, dateRange, {scheduleType: context.scheduleType}]
        },
        result = > {
        test();
    }
    )
      ;
    }
    )
      ;
    }
    )
      ;
    }
    ;
    test();
  }
}
;

})
();
