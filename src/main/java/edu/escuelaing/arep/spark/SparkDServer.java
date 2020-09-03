package edu.escuelaing.arep.spark;

import edu.escuelaing.arep.httpserver.HttpServer;

public class SparkDServer {
    public static void main(String[] args){
        HttpServer server = new HttpServer();
        server.start();
    }
}