package com.jamesx.config;

import com.jamesx.domain.*;
import com.jamesx.service.generic.GenericServiceImpl;
import com.jamesx.service.generic.IGenericService;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**************************************************
 * By JamesXie 2016
 **************************************************/
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.jamesx")
@EnableJpaRepositories("com.jamesx")
public class WebAppConfig extends WebMvcConfigurerAdapter {

	//<editor-fold defaultstate="collapsed" desc="Service Bean Definition">
    //Don't delete the following line !!!
    /**<--Bean Injection here-->**/

    @Bean public IGenericService<Category> categoryService(){return new GenericServiceImpl<Category>(Category.class);}

    @Bean public IGenericService<Orders> ordersService(){return new GenericServiceImpl<Orders>(Orders.class);}

    @Bean public IGenericService<Employees> employeesService(){return new GenericServiceImpl<Employees>(Employees.class);}

    @Bean public IGenericService<Contacts> contactsService(){return new GenericServiceImpl<Contacts>(Contacts.class);}

    //</editor-fold>

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		//<editor-fold defaultstate="collapsed" desc="setting for SQL Server Connection">
//        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        dataSource.setUrl("jdbc:sqlserver://localhost\\jim_sql;databaseName=hynet");
//        dataSource.setUsername("hynet");
//        dataSource.setPassword("hynet"); //</editor-fold
		// connection setting for mySQL
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/james");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.setPackagesToScan(new String[] { "com.jamesx.domain" });
		entityManagerFactoryBean.setJpaProperties(hibernateProperties());
		return entityManagerFactoryBean;
	}

    // set hibernate settings
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.format_sql", "true");
		return properties;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/view/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("..........................add resource handler...");
		registry.addResourceHandler("/content/**").addResourceLocations("/content/");
		registry.addResourceHandler("/content/js/**").addResourceLocations("/content/js/");
		//registry.addResourceHandler("/WEB-INF/view/**").addResourceLocations("/WEB-INF/view/");
		//registry.addResourceHandler("/WEB-INF/view/employee/**").addResourceLocations("/WEB-INF/view/employee/");
		//registry.addResourceHandler("/WEB-INF/view/dept/**").addResourceLocations("/WEB-INF/view/dept/");

	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource getMessageSource() {
		System.out.println(".........................Creating Bean messageSource...");
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasename("/WEB-INF/resource/messages");
		resource.setDefaultEncoding("UTF-8");
		return resource;
	}

}