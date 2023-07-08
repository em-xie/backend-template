package com.xie.web.service;

import com.xie.common.core.domain.model.LoginBody;
import com.xie.common.core.exception.ServiceException;
import com.xie.common.core.utils.SpringUtils;
import com.xie.system.domain.SysClient;
import com.xie.web.domain.LoginVo;

/**
 * @作者：xie
 * @时间：2023/7/6 21:43
 */
public interface IAuthStrategy {
    String BASE_NAME = "AuthStrategy";


    static LoginVo login(LoginBody loginBody, SysClient sysClient) {
        // 授权类型和客户端id
        String clientId = loginBody.getClientId();
        String grantType = loginBody.getGrantType();
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        IAuthStrategy instance = SpringUtils.getBean(beanName);
        instance.validate(loginBody);
        return instance.login(clientId, loginBody, sysClient);
    }

    /**
     * 参数校验
     */
    void validate(LoginBody loginBody);

    /**
     * 登录
     */
    LoginVo login(String clientId, LoginBody loginBody, SysClient client);



}
