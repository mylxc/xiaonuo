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
package com.jackli.ten.core.filter;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import com.jackli.common.exception.CommonException;
import com.jackli.common.util.CommonFilterExceptionUtil;
import com.jackli.common.util.CommonServletUtil;
import com.jackli.dbs.api.DbsApi;
import com.jackli.ten.core.context.TenContextHolder;
import com.jackli.ten.core.prop.TenProperties;
import com.jackli.ten.modular.entity.TenStorage;
import com.jackli.ten.modular.enums.TenCategoryEnum;
import com.jackli.ten.modular.service.TenService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 动态租户解析过滤器
 *
 * @author lijinchang
 * @date 2022/3/11 16:22
 **/
@Slf4j
public class TenResolveFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            TenProperties tenProperties = SpringUtil.getBean(TenProperties.class);
            DbsApi dbsApi = SpringUtil.getBean(DbsApi.class);
            // 如果开启了多租户
            if(tenProperties.getEnabled()) {
                // 获取到解析的租户
                TenStorage tenStorage = SpringUtil.getBean(TenService.class).getCurrentTen();
                // 获取租户id
                String tenantId = tenStorage.getId();
                if(tenantId.equals(tenProperties.getDefaultTenId())) {
                    // 执行切换租户的数据源为master
                    dbsApi.changeDataSource(dbsApi.getDefaultDataSourceName());
                    // 切换租户id为默认id
                    TenContextHolder.put(tenProperties.getDefaultTenId());
                } else {
                    if(TenCategoryEnum.DB.getValue().equals(tenStorage.getCategory())) {
                        // 执行切换租户的数据源
                        dbsApi.changeDataSource(tenStorage.getDbsName());
                        // 切换租户id为默认id
                        TenContextHolder.put(tenProperties.getDefaultTenId());
                    } else {
                        // 执行切换租户的数据源为master
                        dbsApi.changeDataSource(dbsApi.getDefaultDataSourceName());
                        // 切换租户id为其id
                        TenContextHolder.put(tenantId);
                    }
                }
            } else {
                // 执行切换租户的数据源为master
                dbsApi.changeDataSource(dbsApi.getDefaultDataSourceName());
                // 切换租户id为默认id
                TenContextHolder.put(tenProperties.getDefaultTenId());
            }
            // 继续执行
            chain.doFilter(request,response);
        } catch (Exception e) {
            // 处理filter中的异常
            if(e instanceof CommonException) {
                CommonException commonException = ((CommonException) e);
                commonException.setMsg(commonException.getMsg() + "，请求地址：" + CommonServletUtil.getRequest().getRequestURL());
            }
            CommonFilterExceptionUtil.handleFilterException(request, response, e);
        } finally {
            TenContextHolder.remove();
        }
    }
}
