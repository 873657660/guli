package com.guli.edu.client.exception;

import com.guli.common.vo.R;
import com.guli.edu.client.OssClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jay
 * @create 2019-10-15 20:50
 */
@Component
@Slf4j
public class OssClientExceptionHandlerService implements OssClient {

    @Override
    public R removeFile(String url) {
        log.error("删除文件失败，熔断处理");
        return R.ok().data("删除失败", 0);
    }
}
