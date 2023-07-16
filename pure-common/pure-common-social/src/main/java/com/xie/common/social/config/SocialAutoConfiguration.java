package com.xie.common.social.config;

import com.xie.common.social.config.properties.SocialProperties;
import com.xie.common.social.utils.AuthRedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Social 配置属性
 * @author thiszhc
 */
@AutoConfiguration
@EnableConfigurationProperties(SocialProperties.class)
public class SocialAutoConfiguration {

    @Bean
    public AuthStateCache authStateCache() {
        return new AuthRedisStateCache();
    }

}
