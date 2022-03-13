package com.sq.SYTreeHole.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sq.SYTreeHole.Interceptor.ErrorInterceptor;
import com.sq.SYTreeHole.Interceptor.JwtInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;
import java.util.ArrayList;

@EnableCaching
@Configuration
@EnableSwagger2
@EnableWebMvc
@MapperScans({@MapperScan("com.sq.SYTreeHole.dao")})
public class Config {

    /**
     * MP相关配置
     *
     * @return 自定义的MP插件过滤器
     */
    @Bean
    public MybatisPlusInterceptor paginationInnerInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //分页拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //全表删除、更新阻断拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //乐观锁
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * redis缓存相关配置
     *
     * @param redisConnectionFactory redis链接器工厂
     * @return 返回自定的cache管理器
     */
    @Bean
    public CacheManager cacheManager(JedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(24))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())))
                // 启用 Redis 缓存，使缓存放置/驱逐操作与正在进行的 Spring 管理的事务同步。
                .transactionAware()
                .build();
    }

    /**
     * 自定义redisTemplate
     *
     * @param redisConnectionFactory redis连接工厂
     * @return 返回自定的redisTemplate
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    /**
     * 拦截器配置
     * 若想配置失效，注释此bean
     *
     * @return 返回webMVC自定配置类
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry
                        .addInterceptor(new JwtInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/loginForPass", "/loginForCode", "/register", "/code", "/resetPassword")
                        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/swagger-ui.html", "/csrf", "/");
//                registry.addInterceptor(new ErrorInterceptor())
//                        .addPathPatterns("/**")
//                        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/swagger-ui.html");
            }

            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
        };
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("(?!/error).*"))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "首义感知树洞API文档",
                "Api Documentation",
                "V1.5",
                "urn:tos",
                new Contact("SQ", "http://81.70.47.134", "wsq2001@outlook.com"),
                "Apache 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}
