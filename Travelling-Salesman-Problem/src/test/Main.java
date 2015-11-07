package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
	
		TravellingAlgorithm route = new TravellingAlgorithm(false, 800);
		
		route.addAppointment(1300, 1300);
		route.addAppointment(700, 1300);
		route.addAppointment(1000, 800);
		route.addAppointment(800, 300);
		route.addAppointment(1200, 100);
		route.addAppointment(1400, 800);
		route.addAppointment(1900, 600);
		route.addAppointment(1900, 1200);
		route.addAppointment(2200, 1600);
		route.addAppointment(1600, 1700);
		
		route.generateRoute(Maps.getAllDistancesForGraph(route.startGraph()));
		

	}

}
