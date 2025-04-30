package com.ose.tasks.vo.drawing;


/**
 * 图纸审核状态
 */
public enum DrawingReviewStatus {
    INIT, //待提交
    CHECK, //校对中
    REVIEW, //审核中
    MODIFY, //修改中
    CHECK_DONE,//已校对
    PRE_APPROVED,//内部批准
    APPROVED,//已批准
    REVIEW_DONE //已审核
}
