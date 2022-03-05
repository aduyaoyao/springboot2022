package com.example.demo4.config;

import com.example.demo4.inteceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig  implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")//拦截所有包括动态和静态请求。
        .excludePathPatterns("/login","/css/**","/fonts/**","/images/**","/js/**","/sql");
        /**方向静态资源办法
         * 1.excludePathPatterns中增加需要方向的资源目录
         * 2.配置前缀。然后方向前缀，但是跳转和访问都需要带前缀
         * #spring.mvc.static-path-pattern=/static
         */
    }
}
