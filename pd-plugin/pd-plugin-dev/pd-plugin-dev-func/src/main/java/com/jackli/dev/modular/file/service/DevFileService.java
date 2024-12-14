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
package com.jackli.dev.modular.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import com.jackli.dev.modular.file.entity.DevFile;
import com.jackli.dev.modular.file.param.DevFileIdParam;
import com.jackli.dev.modular.file.param.DevFileListParam;
import com.jackli.dev.modular.file.param.DevFilePageParam;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件Service接口
 *
 * @author lijinchang
 * @date 2022/2/23 18:27
 **/
public interface DevFileService extends IService<DevFile> {

    Tuple2<String, String> uploadLocal(String engine, MultipartFile file);

    /**
     * MultipartFile文件上传，返回文件id
     *
     * @author lijinchang
     * @date 2022/4/22 15:53
     **/
    String uploadReturnId(String engine, MultipartFile file);

    /**
     * MultipartFile文件上传，返回文件Url
     *
     * @author lijinchang
     * @date 2022/4/22 15:53
     **/
    String uploadReturnUrl(String engine, MultipartFile file);

    /**
     * 文件分页列表接口
     *
     * @author lijinchang
     * @date 2022/6/21 15:44
     **/
    Page<DevFile> page(DevFilePageParam devFilePageParam);

    /**
     * 文件列表接口
     *
     * @author lijinchang
     * @date 2022/6/21 15:44
     **/
    List<DevFile> list(DevFileListParam devFileListParam);

    /**
     * 下载文件
     *
     * @author lijinchang
     * @date 2022/6/21 15:44
     **/
    void download(DevFileIdParam devFileIdParam, HttpServletResponse response) throws IOException;

    /**
     * 删除文件
     *
     * @author lijinchang
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevFileIdParam> devFileIdParamList);

    /**
     * 获取文件详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    DevFile detail(DevFileIdParam devFileIdParam);

    /**
     * 获取文件详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    DevFile queryEntity(String id);

    /**
     *  根据业务类型（指定上传的路径）上传文件
     * @param businessType
     * @param file
     * @return
     */
    String uploadCustomFile(String businessType, MultipartFile file);
}
