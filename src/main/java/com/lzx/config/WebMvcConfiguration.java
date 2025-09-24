package com.lzx.config;

import com.lzx.json.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * WebMvc 配置类
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Value("${zx.file.upload-path}")
    private String uploadPath;

    @Value("${zx.file.access-prefix}")
    private String accessPrefix;

    /**
     * 扩展消息转换器
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建一个新的消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 将所有日期格式都设置为 yyyy-MM-dd HH:mm:ss
        converter.setObjectMapper(new JacksonObjectMapper());
        // 将新的消息转换器添加到列表的开头
        converters.addFirst(converter);
    }

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将访问路径映射到本地文件目录
        registry.addResourceHandler(accessPrefix + "**")
                .addResourceLocations("file:" + uploadPath);
    }
}
