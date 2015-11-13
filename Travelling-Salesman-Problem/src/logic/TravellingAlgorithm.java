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
	private ArrayList<Object> identifiers;
	private Graph graph;
	private ArrayList<Route> routes;
	private boolean convetDegreesToMeters, modifiedGraph;

	public TravellingAlgorithm(boolean convetDegreesToMeters) {
		this.appointments = new ArrayList<>();
		this.identifiers = new ArrayList<>();
		this.convetDegreesToMeters = convetDegreesToMeters;
	}

	// *************************** PUBLIC METHODS ***************************

	public void addAppointment(double latitude, double longitude, double duration, Object objectId) {
		appointments.add(new double[]{latitude, longitude, duration});
		identifiers.add(objectId);
		modifiedGraph = true;
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
				graph.nodes.add(new Node(appointments.get(i)[0], appointments.get(i)[1], i, identifiers.get(i)));
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
		createEdgesBetweenNodes(distances, averageDistance);

		// Printing graph
		System.out.println("-- Contectivity graph --");
		System.out.println(graph);

		// Solving the problem of divided groups
		//		ArrayList<Node>[] groups = graph.getGroups();
		//		System.out.println("-- Groups --");
		//		for (int i = 0; i < groups.length; i++) {
		//			System.out.print("{");
		//			for (int j = 0; j < groups[i].size(); j++) {
		//				if(j < groups[i].size()-1)
		//					System.out.print(groups[i].get(j).id+",");
		//				else
		//					System.out.println(groups[i].get(j).id+"}");
		//			}
		//		}
		graph.makeConnected();

		// Solving the bowtie problem
		solvePartitionProblem();

		// Printing graph
		System.out.println("-- Contectivity graph --");
		System.out.println(graph);

		// Creating routes
		routes = new ArrayList<>();
		generateTreeOfRoutes(routes, new Route(appointments.size(), graph.nodes.get(0)));

		// Filter best routes by known distances
		filterRoutes(graph.nodes.size()*2, distances, averageDistance*3);
	}

	public Route getBestRouteBasedOnSchedule(long[][] schedule) {

		long dayStartTime = schedule[0][0];

		// --> Prove all the routes at the specified schedule of working

		return null;
	}

	// *************************** PUBLIC METHODS ***************************

	// ************************** INTERNAL METHODS ***************************

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
			if(i == 11)
				System.out.println("hola");
			for (int j = 0; j < graph.nodes.size(); j++) {

				// Checking if 2 nodes are close enough
				if(i != j && distances[i][j] < minimumDistance) {
					graph.nodes.get(i).addNode(graph.nodes.get(j));
					graph.nodes.get(j).addNode(graph.nodes.get(i));
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
					graph.nodes.get(i).addNode(newConnection);
					newConnection.addNode(graph.nodes.get(i));
				}
			}
		}
	}

	private void generateTreeOfRoutes(ArrayList<Route> routes, Route route) {
		//		System.out.println("Generate");

		if(route.isComplete()) {
			return;
		} else {

			// Getting new routes
			ArrayList<Route> newRoutes = route.continueRoute();

			routes.remove(route);
			routes.addAll(newRoutes);

			// Recursive
			for (int i = 0; i < newRoutes.size(); i++) {
				generateTreeOfRoutes(routes, newRoutes.get(i));
			}
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

	private Object[] mergeArrays(Object[] group1, Object[] group2) {
		Object[] merge = new Object[group1.length+group2.length];
		for (int i = 0; i < group1.length; i++)
			merge[i] = group1[i];
		for (int i = 0; i < group2.length; i++)
			merge[i+group1.length] = group2[i];
		return merge;
	}

	private void solvePartitionProblem() {
		ArrayList<PaintedNode> partitionNodes = graph.getNodesColoredWithPartitions();
		System.out.println("Partitions: "+partitionNodes.size());
		PaintedNode problem = null;
		for (int i = 0; i < partitionNodes.size(); i++) {
			if(i == 0)
				System.out.print("Partitions {"+partitionNodes.get(i).node.id);
			else if(i == partitionNodes.size()-1)
				System.out.print(","+partitionNodes.get(i).node.id+"}\n");
			else
				System.out.println(","+partitionNodes.get(i).node.id);
			if(partitionNodes.get(i).node == graph.nodes.get(0)) {
				problem = partitionNodes.get(i);
				break;
			}
		}
		while(problem != null) {
			System.out.println("We got a problem with partitions...");
		}
	}

	// ************************** INTERNAL METHODS ***************************

	// ************************** PRINTING METHODS **************************

	public void printRoutes(String[] places) {
		System.out.println("-- Routes --");
		for (int i = 0; i < routes.size(); i++) {
			if(places == null)
				System.out.println(routes.get(i));
			else
				System.out.println(routes.get(i).printWithNames(places));
		}
	}

	public void printRoutes() {
		System.out.println("-- Routes --");
		for (int i = 0; i < routes.size(); i++)
			System.out.println(routes.get(i).printWithObjectId());
	}

	// ************************** PRINTING METHODS **************************

}
