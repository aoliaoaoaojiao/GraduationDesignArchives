package com.aoliaoaoaojiao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分组需要的数据
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/28
 */
@Component
@ConfigurationProperties("groupfield.group")
public class DataNeededForGrouping {

    private List<String> adCodes;

    private List<String> directions;

    private List<String> levels;

    public void setAdCodes(List<String> adCodes) {
        this.adCodes = adCodes;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public List<String> getAdCodes() {
        return adCodes;
    }

    public List<String> getDirections() {
        return directions;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }
}
