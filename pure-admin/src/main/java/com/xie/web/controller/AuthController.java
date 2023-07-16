package com.xie.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.xie.common.core.domain.R;
import com.xie.common.core.domain.model.LoginBody;
import com.xie.common.core.utils.StringUtils;
import com.xie.common.social.config.properties.SocialLoginConfigProperties;
import com.xie.common.social.config.properties.SocialProperties;
import com.xie.common.social.utils.SocialUtils;
import com.xie.system.domain.SysClient;
import com.xie.system.service.impl.SysClientService;
import com.xie.web.domain.LoginVo;
import com.xie.web.service.IAuthStrategy;
import com.xie.web.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final SocialProperties socialProperties;
    private final SysLoginService loginService;
    @PostMapping("/login")
    public R<LoginVo> Login(@Validated @RequestBody LoginBody loginBody){
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

    @GetMapping("/binding/{source}")
    public R<String> authBinding(@PathVariable("source") String source) {
        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
        if (ObjectUtil.isNull(obj)) {
            return R.fail(source + "平台账号暂不支持");
        }
        AuthRequest authRequest = SocialUtils.getAuthRequest(source, socialProperties);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return R.ok("操作成功", authorizeUrl);
    }

    @PostMapping("/social/callback")
    public R<LoginVo> socialCallback(@RequestBody LoginBody loginBody) {
        // 获取第三方登录信息
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(loginBody, socialProperties);
        AuthUser authUserData = response.getData();
        // 判断授权响应是否成功
        if (!response.ok()) {
            return R.fail(response.getMsg());
        }
        return loginService.sociaRegister(authUserData);
    }



}
