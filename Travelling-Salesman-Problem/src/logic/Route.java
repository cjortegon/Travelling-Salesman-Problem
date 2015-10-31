package logic;

import java.util.ArrayList;

import graph.Node;

public class Route {

	private boolean contains[];
	public ArrayList<Node> nodes;
	
	public Route(int numberOfNodes, Node first) {
		contains = new boolean[numberOfNodes];
		nodes = new ArrayList<>();
		nodes.add(first);
	}
	
	public boolean isComplete() {
		for (int i = 0; i < contains.length; i++) {
			if(contains[i]==false){
				return false;
			}
		}
		return true;
	}
	
	public void continueRoute(ArrayList<Route> routes) {
		
		Node lastNode = nodes.get(nodes.size()-1);
		for (int i = 0; i < lastNode.adjacencies.size(); i++) {
			
		}
		
	}
	
}
