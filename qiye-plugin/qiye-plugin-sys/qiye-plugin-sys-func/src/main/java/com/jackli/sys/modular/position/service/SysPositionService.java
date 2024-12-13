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
package com.jackli.sys.modular.position.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackli.sys.modular.position.entity.SysPosition;
import com.jackli.sys.modular.position.param.*;

import java.util.List;

/**
 * 职位Service接口
 *
 * @author lijinchang
 * @date 2022/4/21 18:35
 **/
public interface SysPositionService extends IService<SysPosition> {

    /**
     * 获取职位分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    Page<SysPosition> page(SysPositionPageParam sysPositionPageParam);

    /**
     * 添加职位
     *
     * @author lijinchang
     * @date 2022/4/24 20:48
     */
    void add(SysPositionAddParam sysPositionAddParam);

    /**
     * 编辑职位
     *
     * @author lijinchang
     * @date 2022/4/24 21:13
     */
    void edit(SysPositionEditParam sysPositionEditParam);

    /**
     * 删除职位
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    void delete(List<SysPositionIdParam> sysPositionIdParamList);

    /**
     * 获取职位详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    SysPosition detail(SysPositionIdParam sysPositionIdParam);

    /**
     * 获取职位详情
     *
     * @author lijinchang
     * @date 2022/7/25 19:42
     **/
    SysPosition queryEntity(String id);

    /**
     * 根据id获取数据
     *
     * @author lijinchang
     * @date 2022/8/15 14:55
     **/
    SysPosition getById(List<SysPosition> originDataList, String id) ;

    /* ====职位部分所需要用到的选择器==== */

    /**
     * 获取组织树选择器
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    List<Tree<String>> orgTreeSelector();

    /**
     * 获取职位选择器
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    List<SysPosition> positionSelector(SysPositionSelectorPositionParam sysPositionSelectorPositionParam);
}
