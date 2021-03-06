package cadena.cadenareactive.services;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cadena.cadenareactive.com.cadenareactive.model.Location;

public class RestReceiver {
    String getLocationBaseURL = "http://10.0.2.2:8080/CadenaServerJAXRS/rest/location/get";

    public Location syncLocationReceiver() throws IOException {
        URL url = new URL(getLocationBaseURL); //Specify the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        if (connection.getResponseCode() == 200) {
            final Gson gson = new Gson();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            reader.close();
            return gson.fromJson(reader, Location.class);
        } else {
            throw new IOException("Service is not available");
        }
    }

    public void asyncLocationReceiver() {
        LocationRunnable location = new LocationRunnable("locationThread");
        location.start();
    }

    public class LocationRunnable implements Runnable {
        private Thread thread;
        private String threadName;
        private String result = "";

        LocationRunnable(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(getLocationBaseURL); //Specify the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Create input stream and load into reader
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) { //Loop through and output each line in stream.
                        System.out.println(inputLine);
                        result += inputLine + "\n";

                    }
                    System.out.println("Service Result:" + result);
                    reader.close();
                } else {
                    System.out.println("Error connection");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        public void start() {
            if (thread == null) {
                thread = new Thread(this, threadName);
                System.out.println("Thread " + threadName + " started");
                thread.start();
            }
        }
    }

}
