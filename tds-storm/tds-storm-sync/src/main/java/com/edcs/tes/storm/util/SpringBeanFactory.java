package com.edcs.tes.storm.util;

import java.net.URL;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

public class SpringBeanFactory extends DefaultListableBeanFactory {

	public static final String SPRING_BEAN_FACTORY_XML = "tds-calc-topology.xml";

	private transient final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);

	private String xml;
	
	public SpringBeanFactory() {
		super();
		try {
			Resource resource = reader.getResourceLoader().getResource(SPRING_BEAN_FACTORY_XML);
			URL url = resource.getURL();
			this.xml = Resources.toString(url, Charsets.UTF_8);
			this.reader.setValidating(false);
			this.reader.loadBeanDefinitions(resource);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

//	public SpringBeanFactory() {
//		this.xml = SPRING_BEAN_FACTORY_XML;
//		this.reader.setValidating(false);
//		this.reader.loadBeanDefinitions(new ByteArrayResource(xml.getBytes(Charsets.UTF_8)));
//	}

	public String getXml() {
		return xml;
	}

}
