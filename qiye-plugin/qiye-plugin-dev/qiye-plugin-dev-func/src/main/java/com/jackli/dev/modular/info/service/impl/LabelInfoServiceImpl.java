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
package com.jackli.dev.modular.info.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackli.common.enums.CommonSortOrderEnum;
import com.jackli.common.exception.CommonException;
import com.jackli.common.page.CommonPageRequest;
import com.jackli.dev.modular.info.entity.LabelInfo;
import com.jackli.dev.modular.info.entity.LabelValue;
import com.jackli.dev.modular.info.mapper.LabelInfoMapper;
import com.jackli.dev.modular.info.mapper.LabelValueMapper;
import com.jackli.dev.modular.info.param.*;
import com.jackli.dev.modular.info.service.LabelInfoService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签信息Service接口实现类
 *
 * @author 李金昌
 * @date  2023/08/17 13:43
 **/
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo> implements LabelInfoService {
    @Resource
    private LabelValueMapper labelValueMapper;
    @Override
    public Page<LabelInfo> page(LabelInfoPageParam labelInfoPageParam) {
        QueryWrapper<LabelInfo> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(labelInfoPageParam.getLabelName())) {
            queryWrapper.lambda().like(LabelInfo::getLabelName, labelInfoPageParam.getLabelName());
        }
        if(ObjectUtil.isAllNotEmpty(labelInfoPageParam.getSortField(), labelInfoPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(labelInfoPageParam.getSortOrder());
            queryWrapper.orderBy(true, labelInfoPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(labelInfoPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(LabelInfo::getId);
        }
        Page<LabelInfo> labelInfoPage=this.page(CommonPageRequest.defaultPage(), queryWrapper);
        for (LabelInfo item:labelInfoPage.getRecords()
             ) {
            QueryWrapper<LabelValue> labelValueQueryWrapper=new QueryWrapper<>();
            labelValueQueryWrapper.lambda().eq(LabelValue::getLabelInforId,item.getId());
            List<LabelValue> values=labelValueMapper.selectList(labelValueQueryWrapper);
            item.setValueList(values);
        }
        return labelInfoPage;
    }

    @Override
    public List<LabelInfo> getList(LabelInfoPageParam labelInfoPageParam) {
        List<LabelInfo> labelInfoList = this.list();
        List<LabelValue> labelValues = labelValueMapper.selectList(new QueryWrapper<>());
        if (ObjectUtil.isNull(labelValues)) {
            return new ArrayList<>();
        }
        Map<Long, List<LabelValue>> labelInforIdListMap =
                labelValues.stream().collect(Collectors.groupingBy(LabelValue::getLabelInforId));
        labelInfoList.forEach(label -> {
            label.setValueList(labelInforIdListMap.get(label.getId()));
        });
        return  labelInfoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(LabelInfoAddParam labelInfoAddParam) {
        String labelName=labelInfoAddParam.getLabelName();
        QueryWrapper<LabelInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(LabelInfo::getLabelName,labelName).last("limit 1");
        LabelInfo single=this.baseMapper.selectOne(queryWrapper);
        if(ObjectUtil.isNotNull(single))
        {
            throw  new CommonException("已经存在一个相同的设置,不允许操作");
        }
        LabelInfo labelInfo = BeanUtil.toBean(labelInfoAddParam, LabelInfo.class);
        List<LabelValueAddParam> valueAddParams=labelInfoAddParam.getList();
        this.save(labelInfo);
        for (LabelValueAddParam item:valueAddParams
        ) {
            LabelValue value=BeanUtil.toBean(item,LabelValue.class);
            value.setLabelInforId(labelInfo.getId());//设置新的值
            labelValueMapper.insert(value);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(LabelInfoEditParam labelInfoEditParam) {
        String labelName=labelInfoEditParam.getLabelName();
        QueryWrapper<LabelInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(LabelInfo::getLabelName,labelName)
                .ne(LabelInfo::getId,labelInfoEditParam.getId())
                .last("limit 1");
        LabelInfo single=this.baseMapper.selectOne(queryWrapper);
        if(ObjectUtil.isNotNull(single))
        {
            throw  new CommonException("已经存在一个相同的设置,不允许操作");
        }
        LabelInfo labelInfo = this.queryEntity(labelInfoEditParam.getId());
        BeanUtil.copyProperties(labelInfoEditParam, labelInfo);
        this.updateById(labelInfo);
        List<LabelValueAddParam> valueAddParams=labelInfoEditParam.getList();
        for (LabelValueAddParam item:valueAddParams
        ) {
            if(ObjectUtil.isNotNull(item.getId())) {
                UpdateWrapper<LabelValue> labelValueQueryWrapper = new UpdateWrapper<>();
                labelValueQueryWrapper.lambda().set(LabelValue::getLabelValue, item.getLabelValue())
                        .eq(LabelValue::getId, item.getId());
                labelValueMapper.update(null, labelValueQueryWrapper);
            }else
            {
                LabelValue value=new LabelValue();
                value.setLabelInforId(item.getLabelInforId());
                value.setLabelValue(item.getLabelValue());
                value.setLanguageType(item.getLanguageType());
                labelValueMapper.insert(value);//如果没有的语言就新增
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<LabelInfoIdParam> labelInfoIdParamList) {
        // 执行删除
        labelInfoIdParamList.forEach( label -> {
            QueryWrapper<LabelValue> labelValueQueryWrapper = new QueryWrapper<>();
            labelValueQueryWrapper.lambda().eq(LabelValue::getLabelInforId, label.getId());
            labelValueMapper.delete(labelValueQueryWrapper);
            this.removeById(label.getId());
        });
    }

    @Override
    public LabelInfo detail(LabelInfoIdParam labelInfoIdParam) {
        return this.queryEntity(labelInfoIdParam.getId());
    }

    @Override
    public LabelInfo queryEntity(Long id) {
        LabelInfo labelInfo = this.getById(id);
        if(ObjectUtil.isEmpty(labelInfo)) {
            throw new CommonException("标签信息不存在，id值为：{}", id);
        }
        QueryWrapper<LabelValue> labelValueQueryWrapper=new QueryWrapper<>();
        labelValueQueryWrapper.lambda().eq(LabelValue::getLabelInforId,id);
        List<LabelValue> values=labelValueMapper.selectList(labelValueQueryWrapper);
        labelInfo.setValueList(values);
        return labelInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calculate(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0); // 获取第一个Sheet

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String labelName = String.valueOf(row.getCell(0));
                    String stringCellValue1 = String.valueOf(row.getCell(1));
                    QueryWrapper<LabelInfo> queryWrapper=new QueryWrapper<>();
                    queryWrapper.lambda().eq(LabelInfo::getLabelName,labelName).last("limit 1");
                    LabelInfo labelInfo = this.baseMapper.selectOne(queryWrapper);
                    if (ObjectUtil.isNotNull(labelInfo)) {
                        QueryWrapper<LabelValue> labelValueQueryWrapper = new QueryWrapper<>();
                        labelValueQueryWrapper.lambda().eq(LabelValue::getLabelInforId, labelInfo.getId())
                                .eq(LabelValue::getLanguageType, "rus").last("limit 1");
                        LabelValue labelValue = labelValueMapper.selectOne(labelValueQueryWrapper);
                        if (ObjectUtil.isNotNull(labelValue)) {
                            labelValue.setLabelValue(stringCellValue1);
                            labelValueMapper.updateById(labelValue);
                        }
                    } else {
                        LabelInfo labelInfoNew = new LabelInfo();
                        labelInfoNew.setLabelName(labelName);
                        this.save(labelInfoNew);
                        LabelValue value = new LabelValue();
                        value.setLabelInforId(labelInfoNew.getId());
                        value.setLabelValue(stringCellValue1);
                        value.setLanguageType("rus");
                        labelValueMapper.insert(value);
                    }
                }
            }
        }
    }
}
