package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.guli.edu.utils.ExcelImportUtil;
import com.guli.edu.vo.SubjectVo2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-09-24
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> batchImport(MultipartFile file) throws Exception{
        //错误消息列表
        List<String> errorMsg = new ArrayList<>();

        //创建工具类对象
        ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());
        //获取工作表
        Sheet sheet = excelHSSFUtil.getSheet();

        int rowCount = sheet.getPhysicalNumberOfRows();
        if (rowCount <= 1) {
            errorMsg.add("请填写数据");
            return errorMsg;
        }

        for (int rowNum = 1; rowNum < rowCount; rowNum++) {

            Row rowData = sheet.getRow(rowNum);
            if (rowData != null) {// 行不为空

                //获取一级分类
                String levelOneValue = "";
                Cell levelOneCell = rowData.getCell(0);
                if(levelOneCell != null){
                    levelOneValue = excelHSSFUtil.getCellValue(levelOneCell).trim();
                    if (StringUtils.isEmpty(levelOneValue)) {
                        errorMsg.add("第" + rowNum + "行一级分类为空");
                        continue;
                    }
                }

                //判断一级分类是否重复
                Subject subject = this.getByTitle(levelOneValue);
                String parentId = null;
                if(subject == null){
                    //将一级分类存入数据库
                    Subject subjectLevelOne = new Subject();
                    subjectLevelOne.setTitle(levelOneValue);
                    subjectLevelOne.setSort(rowNum);
                    baseMapper.insert(subjectLevelOne);//添加

                    //获取id（二级分类的parent_id）
                    parentId = subjectLevelOne.getId();
                }else{
                    parentId = subject.getId();
                }


                //从excel中获取二级分类的数据
                String levelTwoValue = "";
                Cell levelTwoCell = rowData.getCell(1);
                if(levelTwoCell != null){
                    levelTwoValue = excelHSSFUtil.getCellValue(levelTwoCell).trim();
                    if (StringUtils.isEmpty(levelTwoValue)) {
                        errorMsg.add("第" + rowNum + "行二级分类为空");
                        continue;
                    }
                }

                //判断从数据库中二级分类的数据是否已存在
                Subject subjectSub = this.getSubByTitle(levelTwoValue, parentId);
                Subject subjectLevelTwo = null;
                if(subjectSub == null){
                    // 如果不存在，则将数据插入数据库中
                    subjectLevelTwo = new Subject();
                    subjectLevelTwo.setTitle(levelTwoValue);
                    subjectLevelTwo.setParentId(parentId);
                    subjectLevelTwo.setSort(rowNum);
                    baseMapper.insert(subjectLevelTwo);//添加
                }

            }
        }

        return errorMsg;

    }

    @Override
    public List<SubjectVo2> nestedList2() {
        return baseMapper.selectNestedListByParentId("0");
    }

    /**
     * 判断一级分类是否重复
     * @param title
     * @return
     */
    private Subject getByTitle(String title) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");//一级分类
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 判断二级分类是否重复
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title, String parentId) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(queryWrapper);
    }
}
