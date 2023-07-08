package com.xie.test;

import com.xie.common.core.utils.MapstructUtils;
import com.xie.system.domain.SysClient;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.vo.SysUserVo;
import com.xie.system.mapper.SysClientMapper;
import com.xie.system.mapper.SysUserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @作者：xie
 * @时间：2023/7/7 22:19
 */
@SpringBootTest
public class MapStruct {

    @Autowired
    private  SysUserMapper sysUserMapper;
    @Autowired
    private SysClientMapper baseMapper;
    @Test
    public void Test1(){
//        SysUserVo admin = sysUserMapper.selectUserByUserName("admin");
//        SysUser convert = MapstructUtils.convert(admin, SysUser.class);
//        System.out.println(convert);

//        SysClient sysClient = baseMapper.selectById(1);
//        SysClient convert = MapstructUtils.convert(sysClient, SysClient.class);
//        System.out.println(convert);

    }




}
