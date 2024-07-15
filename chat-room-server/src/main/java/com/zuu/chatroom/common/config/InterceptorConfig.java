package com.zuu.chatroom.common.config;

import com.zuu.chatroom.common.interceptor.RequestInfoInterceptor;
import com.zuu.chatroom.common.interceptor.TokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/14 12:59
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Resource
    private TokenInterceptor tokenInterceptor;
    @Resource
    private RequestInfoInterceptor requestInfoInterceptor;

    //拦截器不进行拦截的路径
    private final static List<String> ignorePaths = new ArrayList<>();

    static {
        // swagger
        ignorePaths.add("/swagger-resources/**");
        ignorePaths.add("/doc.html");
        ignorePaths.add("/v3/**");
        ignorePaths.add("/webjars/**");
        ignorePaths.add("/springdoc/**");
        ignorePaths.add("/static/**");
        ignorePaths.add("/templates/**");
        ignorePaths.add("/error");
        ignorePaths.add("/cipher/check");
        ignorePaths.add("/manager/login");
        ignorePaths.add("/swagger-ui.html");

        //wx
        ignorePaths.add("/wx/portal/**");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //先拦截认证，再拦截授权
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns(ignorePaths);

        registry.addInterceptor(requestInfoInterceptor).addPathPatterns("/**").excludePathPatterns(ignorePaths);
    }

    /*
     * 静态资源配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
