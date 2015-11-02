package graph;

import java.util.ArrayList;

public class Graph {

	public ArrayList<Node> nodes;

	public Graph() {
		this.nodes = new ArrayList<>();
	}

	@Override
	public String toString() {
		String toString = "";
		for (int i = 0; i < nodes.size(); i++) {
			toString += nodes.get(i) + "\n";
		}
		return toString;
	}
	
}
