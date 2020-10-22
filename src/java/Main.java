import providers.DataProvider;
import services.ConfigService;
import services.TokenService;
import utils.Convertor;

import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class Main {
    private static final ArrayList<String> links = new ArrayList<>();
    private static final ArrayList<String> data = new ArrayList<>();
    private static final int serverPort = 5080;

    private static int index = 0;
    private static int threatsNumber = 0;

    public static ArrayList<String> getData() {
        return data;
    }

    public static void main(String[] args) {
        TokenService tokenService = new TokenService();
        tokenService.requestToken();
        long startTime = System.currentTimeMillis();
        new ThreadRequest(ConfigService.Get("serverHomeURL")).start();

        while (true) {
            if (threatsNumber == 0) {
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println("Execution time = " + elapsedTime + "ms. \nJoining port " + serverPort);
                Server tcpServer = new Server();
                tcpServer.start(serverPort);
                break;
            }
        }
    }

    static class ThreadRequest extends Thread {
        private final String url;

        ThreadRequest(String url) {
            this.url = url;
            threatsNumber++;
        }

        @Override
        public void run() {
            TokenService tokenService = new TokenService();
            try {
                URL siteURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) siteURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("X-Access-Token", tokenService.getAccessToken());
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();

                if (code == 200) {
                    String readedData = DataProvider.readData(httpURLConnection);
                    if (DataProvider.getLinks(readedData) != null)
                        links.addAll(DataProvider.getLinks(readedData));

                    System.out.println(url + "Status Code:" + code + "\nType = " + DataProvider.getType(readedData));

                    if (DataProvider.getData(readedData) != null)
                        Convertor.convertData(data, DataProvider.getType(readedData), DataProvider.getData(readedData));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = index; i < links.size(); i++) {
                new ThreadRequest(links.get(i)).start();
                index++;
            }

            threatsNumber--;
        }
    }
}
