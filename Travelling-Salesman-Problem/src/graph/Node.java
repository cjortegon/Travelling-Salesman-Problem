package graph;

import java.util.ArrayList;

public class Node {

	public double latitude, longitude;
	public int id;
	public Object objectId;
	public ArrayList<Node> adjacencies;

	public Node(double latitude, double longitude, int id, Object objectId) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.objectId = objectId;
		this.adjacencies = new ArrayList<>();
	}
	
	public void addNode(Node node) {
		if(!adjacencies.contains(node))
			adjacencies.add(node);
	}

	@Override
	public String toString() {

		String toString = "";
		for (int i = 0; i < adjacencies.size(); i++) {
			toString += adjacencies.get(i).id;
			if(i != adjacencies.size() - 1)
				toString += ", ";
		}

		return "Node "+id+" {"+toString+"}";
	}

}
