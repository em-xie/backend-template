package com.xie.system.service;

import com.xie.system.domain.SysClient;
import com.xie.system.domain.vo.SysClientVo;

/**
 * @作者：xie
 * @时间：2023/7/6 19:50
 */


public interface ISysClientService {
    /**
     * 查询客户端管理
     */
    SysClientVo queryById(Long id);

    SysClient queryByClientId(String clientId);
}
