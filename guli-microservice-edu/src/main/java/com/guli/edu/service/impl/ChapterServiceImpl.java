package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.entity.Chapter;
import com.guli.edu.entity.Video;
import com.guli.edu.mapper.ChapterMapper;
import com.guli.edu.mapper.VideoMapper;
import com.guli.edu.service.ChapterService;
import com.guli.edu.vo.ChapterVo;
import com.guli.edu.vo.VideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-09-24
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    
    @Autowired
    private VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        videoMapper.delete(videoQueryWrapper);

        Integer result = baseMapper.deleteById(id);
        return null != result && result >= 0;
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {

        List<ChapterVo> chapterVoList = new ArrayList<>();

        // 获取章信息
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        chapterQueryWrapper.orderByAsc("sort", "id");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);

        // 获取课程信息
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        videoQueryWrapper.orderByAsc("sort", "id");
        List<Video> videoList = videoMapper.selectList(videoQueryWrapper);

        // 填充列表数据
        for (int i = 0; i < chapterList.size(); i++) {
            Chapter chapter = chapterList.get(i);

            // 创建ChapterVo对象
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            chapterVoList.add(chapterVo);

            // 填充到表数据，video列表
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < videoList.size(); j++) {
                Video video = videoList.get(j);
                // 如果课程的章节id和章节id对应
                if(chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoList);

        }
        
        return chapterVoList;
    }
}
