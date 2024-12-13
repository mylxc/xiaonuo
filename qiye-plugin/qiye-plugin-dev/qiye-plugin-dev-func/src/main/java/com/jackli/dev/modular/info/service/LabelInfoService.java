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
package com.jackli.dev.modular.info.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackli.dev.modular.info.entity.LabelInfo;
import com.jackli.dev.modular.info.param.LabelInfoAddParam;
import com.jackli.dev.modular.info.param.LabelInfoEditParam;
import com.jackli.dev.modular.info.param.LabelInfoIdParam;
import com.jackli.dev.modular.info.param.LabelInfoPageParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 标签信息Service接口
 *
 * @author 李金昌
 * @date  2023/08/17 13:43
 **/
public interface LabelInfoService extends IService<LabelInfo> {

    /**
     * 获取标签信息分页
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    Page<LabelInfo> page(LabelInfoPageParam labelInfoPageParam);

    /**
     * 获取标签信息列表
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    List<LabelInfo> getList(LabelInfoPageParam labelInfoPageParam);
    /**
     * 添加标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    void add(LabelInfoAddParam labelInfoAddParam);

    /**
     * 编辑标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    void edit(LabelInfoEditParam labelInfoEditParam);

    /**
     * 删除标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    void delete(List<LabelInfoIdParam> labelInfoIdParamList);

    /**
     * 获取标签信息详情
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    LabelInfo detail(LabelInfoIdParam labelInfoIdParam);

    /**
     * 获取标签信息详情
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     **/
    LabelInfo queryEntity(Long id);

    /**
     * 读导入的excel模版计算数据
     *
     * @author tianhengmiao
     * @date  2024/01/03 10:29
     */
    void calculate(MultipartFile file) throws IOException;
}
