package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.Subject;
import com.guli.edu.vo.SubjectVo2;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-09-24
 */
public interface SubjectService extends IService<Subject> {

    List<String> batchImport(MultipartFile file) throws Exception;

    List<SubjectVo2> nestedList2();

}
