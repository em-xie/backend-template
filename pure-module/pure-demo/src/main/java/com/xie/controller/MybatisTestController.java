package com.xie.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.xie.common.core.domain.R;
import com.xie.common.core.utils.MapstructUtils;
import com.xie.mapper.MybatisTestMapper;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.vo.SysUserVo;
import com.xie.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @作者：xie
 * @时间：2023/7/2 19:59
 */
@SaIgnore
@RestController
@RequestMapping("/demo/mybatis")
public class MybatisTestController {

    @Autowired
    MybatisTestMapper mybatisTestMapper;

    @Autowired
    SysUserMapper sysUserMapper;
    /**
     * 查询测试
     */
    @GetMapping("/select")
    public R<Void> sendSimpleMessage() {
        mybatisTestMapper.selectById(1L);
        return R.ok();
    }


    @GetMapping("/mapstruct")
    public R<Void> sendSimpleMessage1() {
        SysUserVo admin = sysUserMapper.selectUserByUserName("admin");
        SysUser convert = MapstructUtils.convert(admin, SysUser.class);
        return R.ok(Objects.requireNonNull(convert).toString());
    }

}
