package logic;

import java.util.ArrayList;

import graph.Graph;
import graph.Node;
import graph.PaintedNode;

public class TravellingAlgorithm {

	/**
	 * Input of spots in a map. Could be placed in meters or 
	 */
	private ArrayList<double[]> appointments;
	private Graph graph;
	private ArrayList<Route> routes;
	private boolean convetDegreesToMeters, modifiedGraph;

	public TravellingAlgorithm(boolean convetDegreesToMeters) {
		this.appointments = new ArrayList<>();
		this.convetDegreesToMeters = convetDegreesToMeters;
	}

	public void addAppointment(double latitude, double longitude, double duration) {
		appointments.add(new double[]{latitude, longitude, duration});
		modifiedGraph = true;
	}

	public void generateRoute() {
		generateRoute(null);
	}

	public double getDurationForAppointment(int id) {
		if(id < 0 || id > appointments.size())
			return -1;
		else
			return appointments.get(id)[2];
	}

	public Graph initAndGetGraph() {
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
		initAndGetGraph();

		// Creating distances matrix based on geografical position if the parameter is null
		if(distances == null)
			distances = getGeograficalDistances();

		// Get average distance
		double averageDistance = getDistanceAverageExcludingDiagonalAndInfines(distances);
		System.out.println("Average distance: "+averageDistance);

		// Creating edges between close nodes
		createEdgesBetweenNodes(distances, averageDistance*2);

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
		routes = new ArrayList<>();
		generateTreeOfRoutes(routes, new Route(appointments.size(), graph.nodes.get(0)));

		// Filter best routes by known distances
		filterRoutes(graph.nodes.size()*2, distances, averageDistance*3);

		// Printing route
		printRoutes(null);
	}


	public Route getBestRouteBasedOnSchedule(long[][] schedule) {

		// Consultar

		return null;
	}

	private double[][] getGeograficalDistances() {
		double distances[][] = new double[graph.nodes.size()][graph.nodes.size()];
		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {
				Node node1 = graph.nodes.get(i);
				Node node2 = graph.nodes.get(j);
				if(convetDegreesToMeters)
					distances[i][j] = Maps.distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
				else
					distances[i][j] = Maps.distanceBetweenPoints(node1.longitude, node1.latitude, node2.longitude, node2.latitude);
				distances[j][i] = distances[i][j];
			}
		}
		return distances;
	}

	/**
	 */
	private void createEdgesBetweenNodes(double distances[][], double minimumDistance) {

		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {

				// Checking if 2 nodes are close enough
				if(distances[i][j] < minimumDistance) {
					graph.nodes.get(i).adjacencies.add(graph.nodes.get(j));
					graph.nodes.get(j).adjacencies.add(graph.nodes.get(i));
				}
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
						double dst = 0;
						if(convetDegreesToMeters)
							dst = Maps.distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
						else
							dst = Maps.distanceBetweenPoints(node1.longitude, node1.latitude, node2.longitude, node2.latitude);
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

			routes.addAll(newRoutes);
			routes.remove(route);

			// Recursive
			for (int i = 0; i < newRoutes.size(); i++)
				generateTreeOfRoutes(routes, newRoutes.get(i));
		}
	}

	private void filterRoutes(int maxNumberOfRoutes, double distances[][], double replaceInfiniteBy) {
		for (int i = 0; i < routes.size(); i++) {
			routes.get(i).calculateWeight(distances, replaceInfiniteBy);
		}
		while(routes.size() > maxNumberOfRoutes) {
			int worse = -1;
			double maxWeight = 0;
			for (int i = 0; i < routes.size(); i++) {
				double w = routes.get(i).getWeight();
				if(w > maxWeight) {
					maxWeight = w;
					worse = i;
				}
			}
			//			System.out.println("Removing route with weight "+maxWeight+"...");
			routes.remove(worse);
		}
	}

	private double getDistanceAverageExcludingDiagonalAndInfines(double distances[][]) {

		double cumulative = 0;
		double counter = 0;
		for (int i = 0; i < distances.length; i++) {
			for (int j = i + 1; j < distances.length; j++) {
				if(i != j && distances[i][j] != Double.MAX_VALUE) {
					cumulative += distances[i][j];
					counter ++;
				}
			}
		}
		return cumulative/counter;
	}

	public void printRoutes(String[] places) {
		System.out.println("-- Routes --");
		for (int i = 0; i < routes.size(); i++) {
			if(places == null)
				System.out.println(routes.get(i));
			else
				System.out.println(routes.get(i).printWithNames(places));
		}
	}

}
