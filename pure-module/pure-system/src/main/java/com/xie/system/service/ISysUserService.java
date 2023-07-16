package com.xie.system.service;

import com.xie.system.domain.bo.SysUserBo;

/**
 * @作者：xie
 * @时间：2023/7/16 21:01
 */
public interface ISysUserService {
    boolean checkUserNameUnique(SysUserBo sysUser);

    boolean registerUser(SysUserBo sysUser);
}
