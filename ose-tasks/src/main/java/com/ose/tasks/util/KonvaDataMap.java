package com.ose.tasks.util;

import java.util.HashMap;
import java.util.Map;

public class KonvaDataMap {


    public static Map<String, String> subTypeMap = new HashMap<String, String>(){{
        subTypeMap.put("TEXT", "1");
        subTypeMap.put("LINK", "2");
        subTypeMap.put("FREETEXT","3");
        subTypeMap.put("LINE","4");
        subTypeMap.put("SQUARE","5");
        subTypeMap.put("CIRCLE","6");
        subTypeMap.put("POLYGON","7");
        subTypeMap.put("POLYLINE","8");
        subTypeMap.put("HIGHLIGHT","9");
        subTypeMap.put("UNDERLINE","10");
        subTypeMap.put("SQUIGGLY","11");
        subTypeMap.put("STRIKEOUT","12");
        subTypeMap.put("STAMP","13");
        subTypeMap.put("CARET","14");
        subTypeMap.put("INK","15");
        subTypeMap.put("POPUP","16");
        subTypeMap.put("FILEATTACHMENT","17");
        subTypeMap.put("SOUND","18");
        subTypeMap.put("MOVIE","19");
        subTypeMap.put("WIDGET","20");
        subTypeMap.put("SCREEN","21");
        subTypeMap.put("PRINTERMARK","22");
        subTypeMap.put("TRAPNET","23");
        subTypeMap.put("WATERMARK","24");
        subTypeMap.put("THREED","25");
        subTypeMap.put("REDACT","26");

    }};

    //        | 1 // TEXT
//            | 2 // LINK
//            | 3 // FREETEXT
//            | 4 // LINE
//            | 5 // SQUARE
//            | 6 // CIRCLE
//            | 7 // POLYGON
//            | 8 // POLYLINE
//            | 9 // HIGHLIGHT
//            | 10 // UNDERLINE
//            | 11 // SQUIGGLY
//            | 12 // STRIKEOUT
//            | 13 // STAMP
//            | 14 // CARET
//            | 15 // INK
//            | 16 // POPUP
//            | 17 // FILEATTACHMENT
//            | 18 // SOUND
//            | 19 // MOVIE
//            | 20 // WIDGET
//            | 21 // SCREEN
//            | 22 // PRINTERMARK
//            | 23 // TRAPNET
//            | 24 // WATERMARK
//            | 25 // THREED
//            | 26 // REDACT
}

/*
{
  highlight
  AnnotationType.HIGHLIGHT
  PdfjsAnnotationEditorType.HIGHLIGHT
  PdfjsAnnotationType.HIGHLIGHT
  Highlight
  }
  {
  strikeout
  AnnotationType.STRIKEOUT
  PdfjsAnnotationEditorType.HIGHLIGHT
  PdfjsAnnotationType.STRIKEOUT
  StrikeOut
  }
  {
  underline
  AnnotationType.UNDERLINE
  PdfjsAnnotationEditorType.HIGHLIGHT
  PdfjsAnnotationType.UNDERLINE
  Underline
  }
  {
  rectangle
  AnnotationType.RECTANGLE
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.SQUARE
  Square
  }
  {
  circle
  AnnotationType.CIRCLE
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.CIRCLE
  Circle
  }
  {
  freehand
  AnnotationType.FREEHAND
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.INK
  Ink
  }
  {
  freeHighlight
  AnnotationType.FREE_HIGHLIGHT
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.POLYLINE
  PolyLine
  }
  {
  freeText
  AnnotationType.FREETEXT
  PdfjsAnnotationEditorType.STAMP
  PdfjsAnnotationType.FREETEXT
  FreeText
  }
  {
  signature
  AnnotationType.SIGNATURE
  PdfjsAnnotationEditorType.STAMP
  PdfjsAnnotationType.CARET
  Caret
  }
  {
  stamp
  AnnotationType.STAMP
  PdfjsAnnotationEditorType.STAMP
  PdfjsAnnotationType.STAMP
  Ink
  }
  {
  line
  AnnotationType.LINE
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.LINE
  Line
  }
  {
  arrow
  AnnotationType.ARROW
  PdfjsAnnotationEditorType.INK
  PdfjsAnnotationType.POLYLINE
  PolyLine
  }

    NONE = -1, // 没有批注类型
  SELECT = 0, // 选择批注
  HIGHLIGHT = 1, // 高亮批注
  STRIKEOUT = 2, // 删除线批注
  UNDERLINE = 3, // 下划线批注
  FREETEXT = 4, // 自由文本批注
  RECTANGLE = 5, // 矩形批注
  CIRCLE = 6, // 圆形批注
  FREEHAND = 7, // 自由绘制批注
  FREE_HIGHLIGHT = 8, // 自由高亮批注
  SIGNATURE = 9, // 签名批注
  STAMP = 10, // 盖章批注
  LINE = 11, // 直线批注
  ARROW = 12, // 箭头批注


  export enum PdfjsAnnotationEditorType {
  DISABLE = -1, // 禁用批注编辑器
  NONE = 0, // 没有批注类型
  FREETEXT = 3, // 自由文本批注
  LINE = 4,
  POLYLINE = 8,
  HIGHLIGHT = 9, // 高亮批注
  STAMP = 13, // 盖章批注
  INK = 15, // 墨迹（自由绘制）批注
}
 */
