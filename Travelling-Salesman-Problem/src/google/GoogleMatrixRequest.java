package google;

import java.io.IOException;
//import java.time.LocalDateTime;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GoogleMatrixRequest {

	private OkHttpClient client = new OkHttpClient();

	public String run(String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public int getTravelTime(double[] origin, double[] destination, String key) throws IOException {
		GoogleMatrixRequest request = new GoogleMatrixRequest();
		String url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origin[0]+","+origin[1]+"&destinations="+destination[0]+","+destination[1]+"&mode=driving&language=en-EN&key="+key;

		String response = request.run(url_request);
		int durationIndex=response.indexOf("duration");
		String nuResponse=response.substring(durationIndex);

		int starto=nuResponse.indexOf("value");
		starto+=9;
		int endo=nuResponse.indexOf("},");
		String resp =nuResponse.substring(starto, endo);
		resp=resp.trim();
		return Integer.parseInt(resp);

	}
}
