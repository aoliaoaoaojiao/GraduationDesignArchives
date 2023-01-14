package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 整个城市的分数
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TheWholeCityScore {
    private Long timestamp;

    private Double scoreAvg;

    private String overallSituation;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getScoreAvg() {
        return scoreAvg;
    }

    public void setScoreAvg(Double scoreAvg) {
        this.scoreAvg = scoreAvg;
    }

    public String getOverallSituation() {
        return overallSituation;
    }

    public void setOverallSituation(String overallSituation) {
        this.overallSituation = overallSituation;
    }
}