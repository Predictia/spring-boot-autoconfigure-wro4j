package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;

import ro.isdc.wro.model.resource.ResourceType;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroContextSupport;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroModelAccessor;

public abstract class AbstractWro4jAttrProcessor extends AbstractUnescapedTextChildModifierAttrProcessor {

	private static final int PRECEDENCE = 900;
	private final static int ASSUMED_URI_LENGTH_PER_RESOURCE = 75;
	
	private final ResourceType resourceType = getWro4jResourceType();
	
	private final WroDeliveryConfiguration wroDeliveryConfiguration;
	private final WroContextSupport wroContextSupport;

	public AbstractWro4jAttrProcessor(WroDeliveryConfiguration wroDeliveryConfiguration, WroContextSupport wroContextSupport, IAttributeNameProcessorMatcher matcher) {
		super(matcher);
		this.wroDeliveryConfiguration = wroDeliveryConfiguration;
		this.wroContextSupport = wroContextSupport;
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected final String getText(final Arguments arguments, final Element element, final String attributeName) {
		if(! (arguments.getContext() instanceof IWebContext)) {
			throw new IllegalArgumentException("Thymeleaf context is of unsupported type " + arguments.getContext().getClass().getName());
		}

		final HttpServletRequest request = ((IWebContext) arguments.getContext()).getHttpServletRequest();
		final HttpServletResponse response = ((IWebContext) arguments.getContext()).getHttpServletResponse();
		final WroModelAccessor wro4jModelUtil = new WroModelAccessor(request, response, wroDeliveryConfiguration, wroContextSupport);
		
		final String requestedGroupname = element.getAttributeValue(attributeName);
		final List<String> versionedUris = wro4jModelUtil.resources(requestedGroupname, resourceType);
		
		final StringBuilder buffer = new StringBuilder(ASSUMED_URI_LENGTH_PER_RESOURCE);
		for(final String uri : versionedUris) {
			buffer.append( getRenderedAttribute(uri, wroDeliveryConfiguration.isDevelopment()) );
		}
		return buffer.toString();
	}

	/**
	 * Get the HTML for the requested URI
	 * 
	 * @param uri
	 * @param isDevelopment
	 * @return
	 */
	protected abstract String getRenderedAttribute(String uri, boolean isDevelopment);

	/**
	 * Specify the {@link ResourceType} of this implementation
	 * 
	 * @return
	 */
	protected abstract ResourceType getWro4jResourceType();
}