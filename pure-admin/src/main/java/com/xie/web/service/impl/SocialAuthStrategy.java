package com.xie.web.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.common.core.constant.UserStatus;
import com.xie.common.core.domain.model.LoginBody;
import com.xie.common.core.domain.model.LoginUser;
import com.xie.common.core.exception.ServiceException;
import com.xie.common.core.exception.UserException;
import com.xie.common.core.utils.ValidatorUtils;
import com.xie.common.core.validate.auth.SocialGroup;
import com.xie.common.satoken.utils.LoginHelper;
import com.xie.common.social.config.properties.SocialProperties;
import com.xie.common.social.utils.SocialUtils;
import com.xie.system.domain.SysClient;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.vo.SysSocialVo;
import com.xie.system.domain.vo.SysUserVo;
import com.xie.system.mapper.SysUserMapper;
import com.xie.system.service.ISysSocialService;
import com.xie.web.domain.LoginVo;
import com.xie.web.service.IAuthStrategy;
import com.xie.web.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/7/8 15:14
 */

@Slf4j
@Service("social" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SocialAuthStrategy implements IAuthStrategy{

    private final SocialProperties socialProperties;
    private final ISysSocialService sysSocialService;
    private final SysUserMapper sysUserMapper;
    private final SysLoginService sysLoginService;
    @Override
    public void validate(LoginBody loginBody) {
        ValidatorUtils.validate(loginBody, SocialGroup.class);
    }

    @Override
    public LoginVo login(String clientId, LoginBody loginBody, SysClient client) {
        AuthResponse<AuthUser> authUserAuthResponse = SocialUtils.loginAuth(loginBody, socialProperties);
        if(!authUserAuthResponse.ok()){
            throw new ServiceException(authUserAuthResponse.getMsg());
        }
        AuthUser data = authUserAuthResponse.getData();
        if ("GITEE".equals(data.getSource())) {
            // 如用户使用 gitee 登录顺手 star 给作者一点支持 拒绝白嫖
            HttpUtil.createRequest(Method.PUT, "https://gitee.com/api/v5/user/starred/dromara/RuoYi-Vue-Plus")
                    .formStr(MapUtil.of("access_token", data.getToken().getAccessToken()))
                    .executeAsync();
            HttpUtil.createRequest(Method.PUT, "https://gitee.com/api/v5/user/starred/dromara/RuoYi-Cloud-Plus")
                    .formStr(MapUtil.of("access_token", data.getToken().getAccessToken()))
                    .executeAsync();
        }
        SysSocialVo sysSocialVo = sysSocialService.selectByAuthId(data.getSource() + data.getUuid());
        if(!ObjectUtil.isNotNull(sysSocialVo)){
            throw new ServiceException("你还没有绑定第三方账号，绑定后才可以登录！");
        }

        // 查找用户
        SysUserVo user = loadUser(sysSocialVo.getUserId());
        LoginUser loginUser = sysLoginService.buildLoginUser(user);
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        // 生成token
        LoginHelper.login(loginUser, model);
        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        return loginVo;

    }

    private SysUserVo loadUser(Long userId) {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                        .select(SysUser::getStatus,SysUser::getUserName)
                        .eq(SysUser::getUserId, userId));
        if (ObjectUtil.isNull(sysUser)) {
            log.info("登录用户：{} 不存在.", "");
            throw new UserException("user.not.exists", "");
        } else if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            log.info("登录用户：{} 已被停用.", "");
            throw new UserException("user.blocked", "");
        }

        return sysUserMapper.selectUserByUserName(sysUser.getUserName());
    }
}
