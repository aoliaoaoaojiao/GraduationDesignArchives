package com.aoliaoaoaojiao.entity;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 不同方向的综合分数
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffDirectionScore {
    private Long timestamp;

    private Double directionScore;

    private Double directionSpeedAvg;

    private String direction;

    private String directionStatus;


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getDirectionScore() {
        return directionScore;
    }

    public void setDirectionScore(Double directionScore) {
        this.directionScore = directionScore;
    }

    public Double getDirectionSpeedAvg() {
        return directionSpeedAvg;
    }

    public void setDirectionSpeedAvg(Double directionSpeedAvg) {
        this.directionSpeedAvg = directionSpeedAvg;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirectionStatus() {
        return directionStatus;
    }

    public void setDirectionStatus(String directionStatus) {
        this.directionStatus = directionStatus;
    }
}