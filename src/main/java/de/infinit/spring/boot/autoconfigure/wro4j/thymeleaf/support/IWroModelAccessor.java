package de.infinit.spring.boot.autoconfigure.wro4j.thymeleaf.support;

import java.util.List;

import ro.isdc.wro.model.resource.ResourceType;

/**
 * interface so that we can create a proxy.
 * 
 * @author pgaschuetz
 *
 */
public interface IWroModelAccessor {
	public List<String> resources(String groupname, ResourceType resourceType);
	public List<String> css(String groupname);
	public List<String> js(String groupname);
}
