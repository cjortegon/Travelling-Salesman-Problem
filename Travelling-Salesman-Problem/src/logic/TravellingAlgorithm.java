package logic;

import java.util.ArrayList;

import graph.Graph;
import graph.Node;

public class TravellingAlgorithm {

	private ArrayList<double[]> appointments;
	private Graph graph;
	private int minimumDistance;

	public TravellingAlgorithm() {
		minimumDistance = 800;
		appointments = new ArrayList<>();
	}

	public void addAppointment(double longitude, double latitude) {

		// >>>>> Convert longitude and latitude to meters

		appointments.add(new double[]{longitude,latitude});
	}

	public void generateRoute() {

		graph = new Graph();

		// Creating nodes
		for (int i = 0; i < appointments.size(); i++) {
			graph.nodes.add(new Node(appointments.get(i)[0], appointments.get(i)[1], i));
		}

		// Creating edges between close nodes
		createEdgesBetweenNodes();

		// Creating routes
		ArrayList<Route> routes = new ArrayList<>();
		generateTreeOfRoutes(routes, new Route(appointments.size(), graph.nodes.get(0)));

		// Printing route
		System.out.println("-- Routes --");
		for (int i = 0; i < routes.size(); i++) {
			System.out.println(routes.get(i));
		}

	}

	/**
	 */
	private void createEdgesBetweenNodes() {

		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {

				Node node1 = graph.nodes.get(i);
				Node node2 = graph.nodes.get(j);

				// Checking if 2 nodes are close enough

				double distance = Math.sqrt(Math.pow(node1.longitude-node2.longitude, 2)+Math.pow(node1.latitude-node2.latitude, 2));
				if(distance < minimumDistance) {
					node1.adjacencies.add(node2);
					node2.adjacencies.add(node1);
				}
			}
		}

		// Checking if everyone (nodes) has at least two connections
		for (int i = 0; i < graph.nodes.size(); i++) {
			while (graph.nodes.get(i).adjacencies.size() < 2) {
				double minDst = Double.MAX_VALUE;
				Node newConnection = null;
				for (int j = 0; j < graph.nodes.size(); j++) {
					if(i != j && !graph.nodes.get(i).adjacencies.contains(graph.nodes.get(j))) {
						double dst = Math.sqrt(Math.pow(graph.nodes.get(i).longitude-graph.nodes.get(j).longitude, 2) + Math.pow(graph.nodes.get(i).latitude-graph.nodes.get(j).latitude, 2));
						if(dst < minDst){
							newConnection = graph.nodes.get(j);
							minDst = dst;
						}
					}
				}
				graph.nodes.get(i).adjacencies.add(newConnection);
			}
		}
	}

	private void generateTreeOfRoutes(ArrayList<Route> routes, Route route) {

		if(route.isComplete()) {
			return;
		} else {

			// Getting new routes
			ArrayList<Route> newRoutes = route.continueRoute();
			routes.addAll(newRoutes);
			routes.remove(route);

			// Recursive
			for (int i = 0; i < newRoutes.size(); i++) {
				generateTreeOfRoutes(routes, newRoutes.get(i));
			}
		}

	}



}
