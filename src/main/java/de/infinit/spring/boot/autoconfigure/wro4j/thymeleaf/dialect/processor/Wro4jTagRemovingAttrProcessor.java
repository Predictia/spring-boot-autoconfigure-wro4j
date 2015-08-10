package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

/**
 * 
 * This class is heavily inspired by Miloš Milivojević Jawr Thymeleaf extension.
 * 
 * A wrapper class for wro4j processors that removes the attribute's enclosing
 * tag after using the provided wro4j processor to render the tag's content.
 * 
 * @author Miloš Milivojević
 * 
 */
public class Wro4jTagRemovingAttrProcessor extends AbstractAttrProcessor {
	private final AbstractWro4jAttrProcessor processor;

	public Wro4jTagRemovingAttrProcessor(
			final AbstractWro4jAttrProcessor p_processor) {
		super((IAttributeNameProcessorMatcher) p_processor.getMatcher());
		processor = p_processor;
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public ProcessorResult processAttribute(final Arguments p_arguments,
			final Element p_element, final String p_attributeName) {
		processor.processAttribute(p_arguments, p_element, p_attributeName);
		
		
		final NestableNode parent = p_element.getParent();

		for (final Node child : p_element.getChildren()) {
			p_element.removeChild(child);
			parent.insertBefore(p_element, child);
		}

		parent.removeChild(p_element);
		return ProcessorResult.OK;
	}

}