package graph;

import java.util.ArrayList;

public class Node {

	public double longitude, latitude;
	public int id;
	public ArrayList<Node> adjacencies;

	public Node(double longitude, double latitude, int id) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.id = id;
		this.adjacencies = new ArrayList<>();
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
