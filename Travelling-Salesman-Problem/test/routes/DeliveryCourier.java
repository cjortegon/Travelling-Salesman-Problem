package routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import google.GoogleMatrixRequest;
import graph.Graph;
import logic.GoogleMaps;
import logic.Route;
import logic.TravellingAlgorithm;

public class DeliveryCourier {

	public static void main(String[] args) {

		// Getting Google Maps key
		System.out.println("Write your Google Maps key:");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		try {
			key = brsysi.readLine();
		} catch (IOException e1) {
		}

		testCase(key);
	}

	public static void testCase(String key) {

		TravellingAlgorithm algorithm = new TravellingAlgorithm(true);
		buildTestCase(algorithm);

		GoogleMaps map = new GoogleMaps(key);
		Graph graph = algorithm.initAndGetGraph();
		double distances[][] = map.getAllDistancesForGraph(graph);
		algorithm.generateRoute(distances);

		// Printing options
		System.out.println("*** All the posible routes ***");
		algorithm.printRoutes();

		// Tomorrow at 9:00am
		long secondsOfOneDay = 86400;
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("America/Bogota"));
		long startTime = GoogleMatrixRequest.getTodayTimeInSecondsAt(6, 00, timeZone) + secondsOfOneDay;

		Route best = algorithm.getBestRouteBasedStartingAt(startTime, map);
		System.out.println("*** The best route ***");
		if(best != null) {
			System.out.println(best.printWithObjectId());
			System.out.println("Total time to do the route: "+best.getTimeInSchedule());
		} else {
			System.out.println("Best route hasn't been chosen.");
		}
	}

	public static void buildTestCase(TravellingAlgorithm algorithm) {

		// This will be the starting point
		algorithm.addAppointment(3.385552, -76.538367, 0, "Alkosto");

		// These are the points in the city to do the delivery
		algorithm.addAppointment(3.477838, -76.527730, 180, "Chipichape");
		algorithm.addAppointment(3.491335, -76.509506, 180, "Sameco");
		algorithm.addAppointment(3.486261, -76.516709, 180, "Exito");
		algorithm.addAppointment(3.464946, -76.500997, 180, "Unico");
		algorithm.addAppointment(3.417825, -76.496154, 180, "Poblado");
		algorithm.addAppointment(3.435385, -76.515417, 180, "Comfandi");
		algorithm.addAppointment(3.446821, -76.541889, 180, "SanAntonio");
		algorithm.addAppointment(3.451774, -76.501057, 180, "LosAmigos");
		algorithm.addAppointment(3.483183, -76.492470, 180, "CementerioNorte");
		algorithm.addAppointment(3.397833, -76.508584, 180, "ReservaAgricola");
		algorithm.addAppointment(3.440631, -76.479153, 180, "RioCauca");
		algorithm.addAppointment(3.429549, -76.541762, 180, "Pascual");
		algorithm.addAppointment(3.435947, -76.545053, 180, "ParquePerro");
		algorithm.addAppointment(3.423002, -76.538115, 180, "Panamericanas");
		algorithm.addAppointment(3.427348, -76.520111, 180, "SantaElena");
	}

}
