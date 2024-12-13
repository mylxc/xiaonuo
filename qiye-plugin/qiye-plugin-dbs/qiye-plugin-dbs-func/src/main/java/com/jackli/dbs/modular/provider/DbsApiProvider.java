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
package com.jackli.dbs.modular.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.stereotype.Service;
import com.jackli.dbs.api.DbsApi;
import com.jackli.dbs.modular.param.DbsStorageIdParam;
import com.jackli.dbs.modular.param.DbsStorageTableColumnParam;
import com.jackli.dbs.modular.result.DbsTableColumnResult;
import com.jackli.dbs.modular.service.DbsService;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据源API接口提供者
 *
 * @author lijinchang
 * @date 2022/3/8 16:34
 **/
@Service
public class DbsApiProvider implements DbsApi {

    @Resource
    private DataSource dataSource;

    @Resource
    private DbsService dbsStorageService;

    @Override
    public String getDefaultDataSourceName() {
        return new DynamicDataSourceProperties().getPrimary();
    }

    @Override
    public String getCurrentDataSourceName() {
        return dbsStorageService.getCurrentDataSourceName();
    }

    @Override
    public DataSource getCurrentDataSource() {
        return dbsStorageService.getCurrentDataSource();
    }

    @Override
    public void changeDataSource(String name) {
        dbsStorageService.changeDataSource(name);
    }

    @Override
    public JSONObject dbsDetail(String dbsId) {
        DbsStorageIdParam dbsStorageIdParam = new DbsStorageIdParam();
        dbsStorageIdParam.setId(dbsId);
        return JSONUtil.parseObj(dbsStorageService.detail(dbsStorageIdParam));
    }

    @Override
    public List<JSONObject> dbsSelector() {
        return dbsStorageService.dbsSelector();
    }

    @Override
    public List<JSONObject> tenDbsSelector() {
        return dbsStorageService.tenDbsSelector();
    }

    @Override
    public List<String> tableColumns(String tableName) {
        DbsStorageTableColumnParam dbsStorageTableColumnParam = new DbsStorageTableColumnParam();
        dbsStorageTableColumnParam.setTableName(tableName);
        return dbsStorageService.tableColumns(dbsStorageTableColumnParam).stream().map(DbsTableColumnResult::getColumnName)
                .collect(Collectors.toList());
    }
}
