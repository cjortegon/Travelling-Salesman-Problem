package graph;

import java.util.ArrayList;

import logic.Maps;

public class PaintedNode {

	public Node node;
	public int numberOfPartitions;
	public int[] colors;

	public PaintedNode(Node node, int colors[], int numberOfPartitions) {
		this.node = node;
		this.colors = colors;
		this.numberOfPartitions = numberOfPartitions;
	}

	public void createEdgeToRemovePartition(ArrayList<Node> nodes, boolean convertDegrees) {
		Node edge[] = null;
		double minDistance = Double.MAX_VALUE;
		for (int i = 0; i < nodes.size(); i++) {
			if(colors[i] != -1) {
				Node node1 = nodes.get(i);
				for (int j = 0; j < nodes.size(); j++) {
					if(colors[j] != -1) {
						Node node2 = nodes.get(j);
						double d = Maps.distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
						if(i != j && colors[i] != colors[j] && d < minDistance) {
							minDistance = d;
							edge = new Node[]{node1, node2};
						}
					}
				}
			}
		}
		edge[0].addNode(edge[1]);
		edge[1].addNode(edge[0]);
	}
}
