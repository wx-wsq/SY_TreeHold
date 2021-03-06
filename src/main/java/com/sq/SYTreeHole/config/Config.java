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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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

@Configuration
@EnableCaching
@EnableSwagger2
@EnableWebMvc
@EnableScheduling
@EnableTransactionManagement
@MapperScans({@MapperScan("com.sq.SYTreeHole.dao")})
public class Config {

    /**
     * MP????????????
     *
     * @return ????????????MP???????????????
     */
    @Bean
    public MybatisPlusInterceptor paginationInnerInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //???????????????
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //????????????????????????????????????
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //?????????
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * redis??????????????????
     *
     * @param redisConnectionFactory redis???????????????
     * @return ???????????????cache?????????
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofHours(24))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())))
                // ?????? Redis ????????????????????????/?????????????????????????????? Spring ????????????????????????
                .transactionAware()
                .build();
    }

    /**
     * ?????????redisTemplate
     *
     * @param redisConnectionFactory redis????????????
     * @return ???????????????redisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    /**
     * ???????????????
     * ??????????????????????????????bean
     *
     * @return ??????webMVC???????????????
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
//                registry
//                        .addInterceptor(new JwtInterceptor())
//                        .addPathPatterns("/**")
//                        .excludePathPatterns("/loginForPass", "/loginForCode", "/register", "/code", "/resetPassword","/images/**");
//                        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/swagger-ui.html", "/csrf", "/");
                registry.addInterceptor(new ErrorInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/swagger-ui.html");
            }

            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
                registry.addResourceHandler("/images/**").addResourceLocations("file:/usr/images/");
                registry.addResourceHandler("/head/**").addResourceLocations("file:/usr/images/heads");
            }
        };
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("SQ")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("(?!/error).*"))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfo(
                "??????????????????API??????",
                "Api Documentation",
                "V1.5",
                "urn:tos",
                new Contact("SQ", "http://81.70.47.134", "wsq2001@outlook.com"),
                "Apache 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}
