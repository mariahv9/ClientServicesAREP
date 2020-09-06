package edu.escuelaing.arep.spark;

import edu.escuelaing.arep.httpserver.Request;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class SparkD {
    private static Map<String, String> path = new HashMap<>();

    public static String get(String resourcePath, String f){
        if (path.containsKey(resourcePath)){
            return path.get(resourcePath);
        }
        return null;
    }

    public void setStaticResourcesPath(String resourcesPath, String data){
        path.put(resourcesPath, data);
    }
}