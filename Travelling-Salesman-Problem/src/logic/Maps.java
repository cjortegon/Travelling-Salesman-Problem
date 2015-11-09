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

	public static GoogleMatrixRequest google = new GoogleMatrixRequest();
	private String keyo;

	public static double distanceBetweenPlaces(double latitude1, double longitude1, double latitude2, double longitude2) {
		double dlon = Math.toRadians(longitude2 - longitude1);
		double dlat = Math.toRadians(latitude2 - latitude1);

		double a = (Math.sin(dlat / 2) * Math.sin(dlat / 2)) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * (Math.sin(dlon / 2) * Math.sin(dlon / 2));
		double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return angle * EARTH_RADIUS;
	}

	public static double getPeakHourTravelTime(double latitude1, double longitude1, double latitude2, double longitude2, String key) {
		//		return distanceBetweenPlaces(latitude1, longitude1, latitude2, longitude2);
		double[] from = {latitude1, longitude1};
		double[] to = {latitude2, longitude2};

		try {
			double time = google.getTravelTime(from, to, key);
			return time;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -47;
	}

	public double[][] getAllDistancesForGraph(Graph graph, String key) {
		keyo=key;
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

		for (int i = 0; i < graph.nodes.size(); i++) {
			for (int j = 0; j < graph.nodes.size(); j++) {
				if(i != j) {
					if(distances[i][j] > averageDistance) {
						distances[i][j] = Double.MAX_VALUE;
						distances[j][i] = Double.MAX_VALUE;
					} else {
						Node node1 = graph.nodes.get(i);
						Node node2 = graph.nodes.get(j);
						//						double pre = distances[i][j];
						distances[i][j] = getPeakHourTravelTime(node1.latitude, node1.longitude, node2.latitude, node2.longitude, keyo);
					}
				}
			}
		}
		return distances;
	}

}
