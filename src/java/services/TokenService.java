package services;

import com.google.gson.JsonParser;
import providers.DataProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenService {
    private String token = "";

    public String getAccessToken() {
        return this.token;
    }

    public void requestToken() {
        try {
            URL url = new URL(ConfigService.Get("serverRegisterURL"));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 200) {
                JsonParser parser = new JsonParser();

                String data = DataProvider.readData(httpURLConnection);
                token = parser.parse(data).getAsJsonObject().get("access_token").getAsString();
                System.out.println("AccessToken - " + token);
            } else throw new RuntimeException("HTTP URL Connection: " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
