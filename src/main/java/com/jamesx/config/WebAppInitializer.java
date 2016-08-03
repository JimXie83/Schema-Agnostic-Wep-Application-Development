package com.jamesx.config;//package com.jamesx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
/**************************************************
 * By JamesXie 2016
 **************************************************/
@Configuration
public class WebAppInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext container) {
		// Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherServlet = new AnnotationConfigWebApplicationContext();
		dispatcherServlet.register(WebAppConfig.class);
		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = container.addServlet(
				"mvc-dispatcher", new DispatcherServlet(dispatcherServlet));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
