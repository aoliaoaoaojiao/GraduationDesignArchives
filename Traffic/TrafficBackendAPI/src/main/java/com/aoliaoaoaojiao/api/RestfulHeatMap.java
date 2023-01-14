package com.aoliaoaoaojiao.api;

/**
 * 热力图数据
 *
 * @author A
 * @date 2020/06/01
 */
public class RestfulHeatMap {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;
    /**
     * 交通拥堵指数
     */
    private Double count;

    public RestfulHeatMap(){}

    public RestfulHeatMap(String lng,String lat,Double count){
        this.lng = lng;
        this.lat = lat;
        this.count = count;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
