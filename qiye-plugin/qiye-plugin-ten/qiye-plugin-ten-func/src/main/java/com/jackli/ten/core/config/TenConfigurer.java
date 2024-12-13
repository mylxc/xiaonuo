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
package com.jackli.ten.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import com.jackli.common.pojo.CommonResult;
import com.jackli.ten.core.context.TenContextHolder;
import com.jackli.ten.core.filter.TenResolveFilter;
import com.jackli.ten.core.prop.TenProperties;

import javax.annotation.Resource;
import java.util.List;

/**
 * 动态租户解析过滤器配置
 *
 * @author lijinchang
 * @date 2022/3/11 16:21
 **/
@Configuration
public class TenConfigurer implements WebMvcConfigurer {

    @Resource
    private TenProperties tenProperties;

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * 动态租户解析过滤器
     *
     * @author lijinchang
     * @date 2022/6/15 9:19
     **/
    @Bean
    public FilterRegistrationBean<TenResolveFilter> dynamicTenResolveFilter() {
        FilterRegistrationBean<TenResolveFilter> registration = new FilterRegistrationBean<>(new TenResolveFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * 共享库方式数据隔离动态租户插件，优先于分页插件添加，当开启后，会在增删改查的sql中拼接TENANT_ID
     *
     * @author lijinchang
     * @date 2022/3/11 10:59
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptorPrimary() {
        MybatisPlusInterceptor mybatisPlusInterceptorPrimary = new MybatisPlusInterceptor();
        // 添加行级动态租户插件
        mybatisPlusInterceptorPrimary.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                // 获取当前租户的ID
                String currentTenantId = TenContextHolder.get();
                return ObjectUtil.isEmpty(currentTenantId)?new StringValue(tenProperties.getDefaultTenId()):new StringValue(currentTenantId);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // 未开启多租户则返回true，表示所有表都不需要拼多租户条件
                if(!tenProperties.getEnabled()) {
                    return true;
                } else {
                    // 根据配置的忽略表名决定是否拼接
                    String ignoreTableNames = tenProperties.getIgnoreTableNames();
                    if(ObjectUtil.isNotEmpty(ignoreTableNames)) {
                        List<String> ignoreTableNameList = StrUtil.split(ignoreTableNames.toLowerCase(), StrUtil.COMMA);
                        return ignoreTableNameList.contains(tableName.toLowerCase());
                    } else {
                        // 未配置则返回false表示所有表都需要拼多租户条件
                        return false;
                    }
                }
            }

            @Override
            public String getTenantIdColumn() {
                // 租户字段名称
                return tenProperties.getTenIdColumnName();
            }
        }));
        // 添加分页插件
        mybatisPlusInterceptorPrimary.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptorPrimary;
    }

    /**
     * API文档分组配置
     *
     * @author lijinchang
     * @date 2022/7/7 16:18
     **/
    @Bean(value = "tenDocApi")
    public Docket tenDocApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("动态租户TEN")
                        .description("动态租户TEN")
                        .termsOfServiceUrl("https://www.jcxxdd.com")
                        .contact(new Contact("JACKLI_TEAM","https://www.jcxxdd.com", "lijinchang29@foxmail.com"))
                        .version("2.0.0")
                        .build())
                .globalResponseMessage(RequestMethod.GET, CommonResult.responseList())
                .globalResponseMessage(RequestMethod.POST, CommonResult.responseList())
                .groupName("动态租户TEN")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.jackli.ten"))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("动态租户TEN"));
    }
}
