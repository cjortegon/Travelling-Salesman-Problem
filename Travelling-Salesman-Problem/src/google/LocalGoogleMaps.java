package google;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LocalGoogleMaps {

	public static final LocalGoogleMaps INSTANCE = new LocalGoogleMaps();

	private HashMap<String, Integer> map;
	private File localDB;

	public LocalGoogleMaps() {
		map = new HashMap<>();
		File folder = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		folder = folder.getParentFile();
		localDB = new File(folder.getAbsolutePath()+"/localDB.txt");
		try {
			if(!localDB.exists()) {
				localDB.createNewFile();
			} else {
				// Reading all files
				BufferedReader bufferedReader = new BufferedReader(new FileReader(localDB));
				String line;
				while((line = bufferedReader.readLine()) != null) {
					String parts[] = line.split(" ");
					map.put(parts[0], Integer.parseInt(parts[1]));
				}
				bufferedReader.close();
			}
		} catch (IOException e) {
		}
		System.out.println(map.size()+" values read from local data base.");
	}

	public void saveValue(double[] origin, double[] destination, long hour, int time) {
		String key = origin[0]+","+origin[1]+","+destination[0]+","+destination[1]+","+hour;
		map.put(key, time);
		try {
			FileOutputStream fos = new FileOutputStream(localDB, true);
			fos.write((key+" "+time+"\n").getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTime(double[] origin, double[] destination, long hour) {
		Integer time = map.get(origin[0]+","+origin[1]+","+destination[0]+","+destination[1]+","+hour);
		if(time == null)
			return -1;
		else
			return time;
	}

	public class TimeSpace {

	}

}
