package com.ljm.copang;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String [] patterns = {"/", "/members/new", "/login", "/logout",
							  "/css/**", "/*.ico", "/error", "/items/{itemId}"};
		
		registry.addInterceptor(new LoginCheckInterceptor())
				.order(1).addPathPatterns("/**")
				.excludePathPatterns(patterns);
	}
	
}
