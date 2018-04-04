package cadena.cadenareactive.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestReceiver {

    public void locationReceiver() {
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
                String serviceLocation = "http://10.0.2.2:8080/CadenaServerJAXRS/rest/location/get";
                URL url = new URL(serviceLocation); //Specify the URL
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
