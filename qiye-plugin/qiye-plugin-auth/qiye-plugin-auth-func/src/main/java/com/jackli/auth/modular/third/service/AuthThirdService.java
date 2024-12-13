/*
 * Copyright [2022] [https://www.jcxxdd.com]
 *
 * Jackli采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Jackli源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.jcxxdd.com
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队767076381@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Jackli商业授权许可，请在官网购买授权，地址为 https://www.jcxxdd.com
 */
package com.jackli.auth.modular.third.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthCallback;
import com.jackli.auth.modular.third.entity.AuthThirdUser;
import com.jackli.auth.modular.third.param.AuthThirdCallbackParam;
import com.jackli.auth.modular.third.param.AuthThirdRenderParam;
import com.jackli.auth.modular.third.param.AuthThirdUserPageParam;
import com.jackli.auth.modular.third.result.AuthThirdRenderResult;

/**
 * 第三方登录Service接口
 *
 * @author lijinchang
 * @date 2022/7/8 16:20
 **/
public interface AuthThirdService extends IService<AuthThirdUser> {

    /**
     * 第三方登录页面渲染，返回授权结果
     *
     * @author lijinchang
     * @date 2022/7/8 16:37
     **/
    AuthThirdRenderResult render(AuthThirdRenderParam authThirdRenderParam);

    /**
     * 第三方登录授权回调，返回登录token
     *
     * @author lijinchang
     * @date 2022/7/8 16:42
     **/
    String callback(AuthThirdCallbackParam authThirdCallbackParam, AuthCallback authCallback);

    /**
     * 获取三方用户分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    Page<AuthThirdUser> page(AuthThirdUserPageParam authThirdUserPageParam);
}
