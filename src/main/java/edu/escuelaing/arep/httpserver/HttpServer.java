package edu.escuelaing.arep.httpserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

/**
 * Class that does the connection with web server
 * @author Maria Fernanda Hernandez Vargas
 */
public class HttpServer {
    private int port = 36000;
    private boolean running = false;
    private DataBase connect = null;
    private Map<String, String> request;

    /**
     * Method constructor that obtains port and does request uri
     */
    public HttpServer() {
        this.port = getPort();
        request = new HashMap<>();
    }

    /**
     * Method constructor that saves port
     * @param port
     */
    public HttpServer(int port) {
        this.port = port;
        request = new HashMap<>();
    }

    /**
     * Method that obtains port
     * @return port connection
     */
    public int getPort(){
        if (System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000;
    }

    /**
     * Method that starts connection with sockets
     */
    public void start() {
        try {
            ServerSocket serverSocket = null;
            this.port = getPort();

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

    /**
     * Method that does the process of the request of the uri that
     * wnats to connect
     * @param clientSocket
     * @throws IOException
     */
    private void processRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        boolean requestLineReady = true;
        Request req = null;
        while ((inputLine = in.readLine()) != null) {
            if (requestLineReady) {
                req = new Request(inputLine);
                requestLineReady = false;
            }
            if (!in.ready()) {
                break;
            }
        }
        if(req!=null) {
            createResponse(req, new PrintWriter(clientSocket.getOutputStream(), true), clientSocket);
        }
        in.close();
    }

    /**
     * Method that create response of data base
     * @param req
     * @param out
     * @param clientSocket
     * @throws IOException
     */
    private void createResponse(Request req, PrintWriter out, Socket clientSocket) throws IOException {
        if (req.getRequestURI().startsWith("/dataBase")) {
            String db = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>Base de Datos</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<h1>Utiles escolares</h1>\n"
                    + "</body>\n"
                    + "</html>\n";
            db += getDataBase();
            out.println(db);
        } else {
            getStaticResource(req.getRequestURI(), out, clientSocket);
        }
        out.close();
    }

    /**
     * Method that obtains the path to connect
     * @param path
     * @param out
     * @param clientSocket
     * @throws IOException
     */
    private void getStaticResource(String path, PrintWriter out, Socket clientSocket) throws IOException{
        Path file = Paths.get("src/main/resources/public_html" + path);
        String resource = "HTTP/1.1 200 OK\r\n";
        if (path.contains(".html") || path.equals("/")){
            path = "index.html";
            resource += ("Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n");
        }else if (path.contains(".jpg")) {
            getImage(path, clientSocket.getOutputStream());
        }
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            out.println(resource);
            String line = null;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that obtains the image and helps to show on web site
     * @param path
     * @param outputStream
     */
    public void getImage (String path, OutputStream outputStream){
        File file = new File("src/main/resources" + path);
        try {
            BufferedImage pic = ImageIO.read(file);
            ByteArrayOutputStream picShow = new ByteArrayOutputStream();
            DataOutputStream picDraw = new DataOutputStream(outputStream);
            ImageIO.write (pic, "JPG", picShow);
            picDraw.writeBytes("HTTP/1.1 200 OK\r\n" + "Content-Type: image/jpg \r\n\r\n");
            picDraw.write(picShow.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that connect to persistence
     * @return
     */
    public String getDataBase (){
        DataBase db = new DataBase();
        ArrayList <String []> data = db.getTable();
        String list = "";
        for (String [] datos : data) {
            list += datos [0] + ". Nombre producto: " + datos [1] + "\n";
        }
        return list;
    }
}