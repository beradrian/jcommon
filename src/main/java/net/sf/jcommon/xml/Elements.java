package net.sf.jcommon.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

/**
 * Some utility methods for working with XML elements.
 */
public class Elements {

    /** Use only the static methods. */
    private Elements() {
    }
    
    private static class IterableNodeList implements Iterable<Node> {
    	private NodeList nodes;
    	
    	public IterableNodeList(NodeList nodes) {
    		this.nodes = nodes;
    	}

    	@Override
    	public Iterator<Node> iterator() {
    		return new NodeListIterator(nodes);
    	}
    }
    
    /**
     * An iterator through a list of XML nodes. At creation it will receive a list of nodes and it will act as an iterator
     * through them.
     */
    private static class NodeListIterator implements Iterator<Node> {

        /** The iterated node list */
        private NodeList nodeList;
        /** The current index. */
        private int index = 0;

        public NodeListIterator(NodeList nodeList) {
            this.nodeList = nodeList;
        }

        public void remove() {
            Node oldNode = nodeList.item(index);
            oldNode.getParentNode().removeChild(oldNode);
        }

        public boolean hasNext() {
            return index < nodeList.getLength();
        }

        public Node next() {
            if (hasNext()) {
                return nodeList.item(index++);
            }
            throw new NoSuchElementException();
        }
    }


    public static Iterable<Node> asIterable(NodeList nodeList) {
    	return new IterableNodeList(nodeList);
    }
    
    public static Iterator<Node> asIterator(NodeList nodeList) {
    	return new NodeListIterator(nodeList);
    }
    
    public static Iterator<Node> childrenAsIterator(Element e) {
    	return asIterator(e.getChildNodes());
    }
    
    public static Iterable<Node> childrenAsIterable(Element e) {
    	return asIterable(e.getChildNodes());
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
        for (Iterator<Node> it = childrenAsIterator(element); it.hasNext();) {
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