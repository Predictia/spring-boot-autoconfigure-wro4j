package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.dialect.IDialect;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.http.support.ServletContextAttributeHelper;
import de.infinit.spring.boot.autoconfigure.wro4j.Wro4jProperties;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.Wro4jDialect;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.IWroModelAccessor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroContextSupport;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroModelAccessor;

/**
 * Adds {@link Wro4jDialect} to Thymeleafs configuration
 * 
 * @see WroDeliveryConfiguration
 * @author pgaschuetz
 * 
 */
@Configuration
@ConditionalOnClass(name="org.thymeleaf.dialect.IDialect")
public class ThymeleafWro4jDialectConfiguration {
	
	@Bean
	public IDialect wro4jDialect(WroDeliveryConfiguration wroDeliveryConfiguration, WroContextSupport wroContextSupport){
		return new Wro4jDialect(wroDeliveryConfiguration, wroContextSupport);
	}
	
	@Bean
	public WroDeliveryConfiguration wroDeliveryConfiguration(Wro4jProperties wro4jProperties){
		WroDeliveryConfiguration wdc = new WroDeliveryConfiguration();
		wdc.setDevelopment(wro4jProperties.isDevelopment());
		wdc.setUriPrefix(wro4jProperties.getUrlPattern().replace("*", ""));
		return wdc;
	}
	
	/**
	 * {@link WroContextSupport} is a support class, that allows to execute code
	 * within a Wro {@link Context}, which is needed to access the wro
	 * infrastructure from ie. within in a JSP tag. {@link WroContextSupport}
	 * eliminates the need to install a {@link Filter ServletFilter} on <code>/*</code>
	 * 
	 * @return {@link WroContextSupport}
	 */
	@Bean
	public WroContextSupport wroContextSupport() {
		return new WroContextSupport();
	}
	
	@Bean
	public ServletContextAttributeHelper servletContextAttributeHelper(ServletContext servletContext) {
		return new ServletContextAttributeHelper(servletContext);
	}
	
	@Bean
	@Scope(value=WebApplicationContext.SCOPE_REQUEST, proxyMode=ScopedProxyMode.INTERFACES)
	public IWroModelAccessor wroModelUtility() {
		return new WroModelAccessor();
	}
	
}
