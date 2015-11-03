package graph;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {

	public ArrayList<Node> nodes;

	public Graph() {
		this.nodes = new ArrayList<>();
	}

	public int[][] getAdjacencyMatrix() {
		int adjacencyMatrix[][] = new int[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			Arrays.fill(adjacencyMatrix[i], Integer.MAX_VALUE);
			for (int j = 0; j < nodes.get(i).adjacencies.size(); j++) {
				adjacencyMatrix[i][nodes.get(i).adjacencies.get(j).id] = 1;
			}
		}
		return adjacencyMatrix;
	}

	public int countGroups() {
		
		// Floyd warshall
		int adjacencyMatrix[][] = getAdjacencyMatrix();
		int next[][] = new int[nodes.size()][nodes.size()];
		for (int k = 0; k < adjacencyMatrix.length; k++) {
			for (int i = 0; i < adjacencyMatrix.length; i++) {
				for (int j = 0; j < adjacencyMatrix.length; j++) {
					if(adjacencyMatrix[i][k] != Integer.MAX_VALUE && adjacencyMatrix[k][j] != Integer.MAX_VALUE) {
						if (adjacencyMatrix[i][k] + adjacencyMatrix[k][j] < adjacencyMatrix[i][j]) {
							adjacencyMatrix[i][j] = adjacencyMatrix[i][k] + adjacencyMatrix[k][j];
							next[i][j] = k;
						}
					}
				}
			}
		}
		
		// Finding groups
		int groupId = 1;
		int groups[] = new int[nodes.size()];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			if(groups[i] == 0) {
				groups[i] = groupId;
				for (int j = 0; j < adjacencyMatrix.length; j++) {
					if(adjacencyMatrix[i][j] != Integer.MAX_VALUE) {
						groups[j] = groupId;
					}
				}
				groupId ++;
			}
		}
		return groupId-1;
	}
	
	public int[] getProblematicPoints() {
		return null;
	}

	@Override
	public String toString() {
		String toString = "";
		for (int i = 0; i < nodes.size(); i++) {
			toString += nodes.get(i) + "\n";
		}
		return toString;
	}

}
