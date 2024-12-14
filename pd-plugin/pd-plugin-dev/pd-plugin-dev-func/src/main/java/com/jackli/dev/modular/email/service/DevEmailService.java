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
package com.jackli.dev.modular.email.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackli.dev.modular.email.entity.DevEmail;
import com.jackli.dev.modular.email.param.*;

import java.util.List;

/**
 * 邮件Service接口
 *
 * @author lijinchang
 * @date 2022/2/23 18:27
 **/
public interface DevEmailService extends IService<DevEmail> {

    /**
     * 发送邮件——本地TXT
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendLocal(DevEmailSendLocalTxtParam devEmailSendLocalTxtParam);

    /**
     * 发送邮件——本地HTML
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendLocal(DevEmailSendLocalHtmlParam devEmailSendLocalHtmlParam);

    /**
     * 发送邮件——阿里云TXT
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunTxtParam devEmailSendAliyunTxtParam);

    /**
     * 发送邮件——阿里云HTML
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunHtmlParam devEmailSendAliyunHtmlParam);

    /**
     * 发送邮件——阿里云TMP
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunTmpParam devEmailSendAliyunTmpParam);

    /**
     * 发送邮件——腾讯云TXT
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentTxtParam devEmailSendTencentTxtParam);

    /**
     * 发送邮件——腾讯云HTML
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentHtmlParam devEmailSendTencentHtmlParam);

    /**
     * 发送邮件——腾讯云TMP
     *
     * @author lijinchang
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentTmpParam devEmailSendTencentTmpParam);

    /**
     * 获取邮件分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    Page<DevEmail> page(DevEmailPageParam devEmailPageParam);

    /**
     * 删除邮件
     *
     * @author lijinchang
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevEmailIdParam> devEmailIdParamList);

    /**
     * 获取邮件详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    DevEmail detail(DevEmailIdParam devEmailIdParam);

    /**
     * 获取邮件详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    DevEmail queryEntity(String id);
}
