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
package com.jackli.ten.modular.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackli.ten.modular.entity.TenStorage;
import com.jackli.ten.modular.param.*;
import com.jackli.ten.modular.result.TenDbsSelectorResult;

import java.util.List;

/**
 * 多租户Service接口
 *
 * @author lijinchang
 * @date 2022/3/11 10:25
 **/
public interface TenService extends IService<TenStorage> {

    /**
     * 获取当前租户域名
     *
     * @author lijinchang
     * @date 2022/4/22 14:52
     **/
    String getCurrentTenDomain();

    /**
     * 获取当前租户
     *
     * @author lijinchang
     * @date 2022/7/18 18:31
     **/
    TenStorage getCurrentTen();

    /**
     * 获取租户分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    Page<TenStorage> page(TenStoragePageParam tenStoragePageParam);
    /**
     * 租户选择器
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    List<TenStorage> tenSelector(TenStorageSelectorParam tenStorageSelectorParam);
    /**
     * 添加租户
     *
     * @author lijinchang
     * @date 2022/4/24 20:48
     */
    void add(TenStorageAddParam tenStorageAddParam);

    /**
     * 编辑租户
     *
     * @author lijinchang
     * @date 2022/4/24 21:13
     */
    void edit(TenStorageEditParam tenStorageEditParam);

    /**
     * 删除租户
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    void delete(List<TenStorageIdParam> tenStorageIdParamList);

    /**
     * 获取租户详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    TenStorage detail(TenStorageIdParam tenStorageIdParam);

    /**
     * 获取租户详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    TenStorage queryEntity(String id);

    /* ====租户部分所需要用到的选择器==== */

    /**
     * 获取租户未被使用数据源列表
     *
     * @author yubaoshan
     * @date 2022/7/11 17:53
     */
    List<TenDbsSelectorResult> dbsList();
}
