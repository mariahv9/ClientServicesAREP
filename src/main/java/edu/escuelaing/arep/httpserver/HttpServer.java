package edu.escuelaing.arep.httpserver;

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
    private boolean running = false;
    private Map<String, String> request;

    public HttpServer() {
        this.port = getPort();
        request = new HashMap<>();
    }

    public HttpServer(int port) {
        this.port = port;
        request = new HashMap<>();
    }

    public int getPort(){
        if (System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000;
    }

    public void start() {
        try {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + getPort());
                System.exit(1);
            }

            running = true;
            while (running) {
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
                }
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;

        boolean requestLineReady = false;
        Request req = null;
        while ((inputLine = in.readLine()) != null) {
            if (!requestLineReady) {
                req = new Request(inputLine);


                requestLineReady = true;
            } else {
                String[] entry = createEntry(inputLine);
                if (entry.length > 1) {
                    request.put(entry[0], entry[1]);
                }
            }
            if (!in.ready()) {
                break;
            }
        }


        System.out.println("RequestLine: " + req);
        createResponse(req, new PrintWriter(clientSocket.getOutputStream(), true));
        in.close();
    }

    private String[] createEntry(String rawEntry) {
        return rawEntry.split(":");
    }

    private void createResponse(Request req, PrintWriter out) {
        String outputLine = testResponse();
        URI theuri = req.getTheuri();
        if (theuri.getPath().startsWith("/Apps")) {
        } else {
            getStaticResource(theuri.getPath(), out);
        }
        out.close();
    }

    private String testResponse() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Mi propio mensaje</h1>\n"
                + "</body>\n"
                + "</html>\n";
        return outputLine;
    }

    private void getStaticResource(String path, PrintWriter out) {
        Path file = Paths.get("target/classes/public_html" + path);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String header = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n";
            out.println(header);
            String line = null;
            while ((line = reader.readLine()) != null) {
                out.println(line);
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}