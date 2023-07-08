package com.xie.web.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xie.common.core.constant.UserStatus;
import com.xie.common.core.domain.model.LoginBody;
import com.xie.common.core.domain.model.LoginUser;
import com.xie.common.core.enums.LoginType;
import com.xie.common.core.exception.UserException;
import com.xie.common.core.utils.ValidatorUtils;
import com.xie.common.core.validate.auth.PasswordGroup;
import com.xie.common.satoken.utils.LoginHelper;
import com.xie.system.domain.SysClient;
import com.xie.system.domain.SysUser;
import com.xie.system.domain.vo.SysUserVo;
import com.xie.system.mapper.SysUserMapper;
import com.xie.web.domain.LoginVo;
import com.xie.web.service.IAuthStrategy;
import com.xie.web.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/7/6 22:20
 */
@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordAuthStrategy implements IAuthStrategy{

    private final SysUserMapper userMapper;
    private final SysLoginService loginService;

    @Override
    public void validate(LoginBody loginBody) {
        ValidatorUtils.validate(loginBody, PasswordGroup.class);
    }

    @Override
    public LoginVo login(String clientId, LoginBody loginBody, SysClient client) {
        String password = loginBody.getPassword();
        String username = loginBody.getUsername();

        SysUserVo  user = loadByUserName(username);
        loginService.checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = loginService.buildLoginUser(user);
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
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        return loginVo;

    }

    private SysUserVo loadByUserName(String username) {
        SysUser sysUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserName,SysUser::getStatus)
                .eq(SysUser::getUserName,username));
        if (ObjectUtil.isNull(sysUser)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UserException("user.not.exists", username);
        } else if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UserException("user.blocked", username);
        }
        return userMapper.selectUserByUserName(username);
    }
}
