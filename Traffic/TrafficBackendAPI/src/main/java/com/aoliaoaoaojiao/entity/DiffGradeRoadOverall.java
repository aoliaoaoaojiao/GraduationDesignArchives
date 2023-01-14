package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 不同等级道路的总体情况
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffGradeRoadOverall {
    private Long timestamp;

    private Double scoreAvg;

    private Double diffLevelSpeed;

    private String level;

    private String diffLevelRoadStatus;

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

    public Double getDiffLevelSpeed() {
        return diffLevelSpeed;
    }

    public void setDiffLevelSpeed(Double diffLevelSpeed) {
        this.diffLevelSpeed = diffLevelSpeed;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiffLevelRoadStatus() {
        return diffLevelRoadStatus;
    }

    public void setDiffLevelRoadStatus(String diffLevelRoadStatus) {
        this.diffLevelRoadStatus = diffLevelRoadStatus;
    }
}