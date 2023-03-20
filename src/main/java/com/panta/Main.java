package com.panta;

import com.panta.dto.Album;
import okhttp3.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void sendGetRequest(String url) throws IOException {
        StringBuffer responseContent = new StringBuffer();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            responseContent.append(responseBody);
            testJacksonArray( responseContent );
            //System.out.println(responseBody);
        }
    }
    public static void testJackson( StringBuffer responseContent ) throws IOException {

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            Album[] albums = mapper.readValue(responseContent.toString(),  Album[].class );
            Arrays.stream(albums).forEach(System.out::println);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }


    }
    public static void testJacksonArray ( StringBuffer responseContent ) throws IOException {

        try
        {
        {
            final ObjectMapper mapper = new ObjectMapper();
            List<Album> albumList = mapper.readValue(String.valueOf(responseContent), new TypeReference<List<Album>>(){});
            albumList.forEach(x -> System.out.println(x.toString()));
           }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }


    }

    public static void main(String[] args) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();


        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/albums");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            //System.out.println(status);

            if (status > 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                // System.out.println(responseContent.toString());

                //testJackson( responseContent );
                //testJacksonArray( responseContent );
                sendGetRequest( "https://jsonplaceholder.typicode.com/albums" );

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
    }

