package com.xie.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.xie.common.core.domain.R;
import com.xie.common.core.domain.model.LoginBody;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @作者：xie
 * @时间：2023/7/8 20:11
 */
@SaIgnore
@RestController
@RequestMapping("/demo/social")
public class SocialDemoController {
    /**
     * Github认证令牌服务器地址
     */
    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    /**
     * Github认证服务器地址
     */
    private static final String AUTHORIZE_URL = "https://github.com/login/oauth/authorize";

    /**
     * Github资源服务器地址
     */
    private static final String RESOURCE_URL = "https://api.github.com/user";

    /**
     * 前端获取认证的URL，由后端拼接好返回前端进行请求
     */
    @GetMapping("/githubLogin")
    public R<LoginBody> githubLogin() throws IOException {

        Request request = new Request.Builder()
                .url(ACCESS_TOKEN_URL)
                .build();

        System.out.println(request);
        return R.ok(request.toString());

    }

}
