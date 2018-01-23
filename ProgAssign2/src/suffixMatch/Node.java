package suffixMatch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private SuffixTree suffixTree;

    private Node suffixNode;
    private Map<Character, Edge> edges;
    private int name;

    public Node(Node node, Node suffixNode) {
        this(node.suffixTree, suffixNode);
    }

    public Node(SuffixTree suffixTree, Node suffixNode) {
        this.suffixTree = suffixTree;
        name = suffixTree.getNewNodeNumber();

        this.suffixNode = suffixNode;
        edges = new HashMap<Character, Edge>();
    }

    public char charAt(int index) {
        return suffixTree.getText().charAt(index);
    }

    public void addEdge(int charIndex, Edge edge) {
        edges.put(charAt(charIndex), edge);
    }

    public void removeEdge(int charIndex) {
        edges.remove(charAt(charIndex));
    }

    public Edge findEdge(char ch) {
        return edges.get(ch);
    }

    public Node getSuffixNode() {
        return suffixNode;
    }

    public void setSuffixNode(Node suffixNode) {
        this.suffixNode = suffixNode;
    }

    public Collection<Edge> getEdges() {
        return edges.values();
    }

    @Override
    public String toString() {
        return ((Integer) name).toString();
    }

    @Override
    public int hashCode() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;

        Node node = (Node) obj;
        return name == node.name;
    }
}

