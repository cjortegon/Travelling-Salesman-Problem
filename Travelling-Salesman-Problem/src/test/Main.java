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

			route.addAppointment(longitud, latitud);
		}
		route.generateRoute();
	}

	public static void main(String[] args) {

		TravellingAlgorithm route = new TravellingAlgorithm(false, 20);

		route.addAppointment(3.342090, -76.530847); // U. Icesi
		route.addAppointment(3.369367, -76.527843); // CC Jardin Plaza
		route.addAppointment(3.385552, -76.538367); // Alkosto
		route.addAppointment(3.369573, -76.523412); // La 14 Valle del Lili
		route.addAppointment(3.372966, -76.540071); // Unicentro
		route.addAppointment(3.394126, -76.544926); // Premier
		route.addAppointment(3.353669, -76.523277); // Autonoma
		route.addAppointment(3.398072, -76.539722); // Ruta 66

//		route.addAppointment(3.486261, -76.516709); // Exito La Flora
//		route.addAppointment(3.464946, -76.500997); // CC Unico Outlet
//		route.addAppointment(3.414001, -76.548025); // CC Cosmocentro
//		route.addAppointment(3.430365, -76.540557); // Estadio Pascual Guerrero

		String places[] = {"Icesi","JP","Alkosto","La14","Unicentro","Premier","Autonoma","Ruta66"};

		System.out.println("Write your Google Maps key:");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		try {
			key = brsysi.readLine();
		} catch (IOException e1) {
		}

		Maps map = new Maps(key);
		Graph graph = route.initAndGetGraph();
		double distances[][] = map.getAllDistancesForGraph(graph);
		route.generateRoute(distances);
		route.printRoute(places);

	}

}
