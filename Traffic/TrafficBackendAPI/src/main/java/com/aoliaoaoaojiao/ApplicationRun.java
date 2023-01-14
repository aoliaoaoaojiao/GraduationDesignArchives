package com.aoliaoaoaojiao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序运行
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/27
 */
@SpringBootApplication
@MapperScan("com.aoliaoaoaojiao.dao")
public class ApplicationRun {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRun.class,args);
    }
}
