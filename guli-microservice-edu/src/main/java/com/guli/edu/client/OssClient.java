package com.guli.edu.client;

import com.guli.common.vo.R;
import com.guli.edu.client.exception.OssClientExceptionHandlerService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Jay
 * @create 2019-10-15 11:22
 */
@Component
@FeignClient(value="guli-oss", fallback = OssClientExceptionHandlerService.class)
public interface OssClient {

    @DeleteMapping("/admin/oss/file/remove")
    public R removeFile(@RequestBody String url);
}
