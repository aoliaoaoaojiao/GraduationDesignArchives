package com.aoliaoaoaojiao.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 各区域不同交通状况的路段总数
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EveryRegionStatusCount {
    private Long timestamp;

    private Long countStatus;

    private String adCode;

    private String status;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCountStatus() {
        return countStatus;
    }

    public void setCountStatus(Long countStatus) {
        this.countStatus = countStatus;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}