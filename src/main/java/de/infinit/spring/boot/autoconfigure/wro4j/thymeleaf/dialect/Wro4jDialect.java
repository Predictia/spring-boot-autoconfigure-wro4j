package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractXHTMLEnabledDialect;
import org.thymeleaf.doctype.translation.IDocTypeTranslation;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;

import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor.Wro4jScriptAttrProcessor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor.Wro4jStyleAttrProcessor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.dialect.processor.Wro4jTagRemovingAttrProcessor;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroContextSupport;
import de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support.WroDeliveryConfiguration;

/**
 * Wro4JDialect - heavily inspired by Miloš JawrDialect
 */
public class Wro4jDialect extends AbstractXHTMLEnabledDialect {

	public static final String PREFIX = "wro4j";

	private final WroDeliveryConfiguration wroDeliveryConfiguration;
	private final WroContextSupport wroContextSupport;
	
	public Wro4jDialect(WroDeliveryConfiguration wroDeliveryConfiguration, WroContextSupport wroContextSupport) {
		this.wroDeliveryConfiguration = wroDeliveryConfiguration;
		this.wroContextSupport = wroContextSupport;
	}

	@Override
	public final String getPrefix() {
		return PREFIX;
	}

	@Override
	public final boolean isLenient() {
		return true;
	}

	@Override
	public final Set<IDocTypeTranslation> getDocTypeTranslations() {
		return SpringStandardDialect.SPRING4_DOC_TYPE_TRANSLATIONS;
	}

	@Override
	public final Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new Wro4jTagRemovingAttrProcessor(new Wro4jScriptAttrProcessor(wroDeliveryConfiguration, wroContextSupport)));
		processors.add(new Wro4jTagRemovingAttrProcessor(new Wro4jStyleAttrProcessor(wroDeliveryConfiguration, wroContextSupport)));
		return processors;
	}
}
