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
package com.jackli.dev.modular.file.service.impl;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackli.common.exception.CommonException;
import com.jackli.common.page.CommonPageRequest;
import com.jackli.common.prop.CommonProperties;
import com.jackli.common.util.CommonDownloadUtil;
import com.jackli.common.util.CommonResponseUtil;
import com.jackli.dev.modular.config.service.DevConfigService;
import com.jackli.dev.modular.file.entity.DevFile;
import com.jackli.dev.modular.file.enums.DevFileEngineTypeEnum;
import com.jackli.dev.modular.file.mapper.DevFileMapper;
import com.jackli.dev.modular.file.param.DevFileIdParam;
import com.jackli.dev.modular.file.param.DevFileListParam;
import com.jackli.dev.modular.file.param.DevFilePageParam;
import com.jackli.dev.modular.file.service.DevFileService;
import com.jackli.dev.modular.file.util.DevFileAliyunUtil;
import com.jackli.dev.modular.file.util.DevFileLocalUtil;
import com.jackli.dev.modular.file.util.DevFileMinIoUtil;
import com.jackli.dev.modular.file.util.DevFileTencentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 文件Service接口实现类
 *
 * @author lijinchang
 * @date 2022/2/23 18:43
 **/
@Service
public class DevFileServiceImpl extends ServiceImpl<DevFileMapper, DevFile> implements DevFileService {

    @Autowired
    private DevConfigService devConfigService;
    @Resource
    private CommonProperties commonProperties;

    @Override
    public Tuple2<String, String> uploadLocal(String engine, MultipartFile file) {
        return this.storageFile(engine, file);
    }

    @Override
    public String uploadReturnId(String engine, MultipartFile file) {
        return this.storageFile(engine, file, true);
    }

    @Override
    public String uploadReturnUrl(String engine, MultipartFile file) {
        return this.storageFile(engine, file, false);
    }

