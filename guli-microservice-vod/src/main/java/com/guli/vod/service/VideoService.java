package com.guli.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhangyanan
 * @create 2019-10-10 21:34
 */
public interface VideoService {

    String uploadVideo(MultipartFile file);

    void removeVideoById(String videoSourceId);

    void removeVideoByIdList(List<String> videoSourceIdList);
}
