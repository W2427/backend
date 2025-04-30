package com.ose.baiduNormalTranslate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NormalTranslateResult implements Serializable {

    private static final long serialVersionUID = -3696038370955418820L;
    private String from;

    private String to;

    private String transResult;

    @JsonCreator
    public NormalTranslateResult(@JsonProperty("transResult") List<TranslateDetail> transResult) {

        this.transResult = StringUtils.toJSON(transResult);
    }

    public NormalTranslateResult() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTransResult() {
        return transResult;
    }

    public void setTransResult(String transResult) {
        this.transResult = transResult;
    }

    @JsonProperty(value = "transResult", access = JsonProperty.Access.READ_ONLY)
    public List<TranslateDetail> getJsonTransResult() {
        if (transResult != null && !"".equals(transResult)) {
            return StringUtils.decode(transResult, new TypeReference<List<TranslateDetail>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonTransResult(List<TranslateDetail> transResult) {
        if (transResult != null) {
            this.transResult = StringUtils.toJSON(transResult);
        }
    }
}
