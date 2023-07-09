package com.xie.system.service;

import com.xie.system.domain.bo.SysSocialBo;
import com.xie.system.domain.vo.SysSocialVo;

/**
 * @作者：xie
 * @时间：2023/7/8 18:35
 */
public interface ISysSocialService {

    SysSocialVo selectByAuthId(String authId);

    Boolean insertByBo(SysSocialBo bo);
}
