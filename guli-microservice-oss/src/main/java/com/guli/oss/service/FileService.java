package com.guli.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangyanan
 * @create 2019-09-28 19:52
 */
public interface FileService {
    /*
     * @param file
     * @param fileHost 文件上传的具体路径
     * @return
     */
    String upload(MultipartFile file, String fileHost);

    void removeFile(String url);
}
