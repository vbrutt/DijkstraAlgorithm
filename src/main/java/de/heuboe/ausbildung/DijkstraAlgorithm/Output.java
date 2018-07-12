package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.text.*;
import java.util.*;

public class Output {
	public static void output(List<Node> path, List<Node> path2, ShortestWay sW, QuickestWay qW) {
		DecimalFormat f = new DecimalFormat("#0.00");

		System.out.print("Shortest way: ");
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" Total distance: " + path.get(path.size() - 1).getDistance() + " km" + "\nRoute duration: "
				+ f.format(sW.duration) + " Hours");

		System.out.print("Quickest way: ");
		for (int i = 0; i < path2.size(); i++) {
			System.out.print(path2.get(i).getId() + ",");
		}
		System.out.println(" \nTotal distance: " + path2.get(path2.size() - 1).getDistance() + " km"
				+ "\nRoute duration: " + f.format(qW.duration) + " Hours\n");
	}

	public static void outputLCL(List<Node> path, double duration) {
		DecimalFormat f = new DecimalFormat("#0.00");

		System.out.print("Best way: ");
		for (int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i).getId() + ",");
		}
		System.out.println(" \nTotal distance: " + (path.get(path.size() - 1).getDistance()) / 1000 + " km" + "\nRoute duration: "
				+ f.format(duration) + " Hours");

	}
}
