package net.sf.jcommon.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Iterator;

/**
 * An iterator through the children of an XML element.
 */
public class ChildrenIterator implements Iterator<Node> {

    /** The currently returned node by the iterator. */
    private Node child;

    public ChildrenIterator(Element element) {
        child = element.getFirstChild();
    }

    public void remove() {
        Node oldChild = child;
        child = child.getNextSibling();
        oldChild.getParentNode().removeChild(oldChild);
    }

    public boolean hasNext() {
        return child != null;
    }

    public Node next() {
        Node retval = child;
    	if (child != null)
            child = child.getNextSibling();
        return retval;
    }
}
