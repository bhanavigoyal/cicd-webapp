package com.puppet.sample;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestRoutes {

    private static final String BASE_URL = "http://localhost:9999";

    @BeforeClass
    public static void beforeClass() throws Exception {
        // Start your Spark app
        App.main(null);

        // Wait for server to start before running tests
        waitForServerToStart();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        // Stop Spark server
        Spark.stop();
    }

    @Test
    public void testEnMsg() throws IOException {
        // Sending GET request to root path
        TestResponse res = request("GET", "/");

        // Assert the response code and content
        assertEquals(200, res.status);
        assertTrue(res.body.contains("Hello!!! My version is 1.0 and I am built from Develop branch on port 9999!"));
    }

    private static void waitForServerToStart() {
        int retries = 10;
        int timeout = 1000; // 1 second
        for (int i = 0; i < retries; i++) {
            try {
                // Try to connect to the server to see if it's ready
                HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // If the connection is successful (response 200), break out of the loop
                if (connection.getResponseCode() == 200) {
                    System.out.println("Server is ready!");
                    return;
                }
            } catch (IOException e) {
                // Server is not ready, retry after a short delay
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        fail("Server did not start in time");
    }

    private TestResponse request(String method, String path) throws IOException {
        try {
            // Making the HTTP request
            URL url = new URL(BASE_URL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();

            // Read the response body
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {
        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
