package com.sorawingwind.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName Swagger2Config
 * @description: swagger2的配置
 * @author: ygj
 * @date: 2022-03-28 10:43
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * @Api 类/接口	描述类/接口主要用途
     * @ApiOperation 方法    描述方法的用途
     * @ApiImplicitParam 方法    用于描述接口的非对象参数
     * @ApiImplicitParams 方法    用于描述接口的非对象参数集
     * @ApiIgnore 类/方法/参数	Swagger 文档不会显示拥有该注解的接口
     * @ApiModel 参数实体类    可设置接口相关实体的描述
     * @ApiModelProperty	参数实体类属性	可设置实体属性的相关描述
     *
     */

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cotte.estatemall.controller"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("便利店管理")
                .version("1.0.0")
                .build();
    }

}
