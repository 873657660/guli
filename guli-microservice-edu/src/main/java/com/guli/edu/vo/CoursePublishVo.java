package com.guli.edu.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyanan
 * @create 2019-10-10 8:44
 */
@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}