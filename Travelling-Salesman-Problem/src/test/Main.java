package test;

import logic.TravellingAlgorithm;

public class Main {

	public static void main(String[] args) {

		TravellingAlgorithm route = new TravellingAlgorithm();
		
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
		
		route.generateRoute();
		

	}

}
