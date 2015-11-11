package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import google.GoogleMatrixRequest;
import graph.Graph;
import logic.Maps;
import logic.Route;
import logic.TravellingAlgorithm;

public class Main {
	public static void cargarArchivo(TravellingAlgorithm route){
		ArrayList<String> lista= new ArrayList<String>();

		System.out.println("escriba la ruta completa del archivo");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String ruta="";
		try {
			ruta = brsysi.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File (ruta);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			while((linea=br.readLine())!=null)
				lista.add(linea);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}    

		for (int i = 0; i < lista.size(); i++) {

			String[] coordenadas=lista.get(i).split(",");

			int longitud=Integer.parseInt(coordenadas[0]);

			int latitud=Integer.parseInt(coordenadas[1]);

			route.addAppointment(longitud, latitud, 0);
		}
		route.generateRoute();
	}

	public static void main(String[] args) {

		// Getting Google Maps key
		System.out.println("Write your Google Maps key:");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		try {
			key = brsysi.readLine();
		} catch (IOException e1) {
		}

		GoogleMatrixRequest google = new GoogleMatrixRequest();
		try {
			long t650am = GoogleMatrixRequest.getTodayTimeAt(6, 50);
			long t400am = GoogleMatrixRequest.getTodayTimeAt(4, 0);
			System.out.println("6:50 --> "+t650am);
			System.out.println("4:00 --> "+t400am);
			double jplaza[] = new double[]{3.369367, -76.527843};
			double icesi[] = new double[]{3.342090, -76.530847};

			System.out.println(google.getTravelTime(jplaza, icesi, t650am, key));
			System.out.println(google.getTravelTime(jplaza, icesi, t400am, key));
		} catch (IOException e) {
			System.out.println("Google error");
		}

		//		testInCaliColombia();

	}

	public static void testInCaliColombia(String key) {

		TravellingAlgorithm algorithm = new TravellingAlgorithm(false);

		algorithm.addAppointment(3.342090, -76.530847, 0); // U. Icesi
		algorithm.addAppointment(3.369367, -76.527843, 30); // CC Jardin Plaza
		algorithm.addAppointment(3.385552, -76.538367, 30); // Alkosto
		algorithm.addAppointment(3.369573, -76.523412, 30); // La 14 Valle del Lili
		algorithm.addAppointment(3.372966, -76.540071, 30); // Unicentro
		algorithm.addAppointment(3.394126, -76.544926, 30); // Premier
		algorithm.addAppointment(3.353669, -76.523277, 30); // Autonoma
		algorithm.addAppointment(3.398072, -76.539722, 30); // Ruta 66

		//		route.addAppointment(3.486261, -76.516709); // Exito La Flora
		//		route.addAppointment(3.464946, -76.500997); // CC Unico Outlet
		//		route.addAppointment(3.414001, -76.548025); // CC Cosmocentro
		//		route.addAppointment(3.430365, -76.540557); // Estadio Pascual Guerrero

		String places[] = {"Icesi","JP","Alkosto","La14","Unicentro","Premier","Autonoma","Ruta66"};

		Maps map = new Maps(key);
		Graph graph = algorithm.initAndGetGraph();
		double distances[][] = map.getAllDistancesForGraph(graph);
		algorithm.generateRoute(distances);

		// Printing options
		algorithm.printRoutes(places);

		// Getting the best route
		long schedule[][] = {{9,12},{13,19}};
		Route best = algorithm.getBestRouteBasedOnSchedule(schedule);
	}

}
