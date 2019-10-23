package com.guli.ucenter.controller.admin;

import com.guli.common.vo.R;
import com.guli.ucenter.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author zyn
 * @create 2019-10-14 16:38
 */
@RequestMapping("/admin/ucenter/member")
@CrossOrigin
@RestController
public class MemberAdminController {

    @Autowired
    private MemberService memberService;

    @Value("${server.port}")
    private Integer port;

    @ApiOperation(value = "今日注册数")
    @GetMapping(value = "count-register/{day}")
    public R getRegisterCount(
            @ApiParam(name="day", value="统计日期")
            @PathVariable String day) {

        System.out.println("会员管理被调用，端口号为：" + port);

        Integer count = memberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }


}
