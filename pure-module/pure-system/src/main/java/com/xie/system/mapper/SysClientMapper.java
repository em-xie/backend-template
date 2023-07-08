package com.xie.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.common.mybatis.core.mapper.BaseMapperPlus;
import com.xie.system.domain.SysClient;
import com.xie.system.domain.vo.SysClientVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @作者：xie
 * @时间：2023/7/6 19:53
 */

public interface SysClientMapper extends BaseMapperPlus<SysClient,SysClientVo> {
}
