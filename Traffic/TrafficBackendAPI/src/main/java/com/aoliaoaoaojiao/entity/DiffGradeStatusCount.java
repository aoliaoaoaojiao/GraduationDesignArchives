package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 各道路等级总体不同状态数
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffGradeStatusCount {
    private Long timestamp;

    private Long diffLevelCountStatus;

    private String level;

    private String roadStatus;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDiffLevelCountStatus() {
        return diffLevelCountStatus;
    }

    public void setDiffLevelCountStatus(Long diffLevelCountStatus) {
        this.diffLevelCountStatus = diffLevelCountStatus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRoadStatus() {
        return roadStatus;
    }

    public void setRoadStatus(String roadStatus) {
        this.roadStatus = roadStatus;
    }
}