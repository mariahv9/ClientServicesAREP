package edu.escuelaing.arep.spark;

import java.util.*;

/**
 * This class construct the service Spark
 * @author Maria Fernanda Hernandez Vargas
 */
public class SparkD {
    private static Map<String, String> path = new HashMap<>();

    /**
     * Method that get the path
     * @param resourcePath
     * @return
     */
    public static String get(String resourcePath){
        if (path.containsKey(resourcePath)){
            return path.get(resourcePath);
        }
        return null;
    }
}