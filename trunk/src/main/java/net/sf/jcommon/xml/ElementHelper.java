package net.sf.jcommon.xml;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

/**
 * Some utility methods for working with XML elements.
 */
public class ElementHelper {

    /** Use only the static methods. */
    private ElementHelper() {
    }

    /** Removes blank string elements from element x and its children.
     * @param x the XML element
     */
    public static void removeBlankStrings(Element x) {
        removeBlankStrings(x, true);
    }

    /** Removes blank string elements from element x.
     * @param element the XML element
     * @param recursive if true the blank spaces are removed also from its children.
     */
    public static void removeBlankStrings(Element element, boolean recursive) {
        if (element == null) return;
        for (Iterator<Node> it = new ChildrenIterator(element); it.hasNext();) {
            Node child = it.next();
            if ((child instanceof Text) && (((Text) child).getTextContent().trim().length() <= 0)) {
                it.remove();
            }
            if (recursive && (child instanceof Element)) {
                removeBlankStrings((Element) child, true);
            }
        }
    }

    /** Returns the ancestor with the specified tag name of the given element.
     * @param e the element
     * @param name the ancestor tag name
     */
    public static Element getAncestorByTagName(Node e, String tagName) {
        Node x = e;
        while ((x.getParentNode() != null) && (!(x instanceof Element)) 
        		|| (!tagName.equals(((Element)x).getTagName()))) {
            x = x.getParentNode();
        }
        return (Element)x.getParentNode();
    }

    /** Returns the ancestor of the given element.
     * @param e the element
     */
    public static Node getAncestor(Node e) {
        Node x = e;
        while (x.getParentNode() != null) {
            x = x.getParentNode();
        }
        return x;
    }

    /** Replace a child in an element with a new element child.
     * @param parent the XML parent element
     * @param oldChild the XML old child element
     * @param newChild the XML new child element
     */
    public static void replaceChild(Node parent, Node oldChild, Node newChild) {
    	// TODO
    }

    /**
     * Returns an attribute value as a float.
     * @param elem the element
     * @param attrName the attribute name
     * @param defaultValue the default value returned if there is no attribute with that name 
     * or the value cannot be parsed
     * @return the value
     */
    public static float getAttributeAsFloat(Element elem, String attrName, float defaultValue) {
		String value = elem.getAttribute(attrName);
		if (value != null) {
			try {
				return Float.parseFloat(value);
			} catch(NumberFormatException exc) {}
		}
		return defaultValue;
    }

    /**
     * Returns an attribute value as an integer.
     * @param elem the element
     * @param attrName the attribute name
     * @param defaultValue the default value returned if there is no attribute with that name 
     * or the value cannot be parsed
     * @return the value
     */
    public static int getAttributeAsInt(Element elem, String attrName, int defaultValue) {
		String value = elem.getAttribute(attrName);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch(NumberFormatException exc) {}
		}
		return defaultValue;
    }

    /**
     * Returns an attribute value as a float.
     * @param elem the element
     * @param attrName the attribute name
     * @param defaultValue the default value returned if there is no attribute with that name 
     * or the value cannot be parsed
     * @return the value
     */
    public static float getAttributeAsFloat(Attributes attrs, String attrName, float defaultValue) {
		String value = attrs.getValue(attrName);
		if (value != null) {
			try {
				return Float.parseFloat(value);
			} catch(NumberFormatException exc) {}
		}
		return defaultValue;
    }

    /**
     * Returns an attribute value as an integer.
     * @param elem the element
     * @param attrName the attribute name
     * @param defaultValue the default value returned if there is no attribute with that name 
     * or the value cannot be parsed
     * @return the value
     */
    public static int getAttributeAsInt(Attributes attrs, String attrName, int defaultValue) {
		String value = attrs.getValue(attrName);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch(NumberFormatException exc) {}
		}
		return defaultValue;
    }
}