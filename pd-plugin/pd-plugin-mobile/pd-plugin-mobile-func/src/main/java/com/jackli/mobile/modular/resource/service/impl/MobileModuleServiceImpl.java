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
package com.jackli.mobile.modular.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.jackli.common.enums.CommonSortOrderEnum;
import com.jackli.common.exception.CommonException;
import com.jackli.common.page.CommonPageRequest;
import com.jackli.mobile.modular.resource.entity.MobileMenu;
import com.jackli.mobile.modular.resource.entity.MobileModule;
import com.jackli.mobile.modular.resource.enums.MobileResourceCategoryEnum;
import com.jackli.mobile.modular.resource.mapper.MobileModuleMapper;
import com.jackli.mobile.modular.resource.param.module.MobileModuleAddParam;
import com.jackli.mobile.modular.resource.param.module.MobileModuleEditParam;
import com.jackli.mobile.modular.resource.param.module.MobileModuleIdParam;
import com.jackli.mobile.modular.resource.param.module.MobileModulePageParam;
import com.jackli.mobile.modular.resource.service.MobileMenuService;
import com.jackli.mobile.modular.resource.service.MobileModuleService;
import com.jackli.sys.api.SysRelationApi;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模块Service接口实现类
 *
 * @author lijinchang
 * @date 2022/6/27 14:25
 **/
@Service
public class MobileModuleServiceImpl extends ServiceImpl<MobileModuleMapper, MobileModule> implements MobileModuleService {

    @Resource
    private MobileMenuService mobileMenuService;

    @Resource
    private SysRelationApi sysRelationApi;

    @Override
    public Page<MobileModule> page(MobileModulePageParam mobileModulePageParam) {
        QueryWrapper<MobileModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MobileModule::getCategory, MobileResourceCategoryEnum.MODULE.getValue());
        if(ObjectUtil.isNotEmpty(mobileModulePageParam.getSearchKey())) {
            queryWrapper.lambda().like(MobileModule::getTitle, mobileModulePageParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(mobileModulePageParam.getSortField(), mobileModulePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(mobileModulePageParam.getSortOrder());
            queryWrapper.orderBy(true, mobileModulePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(mobileModulePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(MobileModule::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void add(MobileModuleAddParam mobileModuleAddParam) {
        MobileModule mobileModule = BeanUtil.toBean(mobileModuleAddParam, MobileModule.class);
        boolean repeatTitle = this.count(new LambdaQueryWrapper<MobileModule>().eq(MobileModule::getCategory,
                MobileResourceCategoryEnum.MODULE.getValue()).eq(MobileModule::getTitle, mobileModule.getTitle())) > 0;
        if(repeatTitle) {
            throw new CommonException("存在重复的模块，名称为：{}", mobileModule.getTitle());
        }
        mobileModule.setCode(RandomUtil.randomString(10));
        mobileModule.setCategory(MobileResourceCategoryEnum.MODULE.getValue());
        this.save(mobileModule);
    }

    @Override
    public void edit(MobileModuleEditParam mobileModuleEditParam) {
        MobileModule mobileModule = this.queryEntity(mobileModuleEditParam.getId());
        BeanUtil.copyProperties(mobileModuleEditParam, mobileModule);
        boolean repeatTitle = this.count(new LambdaQueryWrapper<MobileModule>().eq(MobileModule::getCategory,
                MobileResourceCategoryEnum.MODULE.getValue()).eq(MobileModule::getTitle, mobileModule.getTitle())
                .ne(MobileModule::getId, mobileModule.getId())) > 0;
        if(repeatTitle) {
            throw new CommonException("存在重复的模块，名称为：{}", mobileModule.getTitle());
        }
        this.updateById(mobileModule);
    }

    @Override
    public void delete(List<MobileModuleIdParam> mobileModuleIdParamList) {
        List<String> mobileModuleIdList = CollStreamUtil.toList(mobileModuleIdParamList, MobileModuleIdParam::getId);
        if(ObjectUtil.isNotEmpty(mobileModuleIdList)) {
            // 获取模块下的菜单
            List<MobileMenu> allMenuList = mobileMenuService.list(new LambdaQueryWrapper<MobileMenu>()
                    .in(MobileMenu::getCategory, CollectionUtil.newArrayList(MobileResourceCategoryEnum.MENU.getValue())));
            if(ObjectUtil.isNotEmpty(allMenuList)) {
                List<String> toDeleteMenuIdList = CollectionUtil.newArrayList(mobileModuleIdList);
                allMenuList.stream().filter(mobileMenu -> mobileModuleIdList.contains(mobileMenu.getModule()))
                        .collect(Collectors.toList()).forEach(mobileMenu -> toDeleteMenuIdList
                        .addAll(mobileMenuService.getChildListById(allMenuList, mobileMenu.getId(), true).stream()
                                .map(MobileMenu::getId).collect(Collectors.toList())));
                if(ObjectUtil.isNotEmpty(toDeleteMenuIdList)) {
                    // 清除对应的角色与移动端资源信息
                    sysRelationApi.removeRoleHasMobileMenuRelation(toDeleteMenuIdList);
                    // 执行删除
                    this.removeBatchByIds(toDeleteMenuIdList);
                }
            }
        }
    }

    @Override
    public MobileModule detail(MobileModuleIdParam mobileModuleIdParam) {
        return this.queryEntity(mobileModuleIdParam.getId());
    }

    @Override
    public MobileModule queryEntity(String id) {
        MobileModule mobileModule = this.getById(id);
        if(ObjectUtil.isEmpty(mobileModule)) {
            throw new CommonException("模块不存在，id值为：{}", id);
        }
        return mobileModule;
    }
}
