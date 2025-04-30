#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');
const projectDir = path.join(__dirname, '../../');
const result = {files: 0};

const readDir = (projectDir, dir) => {

  dir = dir || '';

  fs.readdirSync(path.join(projectDir, dir)).forEach(filename => {

    if (filename.startsWith('.')) {
      return;
    }

    const filePath = path.join(projectDir, dir, filename);
    const stats = fs.statSync(filePath);

    if (stats.isDirectory()) {
      return readDir(projectDir, path.join(dir, filename));
    }

    const extName = path.extname(filePath).toLowerCase();

    if ((extName !== '.java'
          && extName !== '.xml'
          && extName !== '.properties')
        || !dir.match(/^ose-\w+(-api)?(\/|$)/)
        || dir.match(/^ose-\w+(-api)?\/target(\/|$)/)
        || dir.startsWith('ose-test')) {
      return;
    }

    const projectName = dir.match(/^(ose-\w+)(-api)?(\/|$)/)[1];
    const content = fs
      .readFileSync(filePath, 'utf8')
      .replace(/\r/g, '')
      .replace(/\s+\n/, '\n');
    const lines = (content.match(/\n/g) || []).length;
    const contentWithNoBlankLine = content
      .replace(/\n+/g, '\n')
      .replace(/\n$/, '');
    const linesWithNoBlankLine = (contentWithNoBlankLine.match(/\n/g) || []).length;
    const contentWithNoComment = (extName === '.java')
      ? contentWithNoBlankLine
        .replace(/^\s*\/\/.+\n/g, '')
        .replace(/(\/\*)(.|\n)+?(\*\/)\s*\n?/g, '')
      : (
        (extName === '.xml')
          ? contentWithNoBlankLine.replace(/(<!-)(.|\n)+?(-->)\s*\n?/g, '')
          : contentWithNoBlankLine.replace(/^\s*#.+\n/g, '')
      );
    const linesWithNoComment = (contentWithNoComment.match(/\n/g) || []).length;
    const contentCompressed = contentWithNoComment.replace(/\n[\s{}();]+\n/, '\n');
    const linesCompressed = (contentCompressed.match(/\n/g) || []).length;

    result.files++;

    const fileTypeStats = result[extName] = result[extName] || {
      files: 0,
      lines: 0,
      linesWithNoBlankLine: 0,
      linesWithNoComment: 0,
      compressed: 0
    };

    const projectStats = fileTypeStats[projectName] = fileTypeStats[projectName] || {
      files: 0,
      lines: 0,
      linesWithNoBlankLine: 0,
      linesWithNoComment: 0,
      compressed: 0
    };

    fileTypeStats.files++;
    fileTypeStats.lines += lines;
    fileTypeStats.linesWithNoBlankLine += linesWithNoBlankLine;
    fileTypeStats.linesWithNoComment += linesWithNoComment;
    fileTypeStats.compressed += linesCompressed;

    projectStats.files++;
    projectStats.lines += lines;
    projectStats.linesWithNoBlankLine += linesWithNoBlankLine;
    projectStats.linesWithNoComment += linesWithNoComment;
    projectStats.compressed += linesCompressed;
  });

};

const print = fileType => {

  console.log(`\n${fileType}`);

  const fileTypeStats = result[`.${fileType.toLowerCase()}`];

  Object.keys(fileTypeStats).forEach(projectName => {

    if (!projectName.startsWith('ose-')) {
      return;
    }

    const projectStats = fileTypeStats[projectName];
    console.log(`${projectName}\t${projectStats.files}\t${projectStats.lines}\t${projectStats.linesWithNoBlankLine}\t${projectStats.linesWithNoComment}\t${projectStats.compressed}`);
  });

  console.log(`合计\t${fileTypeStats.files}\t${fileTypeStats.lines}\t${fileTypeStats.linesWithNoBlankLine}\t${fileTypeStats.linesWithNoComment}\t${fileTypeStats.compressed}`);
};

readDir(projectDir);

print('Java');
print('XML');
print('Properties');
