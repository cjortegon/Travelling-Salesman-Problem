package routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import graph.Graph;
import logic.GoogleMaps;
import logic.Route;
import logic.TravellingAlgorithm;

public class MedicalVisitor {

	public static void main(String[] args) {

		System.out.println("NOT IMPLEMENTED YET");

		//		// Getting Google Maps key
		//		System.out.println("Write your Google Maps key:");
		//		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		//		String key="";
		//		try {
		//			key = brsysi.readLine();
		//		} catch (IOException e1) {
		//		}

		//		testInCaliColombia(key);
	}

	public static void testInCaliColombia(String key) {

		TravellingAlgorithm algorithm = new TravellingAlgorithm(true);

		// The fist spot will be the starting point

		algorithm.addAppointment(3.385552, -76.538367, 1800, "Alkosto");
		algorithm.addAppointment(3.342090, -76.530847, 1800, "Icesi");
		algorithm.addAppointment(3.369213, -76.529486, 1800, "Jardin");
		algorithm.addAppointment(3.369573, -76.523412, 1800, "La14");
		algorithm.addAppointment(3.372966, -76.540071, 1800, "Unicentro");
		algorithm.addAppointment(3.353669, -76.523277, 1800, "Autonoma");
		algorithm.addAppointment(3.369338, -76.537082, 1800, "Crepes");
		algorithm.addAppointment(3.372298, -76.525489, 1800, "Clinica");
		algorithm.addAppointment(3.477838, -76.527730, 1800, "Chipichape");
		algorithm.addAppointment(3.491335, -76.509506, 1800, "Sameco");
		algorithm.addAppointment(3.486261, -76.516709, 1800, "Exito");
		algorithm.addAppointment(3.464946, -76.500997, 1800, "Unico");

		GoogleMaps map = new GoogleMaps(key);
		Graph graph = algorithm.initAndGetGraph();
		double distances[][] = map.getAllDistancesForGraph(graph);
		algorithm.generateRoute(distances);

		// Printing options
		algorithm.printRoutes();

		// Getting the best route for tomorrow
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("Tomorrow is "+(month+1)+"/"+day+"/"+year);

		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		long t1s = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		long t1e = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		long t2s = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 19);
		long t2e = calendar.getTimeInMillis()/1000;

		long schedule[][] = {{t1s,t1e},{t2s,t2e}};
		System.out.println("-- Schedule --");
		System.out.println(schedule[0][0]+" >> "+schedule[0][1]);
		System.out.println(schedule[1][0]+" >> "+schedule[1][1]);

		Route best = algorithm.getBestRouteBasedOnSchedule(schedule, map);
		if(best != null) {
			System.out.println(">> The best route is:");
			System.out.println(best.printWithObjectId());
			System.out.println("Total of the route: "+best.getTimeInSchedule());
		} else {
			System.out.println("Best route hasn't been chosen.");
		}
	}

}
