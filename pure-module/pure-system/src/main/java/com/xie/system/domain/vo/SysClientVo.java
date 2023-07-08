package com.xie.system.domain.vo;

import com.xie.system.domain.SysClient;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @作者：xie
 * @时间：2023/7/6 19:51
 */
@Data
@AutoMapper(target = SysClient.class)
public class SysClientVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */

    private Long id;

    /**
     * 客户端id
     */

    private String clientId;

    /**
     * 客户端key
     */

    private String clientKey;

    /**
     * 客户端秘钥
     */

    private String clientSecret;

    /**
     * 授权类型
     */

    private List<String> grantTypeList;

    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * token活跃超时时间
     */

    private Long activeTimeout;

    /**
     * token固定超时时间
     */

    private Long timeout;

    /**
     * 状态（0正常 1停用）
     */

    private String status;

}
