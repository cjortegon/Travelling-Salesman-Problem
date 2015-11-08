package graph;

public class PaintedNode {

	public Node node;
	public int numberOfPartitions;
	public int[] colors;

	public PaintedNode(Node node, int colors[], int numberOfPartitions) {
		this.node = node;
		this.colors = colors;
		this.numberOfPartitions = numberOfPartitions;
	}

}
