package edu.escuelaing.arep.httpserver;

import edu.escuelaing.arep.spark.SparkD;

import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

/**
 *
 */
public class HttpServer {
    private int port = 36000;
    private boolean isRunning = true;
    private SparkD spark = null;


    public HttpServer() {

    }
    public HttpServer(SparkD miSpark) {
        this.spark = miSpark;
    }
    public HttpServer(int port, SparkD spark)  {
        this.port = port;
        this.spark = spark;
    }

    public int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000; // returns default port if heroku-port isn't set(i.e. on localhost)
    }
    public void start(){
        try{
            port = getPort();
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port);
                System.exit(1);
            }
            while(isRunning){
                try {
                    Socket clientSocket = null;
                    try {
                        System.out.println("Listo para recibir en puerto 36000 ...");
                        clientSocket = serverSocket.accept();
                    } catch (IOException e) {
                        System.err.println("Accept failed.");
                        System.exit(1);
                    }
                    processRequest(clientSocket);
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }
    }

    public void processRequest(Socket clientSocket) throws IOException, URISyntaxException {
        PrintWriter out = new PrintWriter(
                clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        boolean resourceInLine = true;
        Request resource = null;
        while ((inputLine = in.readLine()) != null) {
            if(resourceInLine) {
                resource = new Request(inputLine);
                resourceInLine = false;
            }
            System.out.println("Recibí: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        createResponse(resource,out,clientSocket);
    }

    private void createResponse(Request response, PrintWriter out, Socket clientSocket) throws IOException, URISyntaxException {
        if (response!= null && response.getRequestURI().startsWith("/API")) {
            String appuri = response.getRequestURI().substring(4);
            response.setTheuri(new URI(appuri));
            spark.get(response.getRequestURI(), out);
        } else if (response != null){
            spark.setStaticResourcesPath(response, out, clientSocket);
        }
        out.close();
    }
}