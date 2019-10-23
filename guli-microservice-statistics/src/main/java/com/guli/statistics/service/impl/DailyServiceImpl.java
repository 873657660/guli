package com.guli.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.vo.R;
import com.guli.statistics.client.UcenterClient;
import com.guli.statistics.entity.Daily;
import com.guli.statistics.mapper.DailyMapper;
import com.guli.statistics.service.DailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-10-14
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDay(String day) {

        //删除已存在的统计对象
        QueryWrapper<Daily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        Daily daily = new Daily();

        R r = ucenterClient.getRegisterCount(day);
        Integer registerNum = (Integer)r.getData().get("countRegister");

        daily.setDateCalculated(day);
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {

        Map<String, Object> map = new HashMap<>();

        // 日期列表dateList和数据列表dataList
        List<String> dateList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        QueryWrapper<Daily> dailyQueryWrapper = new QueryWrapper<>();
        dailyQueryWrapper.select(type, "date_calculated");
        dailyQueryWrapper.between("date_calculated", begin, end);

        List<Daily> dailyList = baseMapper.selectList(dailyQueryWrapper);

        for (int i = 0; i < dailyList.size(); i++) {
            Daily daily = dailyList.get(i);
            dateList.add(daily.getDateCalculated());

            switch (type) {
                case "login_num":
                    dataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    dataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    dataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        map.put("dateList", dateList);
        map.put("dataList", dataList);

        return map;
    }

}
