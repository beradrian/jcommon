package net.sf.jcommon.xml;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator through a list of XML nodes. At creation it will receive a list of nodes and it will act as an iterator
 * through them.
 */
public class NodeListIterator implements Iterator<Node> {

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
