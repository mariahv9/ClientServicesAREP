package edu.escuelaing.arep.httpserver;

import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * Class that makes the Request URI
 * @author Maria Fernanda Hernandez Vargas
 */
public class Request {
    private String method;
    private String requestURI;
    private String HTTPVersion;
    private URI theuri;
    private Map<String,String> query;

    /**
     * Method constructor
     * @param requestLine
     */
    public Request(String requestLine){
        try {
            String[] components= requestLine.split(" ");
            method = components[0];
            this.requestURI = components[1];
            HTTPVersion = components[2];
            setTheuri(new URI(requestURI));
            query = parseQuery(theuri.getQuery());
        } catch (URISyntaxException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to obtains the uri request
     * @return
     */
    public String getRequestURI() {
        return this.requestURI;
    }

    /**
     * Method to convert the uri to string
     * @return
     */
    public String toString(){
        return method + " " + requestURI + " " + HTTPVersion + "\n\r" + getTheuri() + "\n\r" + "Query: " + query;
    }

    /**
     * Method to obtains the uri
     * @return
     */
    public URI getTheuri() {
        return theuri;
    }

    /**
     * Method that saves the uri
     * @param theuri
     */
    public void setTheuri(URI theuri) {
        this.theuri = theuri;
    }

    /**
     * Method that maps the uri
     * @param query
     * @return
     */
    private Map<String, String> parseQuery(String query) {
        if( query == null) return null;
        Map<String, String> theQuery = new HashMap();
        String[] nameValuePairs = query.split("&");
        for(String nameValuePair: nameValuePairs){
            int index = nameValuePair.indexOf("=");
            if(index!=-1){
                theQuery.put(nameValuePair.substring(0, index), nameValuePair.substring(index+1));
            }
        }
        return theQuery;
    }
}