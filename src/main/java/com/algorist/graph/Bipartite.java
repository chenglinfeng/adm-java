package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Bipartite {

    boolean bipartite;
    Color[] color;

    public Bipartite(Graph g) {
        this.color = new Color[g.nvertices() + 1];
        for (int i = 1; i <= g.nvertices(); i++)
            color[i] = Color.UNCOLORED;

        bipartite = true;

        BFS bfs = new BFS(g);
        BipartiteCallback callback = new BipartiteCallback();

        for (int i = 1; i <= g.nvertices(); i++)
            if (!bfs.discovered(i)) {
                color[i] = Color.WHITE;
                bfs.bfs(i, callback);
            }
    }

    public boolean isBipartite() {
        return bipartite;
    }


    Color color(int v) {
        return this.color[v];
    }

    enum Color {
        UNCOLORED, /* vertex not yet colored */
        WHITE,     /* white vertex */
        BLACK;     /* black vertex */

        Color complement() {
            switch (this) {
                case WHITE:
                    return BLACK;
                case BLACK:
                    return WHITE;
                default:
                    return UNCOLORED;
            }
        }
    }

     class BipartiteCallback extends GraphSearchCallback{


        @Override
        public void processVertexEarly(int v) {

        }

        @Override
        public void processVertexLate(int v) {

        }

        @Override
        public void processEdge(int x, int y) {
            if(color[x]==color[y]){
                bipartite = false;
                System.out.printf("Warning: graph not bipartite, due to (%d,%d)%n", x, y);
            }
            color[y] = color[x].complement();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/grid"), "utf-8");
        Graph g = new Graph(scanner, false);
        g.print();

        Bipartite bipartite = new Bipartite(g);

        for (int i = 1; i <= g.nvertices(); i++)
            System.out.printf(" %d", bipartite.color(i).ordinal());
        System.out.println();
    }
}
