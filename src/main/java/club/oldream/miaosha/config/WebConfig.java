package club.oldream.miaosha.config;

import java.util.List;


import club.oldream.miaosha.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	UserArgumentResolver userArgumentResolver;
	@Autowired
	AccessInterceptor accessInterceptor;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
		resolvers.add(userArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessInterceptor);
	}

	
}
