package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import google.GoogleMatrixRequest;
import graph.Graph;
import logic.Maps;
import logic.Route;
import logic.TravellingAlgorithm;

public class Main {

	public static void main(String[] args) {

		// Getting Google Maps key
		System.out.println("Write your Google Maps key:");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		try {
			key = brsysi.readLine();
		} catch (IOException e1) {
		}

		//testGoogleMaps(key);
		testInCaliColombia(key);

	}

	public static void testGoogleMaps(String key) {

		GoogleMatrixRequest google = new GoogleMatrixRequest();
		try {
			long t650am = GoogleMatrixRequest.getTodayTimeInSecondsAt(6, 50)+86400;
			long t400am = GoogleMatrixRequest.getTodayTimeInSecondsAt(4, 0)+86400;
			System.out.println("6:50 --> "+t650am);
			System.out.println("4:00 --> "+t400am);
			double jplaza[] = new double[]{3.369213, -76.529486};
			double icesi[] = new double[]{3.342090, -76.530847};

			long tt650 = google.getTravelTime(jplaza, icesi, t650am, key);
			long tt400 = google.getTravelTime(jplaza, icesi, t400am, key);

			System.out.println("Travel time at 6:50am (rush hour) -> "+(tt650/60)+" minutes.");
			System.out.println("Travel time at 4:00am (no traffic) -> "+(tt400/60)+" minutes.");
		} catch (IOException e) {
			System.out.println("Google error");
		}
	}

	public static void testInCaliColombia(String key) {

		TravellingAlgorithm algorithm = new TravellingAlgorithm(true);

		// The fist spot will be the starting point and the last spot will be the end of the route always

		algorithm.addAppointment(3.385552, -76.538367, 30, "Alkosto");
		algorithm.addAppointment(3.342090, -76.530847, 0, "Icesi");
		algorithm.addAppointment(3.369213, -76.529486, 30, "Jardin");
		algorithm.addAppointment(3.369573, -76.523412, 30, "La14");
		algorithm.addAppointment(3.372966, -76.540071, 30, "Unicentro");
		algorithm.addAppointment(3.353669, -76.523277, 30, "Autonoma");
		algorithm.addAppointment(3.369338, -76.537082, 30, "Crepes");
		algorithm.addAppointment(3.372298, -76.525489, 30, "Clinica");

//		algorithm.addAppointment(3.394126, -76.544926, 30, "Premier");
//		algorithm.addAppointment(3.398072, -76.539722, 30, "Ruta66");
//		algorithm.addAppointment(3.414001, -76.548025, 30, "Cosmocentro");
//		algorithm.addAppointment(3.430365, -76.540557, 30, "Pascual");

		algorithm.addAppointment(3.477838, -76.527730, 30, "Chipichape");
		algorithm.addAppointment(3.491335, -76.509506, 30, "Sameco");
		algorithm.addAppointment(3.486261, -76.516709, 30, "Exito");
		algorithm.addAppointment(3.464946, -76.500997, 30, "Unico");

		Maps map = new Maps(key);
		Graph graph = algorithm.initAndGetGraph();
		double distances[][] = map.getAllDistancesForGraph(graph);
		algorithm.generateRoute(distances);

		// Printing options
		algorithm.printRoutes();

		// Getting the best route for tomorrow
		Date date = new Date(System.currentTimeMillis()); // your date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("Tomorrow is "+day+"/"+(month+1)+"/"+year);

		calendar.set(Calendar.HOUR_OF_DAY, 9);
		long t1s = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		long t1e = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		long t2s = calendar.getTimeInMillis()/1000;
		calendar.set(Calendar.HOUR_OF_DAY, 19);
		long t2e = calendar.getTimeInMillis()/1000;

		long schedule[][] = {{t1s,t1e},{t2s,t2e}};
		long visitTime = 15*60; // 15 minutes
		System.out.println("-- Schedule --");
		System.out.println(schedule[0][0]+" >> "+schedule[0][1]);
		System.out.println(schedule[1][0]+" >> "+schedule[1][1]);

		Route best = algorithm.getBestRouteBasedOnSchedule(schedule, map, visitTime);
		if(best != null) {
			System.out.println(">> The best route is:");
			System.out.println(best.printWithObjectId());
			System.out.println("Total of the route: "+best.getTimeInSchedule());
		} else {
			System.out.println("Best route hasn't been chosen.");
		}
	}

}
