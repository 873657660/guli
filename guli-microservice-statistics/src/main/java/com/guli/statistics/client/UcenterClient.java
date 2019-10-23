package com.guli.statistics.client;

import com.guli.common.vo.R;
import com.guli.statistics.client.exception.UcenterClientExceptionHandlerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author shkstart
 * @create 2019-10-14 16:33
 */
@Component   // 熔断处理，fallback
@FeignClient(value = "guli-ucenter", fallback = UcenterClientExceptionHandlerService.class)
public interface UcenterClient {

    /**
     * 注意：一定要写成 @PathVariable("day")，圆括号中的"day"不能少
     * @param day
     * @return
     */
    @ApiOperation(value = "今日注册数")
    @GetMapping(value = "/admin/ucenter/member/count-register/{day}")
    public R getRegisterCount(
            @ApiParam(name="day", value="统计日期")
            @PathVariable(value = "day") String day);


}
