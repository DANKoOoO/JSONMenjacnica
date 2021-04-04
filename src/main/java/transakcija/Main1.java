package transakcija;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main1 {
	private static final String MY_URL = "http://api.currencylayer.com/live?access_key=2e4baadf5c5ae6ba436f53ae5558107f";

	public static void main(String[] args) {
		
		Transakcija t = new Transakcija();
		t.setIzvornaValuta("USD");
		
		t.setKrajnjaValuta("CAD");  
		
		t.setPocetniIznos(34);
		

		Gson gson = new Gson();

		try {
			URL url = new URL(MY_URL);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			JsonObject result = gson.fromJson(reader, JsonObject.class);

			if (result.get("success").getAsBoolean()) {

				t.setDatumTransakcije(new Date());
				
				String currencies = t.getIzvornaValuta() +  t.getKrajnjaValuta();
				
				double exchangeRate = result.get("quotes").getAsJsonObject().get(currencies).getAsDouble();
				
				t.setKonvertovaniIznos(t.getPocetniIznos() * exchangeRate);
								
				System.out.println(t);
				
				File output = new File("output");
				if(!output.exists())
					output.mkdir();
				
				t.serializeToJson();
				
			} else {
				System.out.println("Greska kod konekcije");
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
