package com.ose.report.entity.dynamicreport;

import java.util.ArrayList;
import java.util.List;

public class ReportDataSource {

    //注意和模板中数据类型保持一致
    private String stuNo;

    private String stuName;

    private List<YnnData> subReportList = new ArrayList<YnnData>();

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public List<YnnData> getSubReportList() {
        return subReportList;
    }

    public void setSubReportList(List<YnnData> subReportList) {
        this.subReportList = subReportList;
    }
}
