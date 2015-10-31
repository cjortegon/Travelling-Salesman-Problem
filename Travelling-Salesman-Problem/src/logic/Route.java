package logic;

import java.util.ArrayList;

import graph.Node;

public class Route {

	private boolean contains[];
	private ArrayList<Node> nodes;

	public Route(int numberOfNodes, Node first) {
		this.contains = new boolean[numberOfNodes];
		this.nodes = new ArrayList<>();
		this.nodes.add(first);
		setContains();
	}

	public Route(int numberOfNodes, ArrayList<Node> nodes, Node next) {
		this.contains = new boolean[numberOfNodes];
		this.nodes = new ArrayList<>();
		this.nodes.addAll(nodes);
		this.nodes.add(next);
		setContains();
	}
	
	private void setContains() {
		for (int i = 0; i < nodes.size(); i++) {
			this.contains[nodes.get(i).id] = true;
		}
	}

	public boolean isComplete() {
		for (int i = 0; i < contains.length; i++) {
			if(contains[i]==false){
				return false;
			}
		}
		return true;
	}

	public ArrayList<Route> continueRoute() {
		
		ArrayList<Route> routes = new ArrayList<>();

		Node lastNode = nodes.get(nodes.size()-1);
		for (int i = 0; i < lastNode.adjacencies.size(); i++) {
			if(!contains[lastNode.adjacencies.get(i).id]) {
				Route route = new Route(contains.length, nodes, lastNode.adjacencies.get(i));
				routes.add(route);
			}
		}
		
		return routes;

	}

	@Override
	public String toString() {
		
		String route = "";
		for (int i = 0; i < nodes.size(); i++) {
			route += nodes.get(i).id;
			if(i != nodes.size() - 1)
				route += ", ";
		}
		
		return "Route {"+route+"}";
	}

}
