package com.xie.controller;

import com.xie.common.core.domain.R;
import com.xie.mapper.MybatisTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者：xie
 * @时间：2023/7/2 19:59
 */
@RestController
@RequestMapping("/demo/mybatis")
public class MybatisTestController {

    @Autowired
    MybatisTestMapper mybatisTestMapper;
    /**
     * 查询测试
     */
    @GetMapping("/select")
    public R<Void> sendSimpleMessage() {
        mybatisTestMapper.selectById(1L);
        return R.ok();
    }

}
