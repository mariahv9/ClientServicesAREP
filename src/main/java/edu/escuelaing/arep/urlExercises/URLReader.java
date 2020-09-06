package edu.escuelaing.arep.urlExercises;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Ejercicio 2

/**
 * This class reads url
 * @author Maria Fernanda Hernandez Vargas
 */
public class URLReader {

    /**
     * Method that read the url
     * @param args
     */
    public static void main(String[] args) {
        readURL("http://localhost:36000/otroarchivoaqui.do?value=56");
    }

    /**
     * Method that does the connection and evaluation of url
     * @param sitetoread
     */
    public static void readURL(String sitetoread) {
        try {
            URL siteURL = new URL(sitetoread);
            URLConnection urlConnection = siteURL.openConnection();
            Map<String, List<String>> headers = urlConnection.getHeaderFields();
            Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
            for (Map.Entry<String, List<String>> entry : entrySet) {
                String headerName = entry.getKey();
                if (headerName != null) {
                    System.out.print(headerName + ":");
                }
                List<String> headerValues = entry.getValue();
                for (String value : headerValues) {
                    System.out.print(value);
                }
                System.out.println("");
            }
            System.out.println("-------message-body------");
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}