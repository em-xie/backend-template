package com.xie.web.service;


import cn.dev33.satoken.secure.BCrypt;
import com.xie.common.core.constant.Constants;
import com.xie.common.core.domain.model.RegisterBody;
import com.xie.common.core.enums.UserType;
import com.xie.common.core.exception.UserException;
import com.xie.common.core.utils.MessageUtils;
import com.xie.system.domain.bo.SysUserBo;
import com.xie.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRegisterService {

    private final ISysUserService userService;


    public void register(RegisterBody registerBody) {
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();
        String password = registerBody.getPassword();
        String username = registerBody.getUsername();
        SysUserBo sysUser = new SysUserBo();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);
        if (!userService.checkUserNameUnique(sysUser)) {
            throw new UserException("user.register.save.error", username);
        }
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
//        recordFootslogging(username, Constants.REGISTER, MessageUtils.message("user.register.success"));

    }

//    private void recordFootslogging(String username, String register, String message) {
//
//    }
}
