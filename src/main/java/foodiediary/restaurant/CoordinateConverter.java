package foodiediary.restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class CoordinateConverter {
	public static void main(String[] args) throws Exception {
		double[] convert5174To4326 = convert5174To4326(325566.176430164, 294170.274941807);
		System.out.println(convert5174To4326[0] + " " + convert5174To4326[1]);
		double[] convert4326To5174 = convert4326To5174(convert5174To4326[0], convert5174To4326[1]);
		System.out.println(convert4326To5174[0]  + " " + convert4326To5174[1]) ;
	}
	
	private static final String API_URL_TEMPLATE =
			"https://api.maptiler.com/coordinates/transform/%f,%f.json?key=%s&s_srs=%s&t_srs=%s";
	private static final String apiKey = "9JuLT4o1qyhA6PFHZLfT";
	
	/*
	 * EPSG:5174 → EPSG:4326 변환 (UTM-K → 위경도)
	 * @param x EPSG:5174 X
	 * @param y EPSG:5174 Y
	 * @return [위도(latitude), 경도(longitude)]
	 */
	public static double[] convert5174To4326(double x, double y) throws Exception {
		return convertCoordinates(x, y, "5174", "4326");
	}
	
	/*
	 * EPSG:4326 → EPSG:5174 변환 (위경도 → UTM-K)
	 * @param longitude 경도
	 * @param latitude 위도
	 * @return [x, y] EPSG:5174 좌표
	 */
	public static double[] convert4326To5174(double longitude, double latitude) throws Exception {
		return convertCoordinates(longitude, latitude, "4326", "5174");
	}
	
	private static double[] convertCoordinates(double coord1, double coord2, String sourceCRS, String targetCRS) throws Exception {
		String url = String.format(API_URL_TEMPLATE, coord1, coord2, apiKey, sourceCRS, targetCRS);
		URL requestUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		
		int status = connection.getResponseCode();
		if (status != 200) {
			throw new RuntimeException("API 호출 실패: HTTP 상태 코드 " + status);
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder content = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		connection.disconnect();
		
		JSONObject json = new JSONObject(content.toString());
		double x = json.getJSONArray("results").getJSONObject(0).getDouble("x");
		double y = json.getJSONArray("results").getJSONObject(0).getDouble("y");
		
		return new double[]{x, y}; // [x, y]
	}
}