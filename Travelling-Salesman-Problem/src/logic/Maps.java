package logic;

import java.io.IOException;

import google.GoogMatrixRequest;
import graph.Graph;
import graph.Node;

public class Maps {
    public static GoogMatrixRequest goog=new GoogMatrixRequest();
    private String keyo;
	public static int getPeakHourTravelTime(double longitude1, double latitude1, double longitude2, double latitude2, String key) {
		//return Math.sqrt(Math.pow(longitude1 - longitude2, 2) + Math.pow(latitude1 - latitude2, 2));
		double[] from = {longitude1, latitude1};
		double[] to = {longitude2, latitude2};
		
		try {
			return goog.getTravelTime(from, to, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				double d = Math.sqrt(Math.pow(node1.longitude-node2.longitude, 2)+Math.pow(node1.latitude-node2.latitude, 2));
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
						double pre = distances[i][j];
						distances[i][j] = getPeakHourTravelTime(node1.longitude, node1.latitude, node2.longitude, node2.latitude,keyo);
					}
				}
			}
		}

		return distances;
	}

}
