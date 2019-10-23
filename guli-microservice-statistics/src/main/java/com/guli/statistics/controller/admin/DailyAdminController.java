package com.guli.statistics.controller.admin;

import com.guli.common.vo.R;
import com.guli.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zyn
 * @create 2019-10-14 17:19
 */
@Api(description = "统计分析管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/statistics/daily")
public class DailyAdminController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation(value = "根据日期统计当前日期的各项信息")
    @GetMapping("{day}")
    public R createStatisticsByDate(
            @ApiParam(name = "day", value = "统计日期")
            @PathVariable String day) {

        dailyService.createStatisticsByDay(day);
        return R.ok();
    }

    @ApiOperation(value = "根据查询条件查询统计信息")
    @GetMapping("show-chart/{begin}/{end}/{type}")
    public R showChart(
            @ApiParam(name = "begin", value = "开始日期", required = true)
            @PathVariable("begin") String begin,
            @ApiParam(name = "end", value = "结束日期", required = true)
            @PathVariable("end") String end,
            @ApiParam(name = "type", value = "查询类型", required = true)
            @PathVariable("type") String type){

        Map<String, Object> map = dailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }

}