    @Override
    public Page<DevFile> page(DevFilePageParam devFilePageParam) {
        QueryWrapper<DevFile> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(devFilePageParam.getEngine())) {
            queryWrapper.lambda().eq(DevFile::getEngine, devFilePageParam.getEngine());
        }
        if(ObjectUtil.isNotEmpty(devFilePageParam.getSearchKey())) {
            queryWrapper.lambda().like(DevFile::getName, devFilePageParam.getSearchKey());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public List<DevFile> list(DevFileListParam devFileListParam) {
        QueryWrapper<DevFile> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(devFileListParam.getEngine())) {
            queryWrapper.lambda().eq(DevFile::getEngine, devFileListParam.getEngine());
        }
        if(ObjectUtil.isNotEmpty(devFileListParam.getSearchKey())) {
            queryWrapper.lambda().like(DevFile::getName, devFileListParam.getSearchKey());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void download(DevFileIdParam devFileIdParam, HttpServletResponse response) throws IOException {
        DevFile devFile = this.queryEntity(devFileIdParam.getId());
        if(!devFile.getEngine().equals(DevFileEngineTypeEnum.LOCAL.getValue())) {
            CommonResponseUtil.renderError(response, "非本地文件不支持此方式下载，id值为：" + devFile.getId());
            return;
        }
        File file = FileUtil.file(devFile.getStoragePath());
        if(!FileUtil.exist(file)) {
            CommonResponseUtil.renderError(response, "找不到存储的文件，id值为：" + devFile.getId());
            return;
        }
        CommonDownloadUtil.download(devFile.getName(), IoUtil.readBytes(FileUtil.getInputStream(file)), response);
    }

    @Override
    public void delete(List<DevFileIdParam> devFileIdParamList) {
        this.removeBatchByIds(CollStreamUtil.toList(devFileIdParamList, DevFileIdParam::getId));
    }

    private String storageFile(String engine, MultipartFile file, boolean returnFileId) {
        Tuple2<String, String> result = this.storageFile(engine, file);
        // 如果是返回id则返回文件id
        if(returnFileId) {
            return result.getT1();
        } else {
            // 否则返回下载地址
            return result.getT2();
        }
    }

    /**
     * 存储文件
     * @param :businessType 业务类型，例如:工装文件上传
     * @author lijinchang
     * @date 2022/6/16 16:24
     **/
    private Tuple2<String, String> storageFile(String engine, MultipartFile file) {

        // 如果引擎为空，默认使用本地
        if(ObjectUtil.isEmpty(engine)) {
            engine = DevFileEngineTypeEnum.LOCAL.getValue();
        }

        // 生成id
        String fileId = IdWorker.getIdStr();

        // 存储桶名称
        String bucketName;

        // 定义存储的url，本地文件返回文件实际路径，其他引擎返回网络地址
        String storageUrl;

        // 根据引擎类型执行不同方法
        if(engine.equals(DevFileEngineTypeEnum.LOCAL.getValue())) {

            // 使用固定名称defaultBucketName
            bucketName = "defaultBucketName";
            storageUrl = DevFileLocalUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        } else if(engine.equals(DevFileEngineTypeEnum.ALIYUN.getValue())) {

            // 使用阿里云默认配置的bucketName
            bucketName = DevFileAliyunUtil.getDefaultBucketName();
            storageUrl = DevFileAliyunUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        } else if(engine.equals(DevFileEngineTypeEnum.TENCENT.getValue())) {

            // 使用腾讯云默认配置的bucketName
            bucketName = DevFileTencentUtil.getDefaultBucketName();
            storageUrl = DevFileTencentUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        } else if(engine.equals(DevFileEngineTypeEnum.MINIO.getValue())) {

            // 使用MINIO默认配置的bucketName
            bucketName = DevFileMinIoUtil.getDefaultBucketName();
            storageUrl = DevFileMinIoUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);
        } else {
            throw new CommonException("不支持的文件引擎：{}", engine);
        }

        // 时间格式
        String dateFolderPath = DateUtil.thisYear() + StrUtil.SLASH +
                (DateUtil.thisMonth() + 1) + StrUtil.SLASH +
                DateUtil.thisDayOfMonth() + StrUtil.SLASH;

        // 将文件信息保存到数据库
        DevFile devFile = new DevFile();
        devFile.setChangeablePath(dateFolderPath);
        // 设置文件id
        devFile.setId(fileId);

        // 设置存储引擎类型
        devFile.setEngine(engine);
        devFile.setBucket(bucketName);
        devFile.setName(file.getOriginalFilename());
        String suffix = ObjectUtil.isNotEmpty(file.getOriginalFilename())?StrUtil.subAfter(file.getOriginalFilename(),
                StrUtil.DOT, true):null;
        devFile.setSuffix(suffix);
        devFile.setSizeKb(Convert.toStr(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024))
                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        devFile.setSizeInfo(FileUtil.readableFileSize(file.getSize()));
        devFile.setObjName(ObjectUtil.isNotEmpty(devFile.getSuffix())?fileId + StrUtil.DOT + devFile.getSuffix():null);
        // 如果是图片，则压缩生成缩略图
        if(ObjectUtil.isNotEmpty(suffix)) {
            if(isPic(suffix)) {
                try {
                    devFile.setThumbnail(ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(file.getBytes()),
                            100, 100, null), suffix));
                } catch (IOException e) {
                    throw new CommonException("文件上传失败，获取文件流错误");
                }
            }
        }
        // 存储路径
        devFile.setStoragePath(storageUrl);

        // 定义下载地址
        String downloadUrl;

