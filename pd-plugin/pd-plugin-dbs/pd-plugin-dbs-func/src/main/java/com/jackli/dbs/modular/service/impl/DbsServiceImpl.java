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
package com.jackli.dbs.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jackli.common.enums.CommonSortOrderEnum;
import com.jackli.common.exception.CommonException;
import com.jackli.common.page.CommonPageRequest;
import com.jackli.dbs.modular.entity.DbsStorage;
import com.jackli.dbs.modular.enums.DbsCategoryEnum;
import com.jackli.dbs.modular.mapper.DbsMapper;
import com.jackli.dbs.modular.param.*;
import com.jackli.dbs.modular.result.DbsTableColumnResult;
import com.jackli.dbs.modular.result.DbsTableResult;
import com.jackli.dbs.modular.service.DbsService;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据源Service接口实现类
 *
 * @author lijinchang
 * @date 2022/6/8 19:47
 **/
@Service
public class DbsServiceImpl extends ServiceImpl<DbsMapper, DbsStorage> implements DbsService {

    @Resource
    private DataSource dataSource;

    @Resource
    private DefaultDataSourceCreator defaultDataSourceCreator;

    @Override
    public Page<DbsStorage> page(DbsStoragePageParam dbsStoragePageParam) {
        QueryWrapper<DbsStorage> queryWrapper = new QueryWrapper<>();
        // 查询部分字段
        queryWrapper.lambda().select(DbsStorage::getId, DbsStorage::getPoolName, DbsStorage::getUrl,
                DbsStorage::getDriverName, DbsStorage::getCategory, DbsStorage::getSortCode);
        if(ObjectUtil.isNotEmpty(dbsStoragePageParam.getCategory())) {
            queryWrapper.lambda().eq(DbsStorage::getCategory, dbsStoragePageParam.getCategory());
        }
        if(ObjectUtil.isNotEmpty(dbsStoragePageParam.getSearchKey())) {
            queryWrapper.lambda().like(DbsStorage::getPoolName, dbsStoragePageParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(dbsStoragePageParam.getSortField(), dbsStoragePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(dbsStoragePageParam.getSortOrder());
            queryWrapper.orderBy(true, dbsStoragePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(dbsStoragePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(DbsStorage::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(DbsStorageAddParam dbsStorageAddParam) {
        checkParam(dbsStorageAddParam);
        DbsStorage dbsStorage = BeanUtil.toBean(dbsStorageAddParam, DbsStorage.class);
        // 执行保存
        this.save(dbsStorage);
        try {
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtil.copyProperties(dbsStorage, dataSourceProperty, true);
            // 将数据源添加到动态数据源
            dataSources.put(dataSourceProperty.getPoolName(), defaultDataSourceCreator.createDataSource(dataSourceProperty));
        } catch (Exception e) {
            throw new CommonException("无法连接该数据源");
        }
    }

    private void checkParam(DbsStorageAddParam dbsStorageAddParam) {
        DbsCategoryEnum.validate(dbsStorageAddParam.getCategory());
        boolean hasSameDbs = this.count(new LambdaQueryWrapper<DbsStorage>()
                .eq(DbsStorage::getPoolName, dbsStorageAddParam.getPoolName())) > 0;
        if (hasSameDbs) {
            throw new CommonException("存在重复的数据源，名称为：{}", dbsStorageAddParam.getPoolName());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(DbsStorageEditParam dbsStorageEditParam) {
        DbsStorage dbsStorage = this.queryEntity(dbsStorageEditParam.getId());
        BeanUtil.copyProperties(dbsStorageEditParam, dbsStorage);
        // 执行更新
        this.updateById(dbsStorage);
        try {
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
            // 将数据源添加到动态数据源
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtil.copyProperties(dbsStorage, dataSourceProperty, true);
            // 移除老数据源
            dataSources.put(dataSourceProperty.getPoolName(), defaultDataSourceCreator.createDataSource(dataSourceProperty));
        } catch (Exception e) {
            throw new CommonException("无法连接该数据源");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<DbsStorageIdParam> dbsStorageIdParamList) {
        List<String> dbsStorageIdList = CollStreamUtil.toList(dbsStorageIdParamList, DbsStorageIdParam::getId);
        if(ObjectUtil.isNotEmpty(dbsStorageIdList)) {
            this.listByIds(dbsStorageIdList).forEach(dbsStorage -> {
                // 移除对应的数据源
                DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
                Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
                dataSources.remove(dbsStorage.getPoolName());
            });
            // 删除
            this.removeBatchByIds(dbsStorageIdList);
        }
    }

    @Override
    public DbsStorage detail(DbsStorageIdParam dbsStorageIdParam) {
        return this.queryEntity(dbsStorageIdParam.getId());
    }

    @Override
    public DbsStorage queryEntity(String id) {
        DbsStorage dbsStorage = this.getById(id);
        if (ObjectUtil.isEmpty(dbsStorage)) {
            throw new CommonException("数据源不存在，id值为：{}", id);
        }
        return dbsStorage;
    }

    @Override
    public List<DbsTableResult> tables() {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = this.getCurrentDataSource().getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();
            String schema = null;
            if (url.toLowerCase().contains("oracle")) {
                schema = metaData.getUserName();
            }
            List<DbsTableResult> tables = new ArrayList<>();
            rs = metaData.getTables(null, schema, "%", new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME").toUpperCase();
                if (!tableName.startsWith("ACT_")) {
                    DbsTableResult dbsTableResult = new DbsTableResult();
                    dbsTableResult.setTableName(tableName);
                    String remarks = rs.getString("REMARKS");
                    if(ObjectUtil.isEmpty(remarks)) {
                        dbsTableResult.setTableRemark(tableName);
                    } else {
                        dbsTableResult.setTableRemark(remarks);
                    }
                    tables.add(dbsTableResult);
                }
            }
            return tables;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new CommonException("获取数据库表失败");
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(conn);
        }
    }

    @Override
    public List<DbsTableColumnResult> tableColumns(DbsStorageTableColumnParam dbsStorageTableColumnParam) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = this.getCurrentDataSource().getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();
            String schema = null;
            if (url.toLowerCase().contains("oracle")) {
                schema = metaData.getUserName();
            }
            List<DbsTableColumnResult> columns = new ArrayList<>();
            rs = metaData.getColumns(null, schema, dbsStorageTableColumnParam.getTableName(), "%");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME").toUpperCase();
                DbsTableColumnResult dbsTableColumnResult = new DbsTableColumnResult();
                dbsTableColumnResult.setColumnName(columnName);
                String remarks = rs.getString("REMARKS");
                if(ObjectUtil.isEmpty(remarks)) {
                    dbsTableColumnResult.setColumnRemark(columnName);
                } else {
                    dbsTableColumnResult.setColumnRemark(remarks);
                }
                columns.add(dbsTableColumnResult);
            }
            return columns;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new CommonException("获取数据库表字段失败，表名称：{}", dbsStorageTableColumnParam.getTableName());
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(conn);
        }
    }

    @Override
    public List<JSONObject> dbsSelector() {
      DynamicRoutingDataSource ds= (DynamicRoutingDataSource) dataSource;;
//      List<JSONObject> list=new ArrayList<JSONObject>();
//        ds.getDataSources().forEach((key,val)->{
//            try {
//                list.add(JSONUtil.createObj().set("id", key).set("poolName", val.getConnection().getSchema()));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        });
//      return list;
        return this.list().stream().map(item -> JSONUtil.createObj().set("id", item.getId()).set("poolName", item.getPoolName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> tenDbsSelector() {
        // 只查询租户类型数据源
        return this.list(new LambdaQueryWrapper<DbsStorage>().eq(DbsStorage::getCategory, DbsCategoryEnum.SLAVE.getValue())).stream()
                .map(item -> JSONUtil.createObj().set("id", item.getId()).set("poolName", item.getPoolName())).collect(Collectors.toList());
    }

    @Override
    public String getDefaultDataSourceName() {
        return new DynamicDataSourceProperties().getPrimary();
    }

    @Override
    public String getCurrentDataSourceName() {
        String dataSourceName = DynamicDataSourceContextHolder.peek();
        if (ObjectUtil.isEmpty(dataSourceName)) {
            dataSourceName = new DynamicDataSourceProperties().getPrimary();
        }
        return dataSourceName;
    }

    @Override
    public DataSource getCurrentDataSource() {
        String currentDataSourceName = this.getCurrentDataSourceName();
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
        return dynamicRoutingDataSource.getDataSource(currentDataSourceName);
    }

    @Override
    public void changeDataSource(String name) {
        DynamicDataSourceContextHolder.push(name);
    }
}
