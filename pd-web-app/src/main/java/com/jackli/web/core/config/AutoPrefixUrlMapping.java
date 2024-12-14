package com.jackli.web.core.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 自动补全路由前缀处理类
 */
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {
    /**
     * 重写方法路由获取
     *
     * @param method
     * @param handlerType
     * @return
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(@NotNull Method method, @NotNull Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);

        if (Objects.nonNull(mappingInfo)) {
            String prefix = this.getPrefix(handlerType);

            if (prefix != null) {
                String[] paths = mappingInfo.getPatternValues()
                        .stream()
                        .map(path -> prefix + path)
                        .toArray(String[]::new);

                return mappingInfo.mutate()
                        .paths(paths)
                        .build();
            }
        }

        return mappingInfo;
    }

    /**
     * 获取方法路由前缀
     *
     * @param handleType
     * @return
     */
    private String getPrefix(Class<?> handleType) {
        String packageName = handleType.getPackage().getName();
        // 如果包名com.jackli.biz.modular.*.controller
        // 请求前缀"api/bizapp"
        if (packageName.startsWith("com.jackli.biz.modular") && packageName.endsWith("controller")) {
            return "api/bizapp";
        }
        // 其他*.controller
        // 请求前缀"api/webapp"
        else if (packageName.endsWith("controller")) {
            return "api/webapp";
        }
        else {
            return null;
        }
    }

}
