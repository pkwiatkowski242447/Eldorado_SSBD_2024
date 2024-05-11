package pl.lodz.p.it.ssbd2024.ssbd03.config.webconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan({"pl.lodz.p.it.ssbd2024.ssbd03", "org.springdoc"})
@PropertySource(value = {
        "classpath:application.properties",
        "classpath:properties/mail.properties",
        "classpath:properties/urls.properties",
        "classpath:properties/consts.properties"
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
        converters.add(new StringHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter(new ObjectMapper()));
    }
}
