package logic;

import java.io.IOException;

import google.GoogleMatrixRequest;
import graph.Graph;
import graph.Node;

public class Maps {

	/**
	 * Constants
	 */
	final static double EARTH_RADIUS = 6378160; // Radio de la tierra en metros

	private GoogleMatrixRequest google = new GoogleMatrixRequest();
	private String key;

	public Maps(String key) {
		this.key = key;
	}

	public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
	}

	public static double distanceBetweenPlaces(double latitude1, double longitude1, double latitude2, double longitude2) {
		double dlon = Math.toRadians(longitude2 - longitude1);
		double dlat = Math.toRadians(latitude2 - latitude1);

		double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
		double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return angle * EARTH_RADIUS;
	}

	public double getPeakTravelTime(double latitude1, double longitude1, double latitude2, double longitude2, long hour) {
		//		return distanceBetweenPlaces(latitude1, longitude1, latitude2, longitude2);
		double[] from = {latitude1, longitude1};
		double[] to = {latitude2, longitude2};

		try {
			double time = google.getTravelTime(from, to, hour, key);
			return time;
		} catch (IOException e) {
			// --> Handle if Google didn't responds (Not implemented yet)
			e.printStackTrace();
		}
		return -1;
	}

	public double[][] getAllDistancesForGraph(Graph graph) {

		double distances[][] = new double[graph.nodes.size()][graph.nodes.size()];
		double averageDistance = 0;
		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = i + 1; j < graph.nodes.size(); j++) {
				Node node1 = graph.nodes.get(i);
				Node node2 = graph.nodes.get(j);
				double d = distanceBetweenPlaces(node1.latitude, node1.longitude, node2.latitude, node2.longitude);
				distances[i][j] = d;
				distances[j][i] = d;
				averageDistance += d;
			}
		}
		averageDistance *= 2;
		averageDistance /= (graph.nodes.size()*(graph.nodes.size()-1));

		int consultados = 0;
		int borrados = 0;
		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = 0; j < graph.nodes.size(); j++) {
				if(i != j) {
					if(distances[i][j] > averageDistance) {
						distances[i][j] = Double.MAX_VALUE;
						distances[j][i] = Double.MAX_VALUE;
						borrados ++;
					} else {
						Node node1 = graph.nodes.get(i);
						Node node2 = graph.nodes.get(j);
						distances[i][j] = getPeakTravelTime(node1.latitude, node1.longitude,
								node2.latitude, node2.longitude, GoogleMatrixRequest.getTodayTimeAt(4, 0));
						consultados ++;
					}
				}
			}
		}
		int allConnections = graph.nodes.size()*(graph.nodes.size()-1);
		System.out.println("Consultados: "+consultados+"/"+allConnections);
		System.out.println("Borrados: "+borrados+"/"+allConnections);
		return distances;
	}

}
