package de.infinit.spring.boot.autoconfigure.wro4j;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ro.isdc.wro.config.jmx.ConfigConstants;
import ro.isdc.wro.http.ConfigurableWroFilter;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.manager.factory.WroManagerFactory;
import ro.isdc.wro.model.group.GroupExtractor;
import ro.isdc.wro.model.resource.locator.factory.ConfigurableLocatorFactory;
import ro.isdc.wro.model.resource.processor.factory.ConfigurableProcessorsFactory;
import ro.isdc.wro.model.resource.support.hash.ConfigurableHashStrategy;
import ro.isdc.wro.model.resource.support.naming.ConfigurableNamingStrategy;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.ThymeleafWro4jDialectConfiguration;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.EnhancedGroupExtractor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.GroupPerFileGroupExtractor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.GroupPerFileModelTransformer;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;

@Configuration
@EnableConfigurationProperties(Wro4jProperties.class)
@Import(ThymeleafWro4jDialectConfiguration.class)
public class Wro4jConfig {

    @Autowired
    Wro4jProperties wro4jProperties;

    @Bean
    ConfigurableWroFilter wroFilter(WroManagerFactory wroManagerFactory) {
        ConfigurableWroFilter wroFilter = new ConfigurableWroFilter();
        wroFilter.setProperties(wroFilterProperties());
        wroFilter.setWroManagerFactory(wroManagerFactory);
        return wroFilter;
    }

    Properties wroFilterProperties() {
        Properties properties = new Properties();
        properties.put(ConfigConstants.debug.name(), String.valueOf(wro4jProperties.isDebug()));
        properties.put(ConfigConstants.disableCache.name(), String.valueOf(wro4jProperties.isDisableCache()));
        properties.put(ConfigConstants.cacheUpdatePeriod, String.valueOf(wro4jProperties.getCacheUpdatePeriod()));
        properties.put(ConfigConstants.resourceWatcherUpdatePeriod, String.valueOf(wro4jProperties.getResourceWatcherUpdatePeriod()));
        properties.put(ConfigConstants.cacheGzippedContent, String.valueOf(wro4jProperties.isCacheGzippedContent()));
        properties.put(ConfigConstants.parallelPreprocessing, String.valueOf(wro4jProperties.isParallelProcessing()));
        return properties;
    }

    @Bean
    FilterRegistrationBean wro4jFilterRegistration(ConfigurableWroFilter wroFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(wroFilter);
        filterRegistrationBean.addUrlPatterns(wro4jProperties.getUrlPattern());
        return filterRegistrationBean;
    }

    @ConditionalOnClass(name="groovy.lang.GroovyObject")
    @ConditionalOnMissingBean(WroManagerFactory.class)
    @Bean
    WroManagerFactory groovyWroManagerFactory(GroupExtractor groupExtractor) {
    	GroovyWroManagerFactory wmf = new GroovyWroManagerFactory(wro4jProperties.getGroovyResourceName(), wroManagerFactoryProperties());
    	configureWroManagerFactory(wmf, groupExtractor);
        return wmf;
    }

    @ConditionalOnMissingBean(WroManagerFactory.class)
    @Bean
    WroManagerFactory fallbackWroManagerFactory(GroupExtractor groupExtractor) {
    	XmlWroManagerFactory wmf = new XmlWroManagerFactory(wro4jProperties.getXmlResourceName(), wroManagerFactoryProperties());
        configureWroManagerFactory(wmf, groupExtractor);
        return wmf;
    }

    protected void configureWroManagerFactory(ConfigurableWroManagerFactory wmf, GroupExtractor groupExtractor){
    	if(wro4jProperties.isDevelopment()) {
        	wmf.addModelTransformer(new GroupPerFileModelTransformer());
		}
    	wmf.setGroupExtractor(groupExtractor);
    }
    
    @ConditionalOnMissingBean(GroupExtractor.class)
	@Bean
	public GroupExtractor groupExtractor(Wro4jProperties wro4jProperties, WroDeliveryConfiguration wroDeliveryConfiguration) {
		if(wro4jProperties.isDevelopment()) {
			return new GroupPerFileGroupExtractor(wroDeliveryConfiguration);
		} else {
			return new EnhancedGroupExtractor();
		}
	}
    
    Properties wroManagerFactoryProperties() {
        Properties properties = new Properties();
        properties.put(ConfigurableLocatorFactory.PARAM_URI_LOCATORS, wro4jProperties.getUriLocators());
        properties.put(ConfigurableProcessorsFactory.PARAM_PRE_PROCESSORS, wro4jProperties.getPreProcessors());
        properties.put(ConfigurableProcessorsFactory.PARAM_POST_PROCESSORS, wro4jProperties.getPostProcessors());
        properties.put(ConfigurableNamingStrategy.KEY, wro4jProperties.getNamingStrategy());
        properties.put(ConfigurableHashStrategy.KEY, wro4jProperties.getHashStrategy());
        return properties;
    }

}
