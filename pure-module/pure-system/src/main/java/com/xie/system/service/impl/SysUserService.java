package com.xie.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.common.core.utils.MapstructUtils;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.bo.SysUserBo;
import com.xie.system.mapper.SysUserMapper;
import com.xie.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/7/16 21:02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserService implements ISysUserService {

    private final SysUserMapper baseMapper;
    @Override
    public boolean checkUserNameUnique(SysUserBo sysUser) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, sysUser.getUserName())
                .ne(ObjectUtil.isNotNull(sysUser.getUserId()), SysUser::getUserId, sysUser.getUserId()));
        return !exists;
    }

    @Override
    public boolean registerUser(SysUserBo sysUser) {
        sysUser.setCreateBy(sysUser.getUserId());
        sysUser.setUpdateBy(sysUser.getUserId());
        SysUser user = MapstructUtils.convert(sysUser, SysUser.class);
        return baseMapper.insert(user) > 0;
    }
}
