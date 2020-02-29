package com.lxjy.mall.annotation.support;

import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.util.UserTokenManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * create by gary 2020/2/26
 * 技术交流请加QQ:498982703
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String LOGIN_TOKE_KEY = "X-CSMD-TOKEN";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(Integer.class)
                && methodParameter.hasMethodAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        String token = nativeWebRequest.getHeader(LOGIN_TOKE_KEY);
        if (StringUtils.isEmpty(token)) {
            return null;

        }

        return UserTokenManager.getUserId(token);
    }

}
