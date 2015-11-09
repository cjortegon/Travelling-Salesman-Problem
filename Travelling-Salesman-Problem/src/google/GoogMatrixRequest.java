package google;

import java.io.IOException;
//import java.time.LocalDateTime;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class GoogMatrixRequest {

 
  OkHttpClient client = new OkHttpClient();

  public String run(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .build();

    Response response = client.newCall(request).execute();
    return response.body().string();
  }
    
  public int getTravelTime(double[] origino,double[] destinationo,String keyo) throws IOException {
    GoogMatrixRequest request = new GoogMatrixRequest();
//    LocalDateTime nowo=LocalDateTime.now();
//    nowo.getHour();
    String url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+origino[0]+","+origino[1]+"&destinations="+destinationo[0]+","+destinationo[1]+"&mode=driving&language=en-EN&key="+keyo;

    String response = request.run(url_request);
    int durationIndex=response.indexOf("duration");
    String nuResponse=response.substring(durationIndex);
    
//    System.out.println(response);
   
    int starto=nuResponse.indexOf("value");
    starto+=9;
    int endo=nuResponse.indexOf("},");
    String resp =nuResponse.substring(starto, endo);
    resp=resp.trim();
//    System.out.println(resp);
    return Integer.parseInt(resp);
   
  }
}