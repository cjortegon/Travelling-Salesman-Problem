package graph;

public class Edge {

	public Node tail, head;
	public int weight;

	public Edge(Node tail, Node head, int weight) {
		this.tail = tail;
		this.head = head;
		this.weight = weight;
	}

}
