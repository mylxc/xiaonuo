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
package com.jackli.sys.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 职位API
 *
 * @author lijinchang
 * @date 2022/6/6 11:35
 **/
public interface SysPositionApi {

    /**
     * 根据id获取名称
     *
     * @author lijinchang
     * @date 2022/8/4 10:13
     **/
    String getNameById(String positionId);

    /**
     * 获取职位选择器
     *
     * @author lijinchang
     * @date 2022/7/22 14:47
     **/
    List<JSONObject> positionSelector(String orgId, String searchKey);
}
