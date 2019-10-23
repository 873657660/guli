package com.guli.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExceptionUtil;
import com.guli.vod.service.VideoService;
import com.guli.vod.util.AliyunVodSDKUtils;
import com.guli.vod.util.ConstantPropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhangyanan
 * @create 2019-10-10 21:34
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Override
    public String uploadVideo(MultipartFile file) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }

        // 调用阿里云SDK进行文件上传
        String originalFilename = file.getOriginalFilename();
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        UploadStreamRequest request = new UploadStreamRequest(
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.ACCESS_KEY_SECRET,
                title,
                originalFilename,
                inputStream);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);

        String videoId = response.getVideoId();
        if(StringUtils.isEmpty(videoId)) {
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }

        return videoId;
    }

    @Override
    public void removeVideoById(String videoSourceId) {

        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoSourceId);

        DefaultAcsClient client = null;
        try {
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            client.getAcsResponse(request);

        } catch (ClientException e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }

    }

    @Override
    public void removeVideoByIdList(List<String> videoSourceIdList) {

        DefaultAcsClient client = null;
        try {
            client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET);

            DeleteVideoRequest request = new DeleteVideoRequest();

            int size = videoSourceIdList.size();
            StringBuffer idListStr = new StringBuffer();
            for (int i = 0; i < size; i++) {
                // 将视频列表中的每一个id追加到idListStr后面
                idListStr.append(videoSourceIdList.get(i));

                if(i == size - 1 || i % 20 == 19){
                    // 支持传入多个视频，多个用逗号隔开
                    request.setVideoIds(idListStr.toString());
                    client.getAcsResponse(request);

                }else if(i % 20 < 19) {
                    idListStr.append(",");
                }
            }

        } catch (ClientException e) {
            log.error(ExceptionUtil.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }


}
