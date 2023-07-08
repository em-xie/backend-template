package com.xie.system.mapper;

import com.xie.common.mybatis.core.mapper.BaseMapperPlus;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @作者：xie
 * @时间：2023/7/6 22:32
 */

public interface SysUserMapper extends BaseMapperPlus<SysUser, SysUserVo> {

    @Select("select * from sys_user where user_name = #{userName} and del_flag = '0'")
    SysUserVo selectUserByUserName(String userName);
}