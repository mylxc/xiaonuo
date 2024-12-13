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
package com.jackli.ten.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jackli.common.cache.CommonCacheOperator;
import com.jackli.common.enums.CommonSortOrderEnum;
import com.jackli.common.exception.CommonException;
import com.jackli.common.page.CommonPageRequest;
import com.jackli.common.prop.CommonProperties;
import com.jackli.common.util.CommonServletUtil;
import com.jackli.dbs.api.DbsApi;
import com.jackli.dev.api.DevApi;
import com.jackli.sys.api.SysApi;
import com.jackli.ten.core.prop.TenProperties;
import com.jackli.ten.modular.entity.TenStorage;
import com.jackli.ten.modular.enums.TenCategoryEnum;
import com.jackli.ten.modular.mapper.TenMapper;
import com.jackli.ten.modular.param.*;
import com.jackli.ten.modular.result.TenDbsSelectorResult;
import com.jackli.ten.modular.service.TenService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 多租户Service接口实现类
 *
 * @author lijinchang
 * @date 2022/3/11 10:25
 **/
@Service
public class TenServiceImpl extends ServiceImpl<TenMapper, TenStorage> implements TenService {

    private static final String TEN_CACHE_KEY = "ext-tenant:";

    @Resource
    private CommonProperties commonProperties;

    @Resource
    private TenProperties tenProperties;

    @Resource
    private SysApi sysApi;

    @Resource
    private DevApi devApi;

    @Resource
    private DbsApi dbsApi;

    @Resource
    private CommonCacheOperator commonCacheOperator;

    @Override
    public String getCurrentTenDomain() {
        // 获取当前租户域名
        String domain = CommonServletUtil.getRequest().getHeader("Domain");
        if (ObjectUtil.isEmpty(domain)) {
            domain = CommonServletUtil.getRequest().getHeader("Origin");
        }

        // 先获取主租户域名
        String mainDomain = commonProperties.getFrontUrl();
        if(ObjectUtil.isEmpty(mainDomain)) {
            throw new CommonException("前端地址未正确配置：snowy.config.common.front-url为空");
        }

        // 获取后端域名
        String apiUrl = commonProperties.getBackendUrl();
        if(ObjectUtil.isEmpty(apiUrl)) {
            throw new CommonException("后端域名地址未正确配置：snowy.config.common.backend-url为空");
        }

        // 如果获取不到租户域名，则返回主租户域名
        if(ObjectUtil.isEmpty(domain)) {
            return mainDomain;
        } else {
            // 如果是后端域名则返回主租户域名
            if(domain.equals(apiUrl)) {
                return mainDomain;
            } else {
                return domain;
            }
        }
    }

    @Override
    public TenStorage getCurrentTen() {
        // 获取当前租户域名
        String domain = this.getCurrentTenDomain();

        // 从缓存中取
        Object cacheValue = commonCacheOperator.get(TEN_CACHE_KEY + domain);
        if(ObjectUtil.isNotEmpty(cacheValue)) {
            return BeanUtil.toBean(cacheValue, TenStorage.class);
        }

        TenStorage tenStorage;

        // 如果当前域名是主租户域名，则使用主租户
        if(domain.equals(commonProperties.getFrontUrl())) {
            TenStorage mainTen = new TenStorage();
            mainTen.setId(tenProperties.getDefaultTenId());
            tenStorage = mainTen;
        } else {
            // 否则根据域名获取租户
            tenStorage = this.getOne(new LambdaQueryWrapper<TenStorage>().eq(TenStorage::getDomain, domain));
            if(ObjectUtil.isEmpty(tenStorage)) {
                throw new CommonException("租户不存在，域名为：{}", domain);
            }
        }
        // 更新到缓存
        commonCacheOperator.put(TEN_CACHE_KEY + domain, tenStorage);
        return tenStorage;
    }

