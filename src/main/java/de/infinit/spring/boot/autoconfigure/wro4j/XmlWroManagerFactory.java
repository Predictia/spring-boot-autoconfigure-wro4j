package de.infinit.spring.boot.autoconfigure.wro4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;

/**
 * Allows the usage of XML files as wro4j descriptors.
 */
public class XmlWroManagerFactory extends SimpleWroManagerFactory {

    private final String resourceName;

    public XmlWroManagerFactory(String resourceName, Properties configProperties) {
        super(configProperties);
        this.resourceName = resourceName;
    }

    @Override
    protected WroModelFactory newModelFactory() {
        return new XmlModelFactory() {
            @Override
            protected InputStream getModelResourceAsStream() throws IOException {
                return new ClassPathResource(resourceName).getInputStream();
            }
        };
    }

}
