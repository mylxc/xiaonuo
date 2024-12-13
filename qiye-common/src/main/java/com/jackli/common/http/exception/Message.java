package com.jackli.common.http.exception;

/**
 * @Description
 * @Author lix
 * @Data 2024/3/30 13:32
 */
public interface Message {

    /**
     * 请使用JSON方式传参
     */
    String SERVER_ERROR = "10001";

    /**
     * 参数：{} 不能为空
     */
    String PARAMETER_NULL = "10008";

    /**
     * 请求方法应为POST
     */
    String HTTP_BAD_METHOD_GET = "10009";

    /**
     * 请求方法应为GET
     */
    String HTTP_BAD_METHOD_POST = "10010";

    /**
     * 请求方法仅支持GET或POST
     */
    String HTTP_BAD_METHOD = "10011";

    /**
     * 参数格式错误
     */
    String PARSE_ERROR = "10012";

    /**
     * 请使用JSON方式传参
     */
    String NON_JSON_FORMAT = "10013";

    /**
     * 请使用multipart/form-data方式上传文件
     */
    String MULTIPART_ERROR = "10014";

    /**
     * 请选择要上传的文件并检查文件参数名称是否正确
     */
    String MISSING_SERVLET_REQUEST_PART = "10015";

    /**
     * 数据操作异常
     */
    String PERSISTENCE_ERROR = "10016";

    /**
     * 响应参数为空
     */
    String RESPONSE_PARAMETER_IS_EMPTY="10018";




}