    @Override
    public Page<TenStorage> page(TenStoragePageParam tenStoragePageParam) {
        QueryWrapper<TenStorage> queryWrapper = new QueryWrapper<>();
        // 查询部分字段
        queryWrapper.lambda().select(TenStorage::getId, TenStorage::getDbsId, TenStorage::getDbsName,
                TenStorage::getName, TenStorage::getDomain, TenStorage::getCategory, TenStorage::getSortCode);
        if(ObjectUtil.isNotEmpty(tenStoragePageParam.getCategory())) {
            queryWrapper.lambda().eq(TenStorage::getCategory, tenStoragePageParam.getCategory());
        }
        if(ObjectUtil.isNotEmpty(tenStoragePageParam.getSearchKey())) {
            queryWrapper.lambda().like(TenStorage::getName, tenStoragePageParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(tenStoragePageParam.getSortField(), tenStoragePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(tenStoragePageParam.getSortOrder());
            queryWrapper.orderBy(true, tenStoragePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(tenStoragePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(TenStorage::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<TenStorage> tenSelector(TenStorageSelectorParam tenStorageSelectorParam) {
        QueryWrapper<TenStorage> queryWrapper = new QueryWrapper<>();
        // 查询部分字段
        queryWrapper.lambda().select(TenStorage::getId, TenStorage::getDbsId, TenStorage::getDbsName,
                TenStorage::getName, TenStorage::getDomain, TenStorage::getCategory, TenStorage::getSortCode);
        if(ObjectUtil.isNotEmpty(tenStorageSelectorParam.getCategory())) {
            queryWrapper.lambda().eq(TenStorage::getCategory, tenStorageSelectorParam.getCategory());
        }
        if(ObjectUtil.isNotEmpty(tenStorageSelectorParam.getSearchKey())) {
            queryWrapper.lambda().like(TenStorage::getName, tenStorageSelectorParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(tenStorageSelectorParam.getSortField(), tenStorageSelectorParam.getSortOrder())) {
            CommonSortOrderEnum.validate(tenStorageSelectorParam.getSortOrder());
            queryWrapper.orderBy(true, tenStorageSelectorParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(tenStorageSelectorParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(TenStorage::getSortCode);
        }
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(TenStorageAddParam tenStorageAddParam) {
        checkParam(tenStorageAddParam);
        TenStorage tenStorage = BeanUtil.toBean(tenStorageAddParam, TenStorage.class);
        tenStorage.setCode(RandomUtil.randomString(10));
        this.save(tenStorage);

        // 初始化ID类型的租户系统模块数据
        sysApi.initTenDataForCategoryId(tenStorage.getId(), tenStorage.getName());

        // 初始化ID类型的租户开发工具模块数据
        devApi.initTenDataForCategoryId(tenStorage.getId(), tenStorage.getName());
    }

    private void checkParam(TenStorageAddParam tenStorageAddParam) {
        if(!tenProperties.getEnabled()) {
            throw new CommonException("请先开启租户功能");
        }
        String tenCategory = tenStorageAddParam.getCategory();
        TenCategoryEnum.validate(tenCategory);
        if(TenCategoryEnum.DB.getValue().equals(tenCategory)) {
            String dbsId = tenStorageAddParam.getDbsId();
            String dbsName = tenStorageAddParam.getDbsName();
            if(ObjectUtil.isEmpty(dbsId)) {
                throw new CommonException("dbsId不能为空");
            }
            if(ObjectUtil.isEmpty(dbsName)) {
                throw new CommonException("dbsName不能为空");
            }
        } else {
            tenStorageAddParam.setDbsId(null);
            tenStorageAddParam.setDbsName(null);
        }

        boolean hasSameTen = this.count(new LambdaQueryWrapper<TenStorage>()
                .eq(TenStorage::getName, tenStorageAddParam.getName())) > 0;
        if(hasSameTen) {
            throw new CommonException("存在重复的租户，名称为：{}", tenStorageAddParam.getName());
        }

        boolean hasSameDomain = this.count(new LambdaQueryWrapper<TenStorage>()
                .eq(TenStorage::getDomain, tenStorageAddParam.getDomain())) > 0;
        if(hasSameDomain) {
            throw new CommonException("存在重复的绑定域名，域名为：{}", tenStorageAddParam.getDomain());
        }
    }

    @Override
    public void edit(TenStorageEditParam tenStorageEditParam) {
        if(!tenProperties.getEnabled()) {
            throw new CommonException("请先开启租户功能");
        }
        TenStorage tenStorage = this.queryEntity(tenStorageEditParam.getId());
        // 移除对应的缓存
        commonCacheOperator.remove(TEN_CACHE_KEY + tenStorage.getDomain());
        checkParam(tenStorageEditParam);
        BeanUtil.copyProperties(tenStorageEditParam, tenStorage);
        this.updateById(tenStorage);
    }

    private void checkParam(TenStorageEditParam tenStorageEditParam) {

        boolean hasSameTen = this.count(new LambdaQueryWrapper<TenStorage>()
                .eq(TenStorage::getName, tenStorageEditParam.getName())
                .ne(TenStorage::getId, tenStorageEditParam.getId())) > 0;
        if(hasSameTen) {
            throw new CommonException("存在重复的租户，名称为：{}", tenStorageEditParam.getName());
        }

        boolean hasSameDomain = this.count(new LambdaQueryWrapper<TenStorage>()
                .eq(TenStorage::getDomain, tenStorageEditParam.getDomain())
                .ne(TenStorage::getId, tenStorageEditParam.getId())) > 0;
        if(hasSameDomain) {
            throw new CommonException("存在重复的绑定域名，域名为：{}", tenStorageEditParam.getDomain());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<TenStorageIdParam> tenStorageIdParamList) {
        if(!tenProperties.getEnabled()) {
            throw new CommonException("请先开启租户功能");
        }
        List<String> tenStorageIdList = CollStreamUtil.toList(tenStorageIdParamList, TenStorageIdParam::getId);
        if(ObjectUtil.isNotEmpty(tenStorageIdList)) {
            this.listByIds(tenStorageIdList).forEach(tenStorage -> {
                // 移除对应的缓存
                commonCacheOperator.remove(TEN_CACHE_KEY + tenStorage.getDomain());

                // 删除ID类型的租户系统模块数据
                sysApi.removeTenDataForCategoryId(tenStorage.getId());

                // 删除ID类型的租户开发工具模块数据
                devApi.removeTenDataForCategoryId(tenStorage.getId());
            });
            // 执行删除
            this.removeBatchByIds(tenStorageIdList);
        }
    }

    @Override
    public TenStorage detail(TenStorageIdParam tenStorageIdParam) {
        return this.queryEntity(tenStorageIdParam.getId());
    }

    @Override
    public TenStorage queryEntity(String id) {
        TenStorage tenStorage = this.getById(id);
        if(ObjectUtil.isEmpty(tenStorage)) {
            throw new CommonException("租户不存在，id值为：{}", id);
        }
        return tenStorage;
    }

    @Override
    public List<TenDbsSelectorResult> dbsList() {
        // 获取已被使用的数据源id集合
        List<String> usedDbsIdList = this.list().stream().map(TenStorage::getDbsId).collect(Collectors.toList());
        // 获取所有租户数据源集合
        List<TenDbsSelectorResult> selectorResultList = dbsApi.tenDbsSelector().stream()
                .map(jsonObject -> JSONUtil.toBean(jsonObject, TenDbsSelectorResult.class)).collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(usedDbsIdList)) {
            // 过滤掉已被使用的数据源id集合
            return selectorResultList.stream().filter(tenDbsSelectorResult -> !usedDbsIdList
                    .contains(tenDbsSelectorResult.getId())).collect(Collectors.toList());
        } else {
            // 返回全部的
            return selectorResultList;
        }
    }
}
