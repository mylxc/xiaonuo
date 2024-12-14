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
package com.jackli.common.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.jackli.common.exception.CommonException;

/**
 * 通用邮件工具类
 *
 * @author lijinchang
 * @date 2022/8/25 15:10
 **/
public class CommonEmailUtil {

    /**
     * 判断是否邮箱
     *
     * @author lijinchang
     * @date 2022/8/15 13:32
     **/
    public static boolean isEmail(String email) {
        return ReUtil.isMatch("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email);
    }

    /**
     * 校验邮箱格式
     *
     * @author lijinchang
     * @date 2022/8/15 13:32
     **/
    public static void validEmail(String emails) {
        StrUtil.split(emails, StrUtil.COMMA).forEach(email -> {
            if(!ReUtil.isMatch("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", email)) {
                throw new CommonException("邮件地址：{}格式错误", email);
            }
        });
    }
}
