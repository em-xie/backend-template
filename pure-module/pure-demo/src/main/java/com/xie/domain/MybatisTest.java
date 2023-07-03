package com.xie.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @作者：xie
 * @时间：2023/7/2 19:55
 */
@Data
@TableName("test_demo")
public class MybatisTest {
    private Long userId;
    private Long id;
}
