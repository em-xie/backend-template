package com.xie.web.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xie.common.core.domain.model.LoginUser;
import com.xie.common.core.enums.LoginType;
import com.xie.common.core.exception.UserException;
import com.xie.system.domain.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
