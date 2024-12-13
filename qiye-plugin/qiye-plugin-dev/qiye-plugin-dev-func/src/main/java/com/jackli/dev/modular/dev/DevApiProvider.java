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
package com.jackli.dev.modular.dev;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jackli.dev.api.DevApi;
import com.jackli.dev.modular.config.entity.DevConfig;
import com.jackli.dev.modular.config.enums.DevConfigCategoryEnum;
import com.jackli.dev.modular.config.service.DevConfigService;
import com.jackli.dev.modular.dict.entity.DevDict;
import com.jackli.dev.modular.dict.enums.DevDictCategoryEnum;
import com.jackli.dev.modular.dict.service.DevDictService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 开发工具模块综合API接口实现类
 *
 * @author lijinchang
 * @date 2022/9/26 14:30
 **/
@Service
public class DevApiProvider implements DevApi {

    @Resource
    private DevDictService devDictService;

    @Resource
    private DevConfigService devConfigService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initTenDataForCategoryId(String tenId, String tenName) {

        // ==============给租户初始化配置=================
        List<DevConfig> devConfigList = devConfigService.list(new LambdaQueryWrapper<DevConfig>().ne(DevConfig::getCategory,
                DevConfigCategoryEnum.BIZ_DEFINE.getValue())).stream().peek(devConfig -> {
            devConfig.setId(null);
            devConfig.setTenantId(tenId);
            // 非系统基础的设置为“未配置”
            if(!devConfig.getCategory().equals(DevConfigCategoryEnum.SYS_BASE.getValue())) {
                devConfig.setConfigValue("未配置");
            }
            // 将系统名称设置为租户名称
            if("SNOWY_SYS_NAME".equals(devConfig.getConfigKey())) {
                devConfig.setConfigValue(tenName);
            }
            // 将系统默认工作台数据设置为空
            if("SNOWY_SYS_DEFAULT_WORKBENCH_DATA".equals(devConfig.getConfigKey())) {
                devConfig.setConfigValue("{\"shortcut\":[]}");
            }
        }).collect(Collectors.toList());
        // 批量保存配置
        devConfigService.saveBatch(devConfigList);

        // ==============给租户初始化字典=================
        List<DevDict> devDictList = devDictService.list(new LambdaQueryWrapper<DevDict>().eq(DevDict::getCategory,
                DevDictCategoryEnum.FRM.getValue()));
        List<TreeNode<String>> treeNodeList = devDictList.stream().map(devDict ->
                new TreeNode<>(devDict.getId(), devDict.getParentId(),
                        devDict.getDictLabel(), devDict.getSortCode()).setExtra(JSONUtil.parseObj(devDict)))
                .collect(Collectors.toList());
        List<Tree<String>> treeList = TreeUtil.build(treeNodeList, "0");
        // 批量保存字典
        execRecursionInsertDict("0", treeList, tenId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeTenDataForCategoryId(String tenId) {
        // ==============移除租户初始化的配置=================
        devConfigService.remove(new LambdaQueryWrapper<DevConfig>().eq(DevConfig::getTenantId, tenId));

        // ==============移除租户初始化的字典=================
        devDictService.remove(new LambdaQueryWrapper<DevDict>().eq(DevDict::getTenantId, tenId));
    }

    /**
     * 递归插入字典
     *
     * @author lijinchang
     * @date 2022/9/26 15:33
     **/
    private void execRecursionInsertDict(String parentId, List<Tree<String>> treeList, String tenId) {
        treeList.forEach(tree -> {
            DevDict devDict = JSONUtil.toBean(JSONUtil.parseObj(tree), DevDict.class);
            devDict.setId(null);
            devDict.setParentId(parentId);
            devDict.setTenantId(tenId);
            devDictService.save(devDict);
            String dictId = devDict.getId();
            List<Tree<String>> children = tree.getChildren();
            if(ObjectUtil.isNotEmpty(children)) {
                execRecursionInsertDict(dictId, children, tenId);
            }
        });
    }
}
