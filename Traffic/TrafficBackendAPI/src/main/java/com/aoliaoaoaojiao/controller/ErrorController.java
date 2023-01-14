package com.aoliaoaoaojiao.controller;

import com.aoliaoaoaojiao.api.CommonRestful;
import com.aoliaoaoaojiao.exception.ParamIsValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 错误控制器
 *
 * @author 奥利嗷嗷嗷叫
 * @date 2020/05/30
 */
@RestControllerAdvice
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler({ParamIsValueException.class})
    public CommonRestful paramValueError(ParamIsValueException e){
        logger.info("时间参数检查与处理-RuntimeException");
        return CommonRestful.notFound(e.getMess());
    }
}
