package com.guli.common.exception;

import com.guli.common.constants.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyanan
 * @create 2019-09-29 17:42
 */
@Data
@ApiModel(value ="自定义全局异常类")
public class GuliException extends RuntimeException{

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误信息创建异常对象
     * @param message
     * @param code
     */
    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message" + this.getMessage() +
                '}';
    }
}
