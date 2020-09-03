package edu.escuelaing.arep.spark;

import edu.escuelaing.arep.httpserver.Request;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.BiFunction;

public class SparkD {
    public static void get(String resourcePath, PrintWriter f){
        // if servidorweb no está corriendo correrlo
        // Poner f con el nombre resourcepath en el arreglo de paths funcionales
    }

    public void setStaticResourcesPath(Request response, PrintWriter out, Socket clientSocket){}
}
