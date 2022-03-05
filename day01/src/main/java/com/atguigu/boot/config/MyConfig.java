package com.atguigu.boot.config;

import com.atguigu.boot.beans.Car;
import com.atguigu.boot.beans.Pet;
import com.atguigu.boot.beans.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 告诉SpringBoot，这是一个配置类。
 * 1.配置类中使用@Bean标注在方法上给容器注册组件，默认也是单实例的。
 * 2.配置类本身也是组件
 * 3.proxyBeanMethods:代理Bean的方法。
 *         full(proxyBeanMethods=true)
 *         lite(proxyBeanMethods=false)
 *         组件依赖（true）
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(Car.class)
public class MyConfig {

    /**
     * 外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象。
     * @return
     */
    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        return new User("dy",18);
    }

    @Bean("pet") //使用自定义名字，而不是默认的方法名
    public Pet pet01(){
        return new Pet("dd");
    }

}
