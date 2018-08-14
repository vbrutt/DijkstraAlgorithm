package de.heuboe.ausbildung.DijkstraAlgorithm;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.junit.*;
import org.opengis.referencing.*;

public class MultipleNodesTest {
    private static final Logger LOGGER = Logger.getLogger(Input2.class.getName());

    @Test
    public void newAlgorithm() throws IOException, FactoryException {
        LOGGER.info("1 - SHORTEST WAY \n2 - QUICKEST WAY"); /* debug, info, warning, error, fatal */
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();

        Way way = new Way("10141", "12903", n); // 11104 Aachen // 10141 Hamburg // 12903 München-Ost // 11769 Köln //
        List<Node> path = way.run();
        Output.outputLCL(path, way.getDuration());
    }
}