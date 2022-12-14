package com.algorist.wgraph;

import java.util.Scanner;

public class AdjacencyMatrix {
    private static final int MAXINT = 100007;
    private final int[][] weight;        /* adjacency/weight info */
    private final int nvertices;         /* number of vertices in the graph */

    private AdjacencyMatrix(int nvertices) {
        this.nvertices = nvertices;
        this.weight = new int[nvertices + 1][nvertices + 1];

        for (int i = 1; i <= nvertices; i++)
            for (int j = 1; j <= nvertices; j++)
                this.weight[i][j] = MAXINT;
    }

    public static AdjacencyMatrix readAdjacencyMatrix(Scanner scanner, boolean directed) {
        int nvertices = scanner.nextInt();
        int m = scanner.nextInt();

        AdjacencyMatrix g = new AdjacencyMatrix(nvertices);

        int x, y, w;            /* placeholder for edge and weight */
        for (int i = 1; i <= m; i++) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            g.weight[x][y] = w;
            if (!directed) g.weight[y][x] = w;
        }

        return g;
    }

    public int nvertices() {
        return this.nvertices;
    }

    public int weight(int i, int j) {
        return this.weight[i][j];
    }

    public void setWeight(int i, int j, int w) {
        this.weight[i][j] = w;
    }

    public void print() {
        for (int i = 1; i <= this.nvertices; i++) {
            System.out.printf("%3d: ", i);
            for (int j = 1; j <= this.nvertices; j++)
                System.out.printf(" %3d", this.weight[i][j]);
            System.out.println();
        }
    }

    public void printGraph() {
        for (int i = 1; i <= this.nvertices; i++) {
            System.out.printf("%d: ", i);
            for (int j = 1; j <= this.nvertices; j++)
                if (this.weight[i][j] < MAXINT)
                    System.out.printf(" %d", j);
            System.out.println();
        }
    }
}
