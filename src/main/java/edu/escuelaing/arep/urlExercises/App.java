package edu.escuelaing.arep.urlExercises;
import java.net.*;
import java.util.logging.*;

// Ejercicio 1

/**
 *
 */
public class App {

    public static void main (String [] args){
        scanURL ("http://ldbn.escuelaing.edu.co:80/AREP/Mitarea.html");
    }

    private static void scanURL(String s) {
        try {
            URL siteURL = new URL (s);
            System.out.println ("URL: " + siteURL);
            System.out.println ("Protocol: " + siteURL.getProtocol());
            System.out.println ("Host: " + siteURL.getHost());
            System.out.println ("Port: " + siteURL.getPort());
            System.out.println ("Path: " + siteURL.getPath());
            System.out.println ("File: " + siteURL.getFile());
            System.out.println ("Query: " + siteURL.getQuery());
            System.out.println ("Ref.: " + siteURL.getRef());
            System.out.println ("Authority: " + siteURL.getAuthority());
            System.out.println("-------------");
        } catch (MalformedURLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}