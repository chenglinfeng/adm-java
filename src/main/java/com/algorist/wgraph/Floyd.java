package com.algorist.wgraph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floyd {
    public Floyd(AdjacencyMatrix g) {
        int through_k;            /* distance through vertex k */
        for (int k = 1; k <= g.nvertices(); k++)
            for (int i = 1; i <= g.nvertices(); i++)
                for (int j = 1; j <= g.nvertices(); j++) {
                    through_k = g.weight(i, k) + g.weight(k, j);
                    if (through_k < g.weight(i, j))
                        g.setWeight(i, j, through_k);
                }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/wgrid"), "utf-8");
        AdjacencyMatrix g = AdjacencyMatrix.readAdjacencyMatrix(scanner, false);
        g.print();

        new Floyd(g);

        System.out.println();
        g.print();
    }
}
