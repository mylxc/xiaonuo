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
package com.jackli.mobile.modular.resource.provider;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import com.jackli.mobile.modular.resource.entity.MobileButton;
import com.jackli.mobile.modular.resource.service.MobileButtonService;
import com.jackli.mobile.vip.MobileButtonApi;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 移动端按钮API接口提供者
 *
 * @author lijinchang
 * @date 2023/1/31 10:12
 **/
@Service
public class MobileButtonApiProvider implements MobileButtonApi {

    @Resource
    private MobileButtonService mobileButtonService;

    @Override
    public List<String> listByIds(List<String> buttonIdList) {
        if (ObjectUtil.isEmpty(buttonIdList)) {
            return null;
        }
        return mobileButtonService.listByIds(buttonIdList).stream().map(MobileButton::getCode).collect(Collectors.toList());
    }
}
