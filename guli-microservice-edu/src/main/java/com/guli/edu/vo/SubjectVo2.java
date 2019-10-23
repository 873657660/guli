package com.guli.edu.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyanan
 * @create 2019-09-30 1:23
 */
@Data
public class SubjectVo2 {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private List<SubjectVo2> children = new ArrayList<>();
}
