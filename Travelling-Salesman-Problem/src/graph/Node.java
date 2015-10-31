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

}
