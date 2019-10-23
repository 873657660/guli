package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-09-24
 */

public interface TeacherService extends IService<Teacher> {

    void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery);

    List<Map<String, Object>> selectNameListByKey(String key);

    public Map<String, Object> pageListWeb(Page<Teacher> pageParam);

}
