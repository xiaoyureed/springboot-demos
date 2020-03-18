/*
 * Copyright (c) 2018. Lemon tree lemon orz
 */

package io.github.xiaoyureed.config;

import freemarker.ext.servlet.FreemarkerServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @auther: xiaoyu
 * @date: 2018/10/29 15:27
 * @description: freemarker config servlet, 优先级最高. - https://freemarker.apache.org/docs/api/freemarker/ext/servlet/FreemarkerServlet.html
 */
@WebServlet(urlPatterns = {"*.ftl"}, name = "freemarkerTemplateController", loadOnStartup = 0, initParams = {
        @WebInitParam(name = "TemplatePath", value = "classpath:/templates"),
        @WebInitParam(name = "NoCache", value = "true"),
        //@WebInitParam(name = "ContentType", value = "text/html;charset=UTF-8"),// 内容类型
        @WebInitParam(name = "template_update_delay", value = "0"), // 开发环境中可设置为0, 正式环境可以是 30s ...
        @WebInitParam(name = "default_encoding", value = "UTF-8"),//The encoding of the template files
        @WebInitParam(name = "ResponseCharacterEncoding", value = "fromTemplate"),//Use the output_encoding setting of FreeMarker
        @WebInitParam(name = "output_encoding", value = "UTF-8"),
        @WebInitParam(name = "number_format", value = "0.##########"),
        @WebInitParam(name = "ExceptionOnMissingTemplate", value = "true"),//true => HTTP 500 on missing template, instead of HTTP 404
        @WebInitParam(name = "locale", value = "en_US"), //Influences number and date/time formatting
})
public class FreemarkerTemplateControllerServlet extends FreemarkerServlet {
}







