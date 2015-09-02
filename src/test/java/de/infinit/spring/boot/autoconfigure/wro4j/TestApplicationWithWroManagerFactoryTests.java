package de.infinit.spring.boot.autoconfigure.wro4j;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ro.isdc.wro.manager.factory.WroManagerFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplication.class, loader = SpringApplicationContextLoader.class)
public class TestApplicationWithWroManagerFactoryTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testContextLoads() throws Exception {
        assertNotNull("The TestWroManagerFactory should be present", applicationContext.getBean(WroManagerFactory.class));
    }

}
