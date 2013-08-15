package net.sf.jcommon.xml;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class IterableNodeList implements Iterable<Node> {
	private NodeList nodes;
	
	public IterableNodeList(NodeList nodes) {
		this.nodes = nodes;
	}

	@Override
	public Iterator<Node> iterator() {
		return new NodeListIterator(nodes);
	}
}