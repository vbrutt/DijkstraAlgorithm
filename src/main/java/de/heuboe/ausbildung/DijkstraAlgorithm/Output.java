package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.text.*;
import java.util.*;

/**
 * @author verab output class
 */
public class Output {
    /**
     * prints out the best way
     * 
     * @param path
     * @param duration
     */
    public static void outputLCL(List<Node> path, double duration) {
        DecimalFormat f = new DecimalFormat("#0.00");

        System.out.println("From: " + path.get(0).getName() + " To: " + path.get(path.size() - 1).getName());
        System.out.print("Best way: ");

        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i).getId() + ",");
        }
        System.out.println(" \nTotal distance: " + (path.get(path.size() - 1).getDist()) / 1000 + " km"
                + "\nRoute duration: " + f.format(duration) + " Hours");
    }
}