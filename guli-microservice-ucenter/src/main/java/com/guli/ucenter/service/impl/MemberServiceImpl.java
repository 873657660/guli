package com.guli.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.mapper.MemberMapper;
import com.guli.ucenter.service.MemberService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zhangyanan
 * @since 2019-10-14
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

//    @Autowired
//    private MemberMapper memberMapper;

    @Override
    public Integer countRegisterByDay(String day) {

        return baseMapper.selectRegisterCount(day);

    }

    /**
     * 根据openid查询数据库中的用户
     * @param id
     * @return
     */
    @Override
    public Member getByOpenid(String id) {

        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("openid", id);

        return baseMapper.selectOne(memberQueryWrapper);
    }
}
