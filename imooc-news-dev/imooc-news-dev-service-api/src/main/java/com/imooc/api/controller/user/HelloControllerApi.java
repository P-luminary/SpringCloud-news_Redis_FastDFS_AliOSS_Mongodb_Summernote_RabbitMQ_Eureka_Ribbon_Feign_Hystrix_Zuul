package com.imooc.api.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "controller的標題",tags = {"xx功能的Controller"})
public interface HelloControllerApi {
    /**
     * api的作用：
     * api就相当于企业的领导,老板,部门经理
     * 其他的服务层都是实现,他们就相当于员工,只做事情
     * 老板(开发人员)来看一下每个人(服务)的进度,做什么事
     * 老板不会去问员工,他只会对接部门经理
     * 这里所有的api接口就是统一在这里管理和调度的,微服务也如此
     */

    /**
     * 运作：
     * 现在的所有接口都在此暴露,实现都是在各自的微服务中
     * 本项目只写项目,不写实现,实现在各自的微服务工程中,因为以业务来划分的微服务有很多
     * Controller也会分散在各个微服务工程中,一旦多了就很难统一管理和查看
     *
     * 其次,微服务之间的调用都是基于接口的
     * 如果不这样做,微服务之间的调用就需要互相依赖了
     * 耦合对也就很高,接口的目的是为了能够提供解耦
     *
     * 此外,本项目的接口其实就是一套规范.实现都是由各自的工程去做的处理
     * 目前我们使用springboot作为接口的实现的
     * 如果未来以后出现新的java web框架,那么我们不需要修改接口
     * 只需要去修改对应的实现就可以了,这其实也是解耦的一个体现
     *
     * Swagger2, 基于接口的自动文档生成
     * 所有的配置文件只需要一份,就能再当前项目中去构建了
     * 管理起来很方便
     *
     * 综上所述,这样做法可以提高多服务的项目可扩展性
     */
    @ApiOperation(value = "hello方法的接口",notes = "hello方法的接口",httpMethod = "GET")
    @GetMapping("/hello")
    public Object hello();
}
