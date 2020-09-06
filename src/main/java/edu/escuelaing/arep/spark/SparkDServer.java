package edu.escuelaing.arep.spark;

import edu.escuelaing.arep.httpserver.HttpServer;
import edu.escuelaing.arep.spark.SparkD;

/**
 * This class makes the service Spark
 * @author Maria Fernanda Hernandez Vargas
 */
public class SparkDServer {
    /**
     * Method that makes the connections
     * @param args
     */
    public static void main(String[] args){
        SparkD spark = new SparkD();
        HttpServer server = new HttpServer();
        server.start();
    }
}