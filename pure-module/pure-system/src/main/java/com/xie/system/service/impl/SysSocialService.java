package com.xie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.common.core.utils.MapstructUtils;
import com.xie.system.domain.SysSocial;
import com.xie.system.domain.bo.SysSocialBo;
import com.xie.system.domain.vo.SysSocialVo;
import com.xie.system.mapper.SysSocialMapper;
import com.xie.system.service.ISysSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/7/8 18:41
 */
@Service
@RequiredArgsConstructor
public class SysSocialService implements ISysSocialService {

    private final SysSocialMapper baseMapper;
    @Override
    public SysSocialVo selectByAuthId(String authId) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<SysSocial>()
                .eq(SysSocial::getAuthId, authId));
    }

    @Override
    public Boolean insertByBo(SysSocialBo bo) {
        SysSocial add = MapstructUtils.convert(bo, SysSocial.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            if (add != null) {
                bo.setId(add.getId());
            } else {
                return false;
            }
        }
        return flag;
    }

    private void validEntityBeforeSave(SysSocial entity) {
        //TODO 做一些数据校验,如唯一约束
    }
}