        // 下载路径，注意：本地文件下载地址设置为下载接口地址 + 文件id
        if(engine.equals(DevFileEngineTypeEnum.LOCAL.getValue())) {
            String apiUrl =devConfigService.getValueByKey("fileServerIP");
            if(ObjectUtil.isEmpty(apiUrl)) {
                apiUrl=commonProperties.getBackendUrl();
            }
            if(ObjectUtil.isEmpty(apiUrl)) {
                throw new CommonException("后端域名地址未正确配置：snowy.config.common.backend-url为空");
            }
            downloadUrl= apiUrl + "/dev/file/download?id=" + fileId;
            devFile.setDownloadPath(downloadUrl);
        } else {
            // 阿里云、腾讯云、MINIO可以直接使用存储地址（公网）作为下载地址
            downloadUrl= storageUrl;
            devFile.setDownloadPath(devFile.getStoragePath());
        }
        this.save(devFile);
        return Tuples.of(fileId, downloadUrl);
    }

    /**
     * 生成文件的key，格式如 2021/10/11/1377109572375810050.docx
     *
     * @author lijinchang
     * @date 2022/4/22 15:58
     **/
    public String genFileKey(String fileId, MultipartFile file) {

        // 获取文件原始名称
        String originalFileName = file.getOriginalFilename();

        // 获取文件后缀
        String fileSuffix = FileUtil.getSuffix(originalFileName);

        // 生成文件的对象名称，格式如:1377109572375810050.docx
        String fileObjectName = fileId + StrUtil.DOT + fileSuffix;

        // 获取日期文件夹，格式如，2021/10/11/
        String dateFolderPath = DateUtil.thisYear() + StrUtil.SLASH +
                (DateUtil.thisMonth() + 1) + StrUtil.SLASH +
                DateUtil.thisDayOfMonth() + StrUtil.SLASH;

        // 返回
        return dateFolderPath + fileObjectName;
    }

    @Override
    public DevFile detail(DevFileIdParam devFileIdParam) {
        return this.queryEntity(devFileIdParam.getId());
    }

    @Override
    public DevFile queryEntity(String id) {
        DevFile devFile = this.getById(id);
        if(ObjectUtil.isEmpty(devFile)) {
            throw new CommonException("文件不存在，id值为：{}", id);
        }
        return devFile;
    }

    /**
     * 根据文件后缀判断是否图片
     *
     * @author lijinchang
     * @date 2020/7/6 15:31
     */
    private static boolean isPic(String fileSuffix) {
        fileSuffix = fileSuffix.toLowerCase();
        return ImgUtil.IMAGE_TYPE_GIF.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_JPEG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_BMP.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PNG.equals(fileSuffix)
                || ImgUtil.IMAGE_TYPE_PSD.equals(fileSuffix);
    }

    @Override
    public String uploadCustomFile(String businessType, MultipartFile file) {
        // 默认可以通过配置来获取需要保存的文件服务器
        // 生成id
        String fileId = IdWorker.getIdStr();
        // 存储桶名称
        String bucketName;
        // 定义存储的url，本地文件返回文件实际路径，其他引擎返回网络地址
        String storageUrl;

        // 使用固定名称defaultBucketName
        bucketName = "defaultBucketName";
        storageUrl = DevFileLocalUtil.storageFileWithReturnUrl(bucketName, genFileKey(fileId, file), file);

        // 将文件信息保存到数据库
        DevFile devFile = new DevFile();

        // 设置文件id
        devFile.setId(fileId);

        // 设置存储引擎类型
        devFile.setEngine("LOCAL");
        devFile.setBucket(bucketName);
        devFile.setName(file.getOriginalFilename());
        String suffix = ObjectUtil.isNotEmpty(file.getOriginalFilename())?StrUtil.subAfter(file.getOriginalFilename(),
                StrUtil.DOT, true):null;
        devFile.setSuffix(suffix);
        devFile.setSizeKb(Convert.toStr(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024))
                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        devFile.setSizeInfo(FileUtil.readableFileSize(file.getSize()));
        devFile.setObjName(ObjectUtil.isNotEmpty(devFile.getSuffix())?fileId + StrUtil.DOT + devFile.getSuffix():null);
        // 如果是图片，则压缩生成缩略图
        if(ObjectUtil.isNotEmpty(suffix)) {
            if(isPic(suffix)) {
                try {
                    devFile.setThumbnail(ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(file.getBytes()),
                            100, 100, null), suffix));
                } catch (IOException e) {
                    throw new CommonException("文件上传失败，获取文件流错误");
                }
            }
        }
        // 存储路径
        devFile.setStoragePath(storageUrl);
        // 定义下载地址
        String downloadUrl;
        // 下载路径，注意：本地文件下载地址设置为下载接口地址 + 文件id
        String apiUrl =devConfigService.getValueByKey("fileServerIP");
        if(ObjectUtil.isEmpty(apiUrl)) {
            apiUrl=commonProperties.getBackendUrl();
        }
        if(ObjectUtil.isEmpty(apiUrl)) {
            throw new CommonException("后端域名地址未正确配置：snowy.config.common.backend-url为空");
        }
        downloadUrl= apiUrl + "/dev/file/download?id=" + fileId;
        devFile.setDownloadPath(downloadUrl);

        this.save(devFile);
        Tuple2<String, String> result = Tuples.of(fileId, downloadUrl);
        // 如果是返回id则返回文件id
        return result.getT1();

    }

}
