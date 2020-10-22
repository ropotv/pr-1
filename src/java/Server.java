import utils.Parser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

class Server {
    private static final String menu = "\nEnter column key to see this (ex: name)\nEnter key value to see this(ex: name john)";

    public void start(int port) {
        try {
            ArrayList<String> arrayList = Main.getData();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection accepted!");

            PrintWriter print = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ArrayList<String> output;
            String inputLine;
            boolean exit = true;
            print.println(menu);

            while ((inputLine = in.readLine()) != null && exit) {
                ArrayList<String> words = new ArrayList<>(Arrays.asList(inputLine.split(" ")));

                switch (words.size()) {
                    case 1: {
                        if (inputLine.equals("exit")) {
                            print.println("Bye bye");
                            exit = false;
                        } else {
                            output = Parser.getValuesForGivenKey(arrayList, inputLine);
                            assert output != null;
                            for (String outputElement : output) {
                                print.println(outputElement);
                            }
                            print.println(menu);
                        }
                        break;
                    }
                    case 2: {
                        output = Parser.getJsonForGivenValue(arrayList, words.get(0), words.get(1));
                        assert output != null;
                        for (String outputElement : output) {
                            print.println(outputElement);
                        }
                        print.println(menu);
                        break;
                    }
                    default: {
                        print.println("Unknown command!");
                        print.println(menu);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}