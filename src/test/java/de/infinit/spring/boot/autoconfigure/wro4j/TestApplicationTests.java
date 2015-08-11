package de.infinit.spring.boot.autoconfigure.wro4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ro.isdc.wro.http.ConfigurableWroFilter;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplication.class, loader = SpringApplicationContextLoader.class)
public class TestApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testBeansAreRegistered() throws Exception {
        assertNotNull("Groovy files should be allowed since Groovy is on the Classpath!", applicationContext.getBean(GroovyWroManagerFactory.class));
        assertNotNull("Filter should be registered", applicationContext.getBean(ConfigurableWroFilter.class));
    }

}
