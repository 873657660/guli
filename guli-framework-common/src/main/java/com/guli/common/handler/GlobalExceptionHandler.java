package com.guli.common.handler;

import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExceptionUtil;
import com.guli.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangyanan
 * @create 2019-09-24 19:25
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e) {
        log.error(e.getMessage());
        //log.error(ExceptionUtil.getMessage(e));
        return R.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException e){
        log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException e){
        log.error(ExceptionUtil.getMessage(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }


    /**
     * 自定义异常处理方法
     * @param e
     * @return
     */
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }


}
