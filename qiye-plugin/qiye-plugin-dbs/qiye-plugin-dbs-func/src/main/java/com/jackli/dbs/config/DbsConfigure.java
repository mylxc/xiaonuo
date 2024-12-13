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
package com.jackli.dbs.config;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.event.EncDataSourceInitEvent;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.jackli.common.enums.CommonDeleteFlagEnum;
import com.jackli.common.pojo.CommonResult;
import com.jackli.dbs.modular.entity.DbsStorage;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源相关配置
 *
 * @author lijinchang
 * @date 2022/1/6 23:10
 */
@Configuration
public class DbsConfigure {

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * 自定义数据源来源
     *
     * @author lijinchang
     * @date 2022/3/8 18:57
     **/
    @AllArgsConstructor
    @Component
    @Order
    public static class DynamicDataSourceProvider extends AbstractDataSourceProvider {

        private final DynamicDataSourceProperties dynamicDataSourceProperties;

        @Resource
        private final MybatisPlusProperties mybatisPlusProperties;

        @Resource
        private final DefaultDataSourceCreator defaultDataSourceCreator;

        @Override
        public Map<String, DataSource> loadDataSources() {
            HashMap<String, DataSource> dataSourceHashMap = MapUtil.newHashMap();
            Map<String, DataSourceProperty> dataSourcePropertyMap = dynamicDataSourceProperties.getDatasource();
            if(ObjectUtil.isNotEmpty(dataSourcePropertyMap)) {
                String primaryDsName = new DynamicDataSourceProperties().getPrimary();
                DataSourceProperty masterDataSourceProperty = dataSourcePropertyMap.get(primaryDsName);
                if(ObjectUtil.isNotEmpty(masterDataSourceProperty)) {
                    Connection conn = null;
                    try {
                        if (ObjectUtil.isEmpty(masterDataSourceProperty.getPublicKey())) {
                            masterDataSourceProperty.setPublicKey(dynamicDataSourceProperties.getPublicKey());
                        }
                        EncDataSourceInitEvent encDataSourceInitEvent = new EncDataSourceInitEvent();
                        encDataSourceInitEvent.beforeCreate(masterDataSourceProperty);
                        conn = DriverManager.getConnection(masterDataSourceProperty.getUrl(), masterDataSourceProperty.getUsername(),
                                masterDataSourceProperty.getPassword());
                        String dbsTableName;
                        Object annotationValue = AnnotationUtil.getAnnotationValue(DbsStorage.class, TableName.class);
                        if(ObjectUtil.isNotEmpty(annotationValue)) {
                            dbsTableName = Convert.toStr(annotationValue);
                        } else {
                            dbsTableName = StrUtil.toUnderlineCase(DbsStorage.class.getSimpleName());
                        }
                        GlobalConfig.DbConfig dbConfig = mybatisPlusProperties.getGlobalConfig().getDbConfig();
                        String logicDeleteField = dbConfig.getLogicDeleteField();
                        if(ObjectUtil.isEmpty(logicDeleteField)) {
                            logicDeleteField = "DELETE_FLAG";
                        }
                        Short logicNotDeleteValue = Short.valueOf(dbConfig.getLogicNotDeleteValue());
                        if(ObjectUtil.isEmpty(logicNotDeleteValue)) {
                            logicNotDeleteValue = CommonDeleteFlagEnum.NOT_DELETE.getCode();
                        }
                        List<Entity> entityList = SqlExecutor.query(conn, "SELECT * FROM " + dbsTableName + " WHERE " + logicDeleteField + " = \"" + logicNotDeleteValue + "\"", new EntityListHandler());
                        entityList.forEach(entity -> {
                            DataSourceProperty dataSourceProperty = new DataSourceProperty();
                            BeanUtil.copyProperties(entity, dataSourceProperty, true);
                            dataSourceHashMap.put(dataSourceProperty.getPoolName(), defaultDataSourceCreator.createDataSource(dataSourceProperty));
                        });
                    } catch (SQLException ignored) {
                    } finally {
                        DbUtil.close(conn);
                    }
                }
            }
            return dataSourceHashMap;
        }
    }

    /**
     * API文档分组配置
     *
     * @author lijinchang
     * @date 2022/7/7 16:18
     **/
    @Bean(value = "dbsDocApi")
    public Docket dbsDocApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("多数据源DBS")
                        .description("多数据源DBS")
                        .termsOfServiceUrl("https://www.jcxxdd.com")
                        .contact(new Contact("JACKLI_TEAM","https://www.jcxxdd.com", "lijinchang29@foxmail.com"))
                        .version("2.0.0")
                        .build())
                .globalResponseMessage(RequestMethod.GET, CommonResult.responseList())
                .globalResponseMessage(RequestMethod.POST, CommonResult.responseList())
                .groupName("多数据源DBS")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.jackli.dbs"))
                .paths(PathSelectors.any())
                .build().extensions(openApiExtensionResolver.buildExtensions("多数据源DBS"));
    }
}
