package com.guli.vod.controller.admin;

import com.guli.common.vo.R;
import com.guli.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhangyanan
 * @create 2019-10-10 22:05
 */

@Api(description="阿里云视频点播微服务")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/vod/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        String videoId = videoService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId", videoId);
    }

    @DeleteMapping("{videoSourceId}")
    public R removeVideo(
            @ApiParam(name="videoSourceId", value="阿里云视频id", required = true)
            @PathVariable String videoSourceId){

        videoService.removeVideoById(videoSourceId);
        return R.ok().message("视频删除成功");
    }

    @DeleteMapping("{remove}")
    public R removeVideoByIdList(
            @ApiParam(name="videoSourceIdList", value="阿里云视频文件的id列表", required = true)
            @RequestBody List<String> videoSourceIdList){

        videoService.removeVideoByIdList(videoSourceIdList);

        return R.ok().message("视频删除成功");
    }

}