package com.xie.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.xie.common.core.domain.R;
import com.xie.common.core.domain.model.LoginBody;
import com.xie.common.core.utils.StringUtils;
import com.xie.system.domain.SysClient;
import com.xie.system.service.impl.SysClientService;
import com.xie.web.domain.LoginVo;
import com.xie.web.service.IAuthStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者：xie
 * @时间：2023/7/6 15:38
 */

@Slf4j
@SaIgnore
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SysClientService sysClientService;

    @PostMapping("/login")
    public R<LoginVo> Login(@Validated @RequestBody LoginBody loginBody){
        System.out.println(loginBody);
        String clientId = loginBody.getClientId();
        String grantType = loginBody.getGrantType();
        SysClient sysClient = sysClientService.queryByClientId(clientId);
        if(ObjectUtil.isNull(sysClient) || !StringUtils.contains(sysClient.getGrantType(),grantType)){
            log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
            return R.fail("auth.grant.type.error");
        }
        // 登录
        return R.ok(IAuthStrategy.login(loginBody, sysClient));
    }

}
