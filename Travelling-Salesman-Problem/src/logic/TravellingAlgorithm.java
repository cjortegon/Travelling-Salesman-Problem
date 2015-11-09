package logic;

import java.util.ArrayList;

import graph.Graph;
import graph.Node;
import graph.PaintedNode;

public class TravellingAlgorithm {

	private ArrayList<double[]> appointments;
	private Graph graph;
	private double minimumDistance;
	private boolean convetDegreesToMeters, modifiedGraph;

	public TravellingAlgorithm(boolean convetDegreesToMeters, double minimumDistance) {
		this.minimumDistance = minimumDistance;
		this.appointments = new ArrayList<>();
		this.convetDegreesToMeters = convetDegreesToMeters;
	}

	public void addAppointment(double latitude, double longitude) {
		appointments.add(new double[]{latitude, longitude});
		modifiedGraph = true;
	}

	public void generateRoute() {
		generateRoute(null);
	}

	public Graph startGraph() {
		if(modifiedGraph) {
			graph = new Graph();
			for (int i = 0; i < appointments.size(); i++) {
				graph.nodes.add(new Node(appointments.get(i)[0], appointments.get(i)[1], i));
			}
			modifiedGraph = false;
		}
		return graph;
	}

	public void generateRoute(double distances[][]) {

		// Creating nodes
		startGraph();

		// Creating distances matrix based on geografical position if the parameter is null
		if(distances == null)
			distances = getGeograficalDistances();

		// Creating edges between close nodes
		createEdgesBetweenNodes(distances);

		// Solving the problem of divided groups
		ArrayList<Node>[] groups = graph.getGroups();
		//		while(groups.lenth > 1) {
		//			numberOfGroups = graph.countGroups();
		//		}

		// Solving the bowtie problem
		ArrayList<PaintedNode> partitionNodes = graph.getNodesColoredWithPartitions();
		//		while(partitionNodes.size() > 0) {
		//			partitionNodes = graph.getNodesColoredWithPartitions();
		//		}

		// Printing graph
		System.out.println("-- Contectivity graph -- ("+groups.length+(groups.length == 1 ? " group) (" : " groups) (")
				+partitionNodes.size()+" partition nodes)");
		System.out.println(graph);

		// Creating routes
		ArrayList<Route> routes = new ArrayList<>();
		generateTreeOfRoutes(routes, new Route(appointments.size(), graph.nodes.get(0)));

		// Printing route
		System.out.println("-- Routes --");
		for (int i = 0; i < routes.size(); i++)
			System.out.println(routes.get(i));
	}

	private double[][] getGeograficalDistances() {
		double distances[][] = new double[graph.nodes.size()][graph.nodes.size()];
		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {
				Node node1 = graph.nodes.get(i);
				Node node2 = graph.nodes.get(j);
				distances[i][j] = Maps.distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
				distances[j][i] = distances[i][j];
			}
		}
		return distances;
	}

	/**
	 */
	private void createEdgesBetweenNodes(double distances[][]) {

		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {

				// Checking if 2 nodes are close enough
				if(distances[i][j] < minimumDistance) {
					graph.nodes.get(i).adjacencies.add(graph.nodes.get(j));
					graph.nodes.get(j).adjacencies.add(graph.nodes.get(i));
				}

				// Geografical based distance
				//				Node node1 = graph.nodes.get(i);
				//				Node node2 = graph.nodes.get(j);
				//				double distance = Math.sqrt(Math.pow(node1.longitude-node2.longitude, 2)+Math.pow(node1.latitude-node2.latitude, 2));
				//				if(distance < minimumDistance) {
				//					node1.adjacencies.add(node2);
				//					node2.adjacencies.add(node1);
				//				}
			}
		}

		// Checking if everyone (nodes) has at least two connections
		for (int i = 0; i < graph.nodes.size(); i++) {
			while (graph.nodes.get(i).adjacencies.size() < 3) {
				double minDst = Double.MAX_VALUE;
				Node newConnection = null;
				for (int j = 0; j < graph.nodes.size(); j++) {
					if(i != j && !graph.nodes.get(i).adjacencies.contains(graph.nodes.get(j))) {
						Node node1 = graph.nodes.get(i);
						Node node2 = graph.nodes.get(j);
						double dst = Maps.distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
						if(dst < minDst) {
							newConnection = graph.nodes.get(j);
							minDst = dst;
						}
					}
				}
				if(newConnection == null) {
					break;
				} else {
					graph.nodes.get(i).adjacencies.add(newConnection);
				}
			}
		}
	}

	private void generateTreeOfRoutes(ArrayList<Route> routes, Route route) {

		if(route.isComplete()) {
			return;
		} else {

			// Getting new routes
			ArrayList<Route> newRoutes = route.continueRoute();

			// Printing temporal routes
			//			for (int i = 0; i < newRoutes.size(); i++)
			//				System.out.println(newRoutes.get(i));

			routes.addAll(newRoutes);
			routes.remove(route);

			// Recursive
			for (int i = 0; i < newRoutes.size(); i++)
				generateTreeOfRoutes(routes, newRoutes.get(i));
		}

	}

}
