package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor;

import org.thymeleaf.processor.AttributeNameProcessorMatcher;

import ro.isdc.wro.model.resource.ResourceType;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroContextSupport;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;


public class Wro4jScriptAttrProcessor extends AbstractWro4jAttrProcessor {

	public static final String ATTR_NAME = "script";
	private static final AttributeNameProcessorMatcher matcher = new AttributeNameProcessorMatcher(ATTR_NAME, ATTR_NAME);

	public Wro4jScriptAttrProcessor(WroDeliveryConfiguration wroDeliveryConfiguration, WroContextSupport wroContextSupport) {
		super(wroDeliveryConfiguration, wroContextSupport, matcher);
	}

	@Override
	protected String getRenderedAttribute(String uri, boolean isDevelopment) {
		return "<script src=\"" + uri + "\"></script>\n";
	}

	@Override
	protected ResourceType getWro4jResourceType() {
		return ResourceType.JS;
	}
}