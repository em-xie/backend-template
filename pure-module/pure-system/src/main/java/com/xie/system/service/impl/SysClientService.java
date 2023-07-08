package com.xie.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.system.domain.SysClient;
import com.xie.system.domain.vo.SysClientVo;
import com.xie.system.mapper.SysClientMapper;
import com.xie.system.service.ISysClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者：xie
 * @时间：2023/7/6 19:52
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysClientService implements ISysClientService {

    @Autowired
    SysClientMapper sysClientMapper;
    @Override
    public SysClientVo queryById(Long id) {
        SysClientVo sysClient = sysClientMapper.selectVoById(id);
        sysClient.setGrantTypeList(List.of(sysClient.getGrantType().split(",")));
        return sysClient;

    }

    /**
     * 查询客户端管理
     */
    @Override
    public SysClient queryByClientId(String clientId) {
        return sysClientMapper.selectOne(
                new LambdaQueryWrapper<SysClient>()
                        .eq(SysClient::getClientId, clientId)
        );
    }
}
