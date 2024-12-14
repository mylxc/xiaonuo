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
package com.jackli.auth.modular.monitor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jackli.auth.modular.monitor.param.AuthExitSessionParam;
import com.jackli.auth.modular.monitor.param.AuthExitTokenParam;
import com.jackli.auth.modular.monitor.param.AuthSessionPageParam;
import com.jackli.auth.modular.monitor.result.AuthSessionAnalysisResult;
import com.jackli.auth.modular.monitor.result.AuthSessionPageResult;

import java.util.List;

/**
 * 会话治理Service接口
 *
 * @author lijinchang
 * @date 2022/6/24 15:46S
 **/
public interface AuthSessionService {

    /**
     * 会话统计
     *
     * @author lijinchang
     * @date 2022/7/19 9:33
     **/
    AuthSessionAnalysisResult analysis();

    /**
     * 查询B端会话
     *
     * @author lijinchang
     * @date 2022/6/24 22:30
     */
    Page<AuthSessionPageResult> pageForB(AuthSessionPageParam authSessionPageParam);

    /**
     * 查询C端会话
     *
     * @author lijinchang
     * @date 2022/6/24 22:30
     */
    Page<AuthSessionPageResult> pageForC(AuthSessionPageParam authSessionPageParam);

    /**
     * 强退B端会话
     *
     * @author lijinchang
     * @date 2022/6/29 21:47
     */
    void exitSessionForB(List<AuthExitSessionParam> authExitSessionParamList);

    /**
     * 强退C端会话
     *
     * @author lijinchang
     * @date 2022/6/29 21:47
     */
    void exitSessionForC(List<AuthExitSessionParam> authExitSessionParamList);

    /**
     * 强退B端token
     *
     * @author lijinchang
     * @date 2022/6/29 21:47
     */
    void exitTokenForB(List<AuthExitTokenParam> authExitTokenParamList);

    /**
     * 强退C端token
     *
     * @author lijinchang
     * @date 2022/6/29 21:47
     */
    void exitTokenForC(List<AuthExitTokenParam> authExitTokenParamList);
}
