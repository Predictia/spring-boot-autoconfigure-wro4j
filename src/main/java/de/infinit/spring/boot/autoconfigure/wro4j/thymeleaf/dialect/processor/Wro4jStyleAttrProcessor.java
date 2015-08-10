package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor;

import org.thymeleaf.processor.AttributeNameProcessorMatcher;

import ro.isdc.wro.model.resource.ResourceType;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroContextSupport;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;


public class Wro4jStyleAttrProcessor extends AbstractWro4jAttrProcessor {

	public static final String ATTR_NAME = "style";
	public static final String ELEM_NAME = "link";
	private final static AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(ATTR_NAME, ELEM_NAME);

	public Wro4jStyleAttrProcessor(WroDeliveryConfiguration wroDeliveryConfiguration, WroContextSupport wroContextSupport) {
		super(wroDeliveryConfiguration, wroContextSupport, matcher);
	}

	@Override
	protected String getRenderedAttribute(String uri, boolean isDevelopment) {
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + uri + "\"/>\n";
		
	}

	@Override
	protected ResourceType getWro4jResourceType() {
		return ResourceType.CSS;
	}
}