package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 道路路段实时数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RealtimeSectionStatus {
    private Double speed;

    private Long timestamp;

    private Double dtp;

    private Double tpi;

    private String name;

    private String level;

    private String specificInformation;

    private String adCode;

    private String direction;

    private String status;

    private String polyline;

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getDtp() {
        return dtp;
    }

    public void setDtp(Double dtp) {
        this.dtp = dtp;
    }

    public Double getTpi() {
        return tpi;
    }

    public void setTpi(Double tpi) {
        this.tpi = tpi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpecificInformation() {
        return specificInformation;
    }

    public void setSpecificInformation(String specificInformation) {
        this.specificInformation = specificInformation;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}