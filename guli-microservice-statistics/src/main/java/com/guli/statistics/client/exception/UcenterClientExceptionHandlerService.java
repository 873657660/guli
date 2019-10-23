package com.guli.statistics.client.exception;

import com.guli.common.vo.R;
import com.guli.statistics.client.UcenterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jay
 * @create 2019-10-15 9:24
 */
@Component
@Slf4j
public class UcenterClientExceptionHandlerService implements UcenterClient {
    /**
     * 当调用 UcenterClient中的 getRegisterCount方法失败时,
     *  会熔断，进而调用UcenterClientExceptionHandlerService中的getRegisterCount方法
     * @param day
     * @return
     */
    @Override
    public R getRegisterCount(String day) {
        log.error("远程调用失败，熔断处理");
        return R.ok().data("countRegister", 0);
    }
}
