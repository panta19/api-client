package com.panta;

import com.panta.dto.Album;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void testJackson( StringBuffer responseContent ) throws IOException {

        try
        {
            ObjectMapper mapper = new ObjectMapper();
            Album[] albums = mapper.readValue(responseContent.toString(),  Album[].class );
            Arrays.stream(albums).forEach(System.out::println);
//            for(int i=0; i<albums.length; i++)    //length is the property of the array
//            {
//                System.out.println("UserID: " + albums[i].getUserId() + " ID: " + albums[i].getId() + " Title: " + albums[i].getTitle());
//            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        catch(Exception ex)
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

                testJackson( responseContent );

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
    }

