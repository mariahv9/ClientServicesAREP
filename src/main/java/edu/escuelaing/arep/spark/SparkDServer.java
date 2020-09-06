package edu.escuelaing.arep.spark;

import edu.escuelaing.arep.httpserver.HttpServer;
import edu.escuelaing.arep.spark.SparkD;

public class SparkDServer {
    public static void main(String[] args){
        SparkD spark = new SparkD();
        HttpServer server = new HttpServer();
        server.start();
    }
}