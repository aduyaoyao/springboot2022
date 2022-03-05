package com.atguigu.boot;

import com.atguigu.boot.beans.User;
import com.atguigu.boot.config.MyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 主程序类
 * SpringBootApplication: 这个一个SpringBoot应用
 */
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        //1.返回我们的IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
        //2.查看容器中的组件
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String name:beanDefinitionNames) {
            System.out.println(name);
        }
        //3.从容器中获取组件

        MyConfig myConfig = run.getBean(MyConfig.class);
        System.out.println(myConfig);

        //如果@Configuration(proxyBeanMethods==true)代理对象调用方法。
        //com.atguigu.boot.config.MyConfig$$EnhancerBySpringCGLIB$$a911cb0a@4a183d02
        // 则SB总会检查这个组件是否在容器中。
        //保持组件单实例
        //flase:com.atguigu.boot.config.MyConfig@2fb5fe30
        User user01 = myConfig.user01();
        User user011 = myConfig.user01();

        System.out.println(user011==user01);
    }
}
