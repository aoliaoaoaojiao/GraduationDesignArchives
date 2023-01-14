package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 每个地区综合数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EveryRegionComplexData {
    private Long timestamp;

    private Double areaScore;

    private Double areaSpeedAvg;

    private String adCode;

    private String areaStatus;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAreaScore() {
        return areaScore;
    }

    public void setAreaScore(Double areaScore) {
        this.areaScore = areaScore;
    }

    public Double getAreaSpeedAvg() {
        return areaSpeedAvg;
    }

    public void setAreaSpeedAvg(Double areaSpeedAvg) {
        this.areaSpeedAvg = areaSpeedAvg;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(String areaStatus) {
        this.areaStatus = areaStatus;
    }
}