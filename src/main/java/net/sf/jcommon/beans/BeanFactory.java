package net.sf.jcommon.beans;

import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jcommon.util.LookupContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BeanFactory implements LookupContainer<BeanTemplate> {
	
	private LookupContainer<?> parent;
	private Collection<BeanTemplate> beanTemplates = new HashSet<BeanTemplate>();
	
	public BeanFactory(LookupContainer<?> parent) {
		this.parent = parent;
	}
	
	public Object createInstance(String type) {
		BeanTemplate t = find(type);
		return (t == null ? null : t.createInstance(this));
	}

	@Override
	public BeanTemplate find(String id) {
		for (BeanTemplate template : beanTemplates) {
			if (id.equals(template.getId())) {
				return template;
			}
		}
		Object x = parent.find(id);
		if (x instanceof BeanTemplate) {
			return (BeanTemplate) x;
		}
		return null;
	}
	
	public void load(InputStream is) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(is, new BeanTemplateHandler());
        } catch (Throwable t) {
        } 

	}
	
	private class BeanTemplateHandler extends DefaultHandler {

		private BeanTemplate current;
		private Map<String, String> currentAttributes;
		private StringBuffer currentContent;
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			String name = qName;
			if ("bean".equals(name) && "template".equals(atts.getValue("scope"))) {
				current = new BeanTemplate(atts.getValue("id"), atts.getValue("class"));
				beanTemplates.add(current);
			} else if ("constructor-arg".equals(name) || "property".equals(name)) {
				for (int i = 0; i < atts.getLength(); i++) {
					currentAttributes.put(atts.getQName(i), atts.getValue(i));
				}
				currentContent = new StringBuffer();			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			String elementName = qName;
			if ("constructor-arg".equals(elementName)) {
				if (current != null) {
					Object value = currentContent != null ? currentContent != null : currentAttributes.get("value"); 
					current.getInitArguments().add(value);
				}
			} else if ("property".equals(elementName)) {
				String name = currentAttributes.get("name");
				Object value = currentContent != null ? currentContent != null : currentAttributes.get("value"); 
				if (current != null)
					current.getProperties().put(name, value);
			}
			currentContent = null;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (currentContent != null)
				currentContent.append(ch);
		}
		
	}
}
