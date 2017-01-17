package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.TimeZone;

public class GoogleMapsTest {

	public static void main(String[] args) {

		// Getting Google Maps key
		System.out.println("Write your Google Maps key:");
		BufferedReader brsysi = new BufferedReader(new InputStreamReader(System.in));
		String key="";
		try {
			key = brsysi.readLine();
		} catch (IOException e1) {
		}

		testGoogleMaps(key);
	}

	public static void testGoogleMaps(String key) {

		GoogleMatrixRequest google = new GoogleMatrixRequest();
		try {
			TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("America/Bogota"));
			long t650am = GoogleMatrixRequest.getTodayTimeInSecondsAt(6, 50, timeZone)+86400;
			long t400am = GoogleMatrixRequest.getTodayTimeInSecondsAt(4, 0, timeZone)+86400;
			System.out.println("6:50 --> "+t650am);
			System.out.println("4:00 --> "+t400am);
			double jplaza[] = new double[]{3.369213, -76.529486};
			double icesi[] = new double[]{3.342090, -76.530847};

			long tt650 = google.getTravelTime(jplaza, icesi, t650am, key);
			long tt400 = google.getTravelTime(jplaza, icesi, t400am, key);

			System.out.println("This test will show the difference between one trip at rush hour and with no traffic.");
			System.out.println("Travel time at 6:50am (rush hour) -> "+(tt650/60)+" minutes.");
			System.out.println("Travel time at 4:00am (no traffic) -> "+(tt400/60)+" minutes.");
		} catch (IOException e) {
			System.out.println("Google error");
		}
	}

}
