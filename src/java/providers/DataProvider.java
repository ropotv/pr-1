package providers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import services.ConfigService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataProvider {
    public static String readData(HttpURLConnection httpURLConnection) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static ArrayList<String> getLinks(String data) {
        ArrayList<String> links = new ArrayList<String>();
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("link");
        Map<String, Object> map = new HashMap<String, Object>();
        String json;

        if (jsonElement != null) {
            json = jsonElement.toString();
        } else return null;

        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            links.add(ConfigService.Get("serverUrl") + entry.getValue());
        }
        return links;
    }

    public static String getData(String data) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("data");
        if (jsonElement != null) {
            return jsonElement.getAsString();
        } else return null;
    }

    public static String getType(String data) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("mime_type");
        if (jsonElement == null) {
            return "json";
        }
        return jsonElement.getAsString();
    }
}
