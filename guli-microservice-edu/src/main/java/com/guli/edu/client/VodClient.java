package com.guli.edu.client;

/**
 * @author Jay
 * @create 2019-10-20 19:05
 */

import com.guli.common.vo.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value="guli-vod")
public interface VodClient {

    @DeleteMapping("{/admin/vod/video/remove}")
    public R removeVideoByIdList(@RequestBody List<String> videoSourceIdList);

}
