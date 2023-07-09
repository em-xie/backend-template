package com.xie.web.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xie.common.core.domain.R;
import com.xie.common.core.domain.model.LoginUser;
import com.xie.common.core.enums.LoginType;
import com.xie.common.core.exception.UserException;
import com.xie.common.satoken.utils.LoginHelper;
import com.xie.system.domain.bo.SysSocialBo;
import com.xie.system.domain.vo.SysUserVo;
import com.xie.system.service.ISysSocialService;
import com.xie.web.domain.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

/**
 * @作者：xie
 * @时间：2023/7/6 23:06
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {

    private final ISysSocialService sysSocialService;
    public void checkLogin(LoginType loginType, String username,  Supplier<Boolean> supplier) {
        int errorNumber = 0;
        if(supplier.get()){
            ++errorNumber;
            throw new UserException(loginType.getRetryLimitCount(),errorNumber);

        }
    }

    public LoginUser buildLoginUser(SysUserVo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setUserType(user.getUserType());
        return loginUser;
    }

    public R<LoginVo> sociaRegister(AuthUser authUserData) {
        SysSocialBo bo = new SysSocialBo();
        bo.setUserId(LoginHelper.getUserId());
        bo.setAuthId(authUserData.getSource() + authUserData.getUuid());
        bo.setOpenId(authUserData.getUuid());
        bo.setUserName(authUserData.getUsername());
        BeanUtils.copyProperties(authUserData, bo);
        BeanUtils.copyProperties(authUserData.getToken(), bo);
        sysSocialService.insertByBo(bo);
        return R.ok();

    }
}
