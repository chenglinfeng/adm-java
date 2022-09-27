package com.algorist.wgraph;

import com.algorist.datastructure.SetUnion;
import com.algorist.graph.EdgeNode;
import com.algorist.graph.Graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Kruskal {
    public int total = 0;

    public Kruskal(WGraph g){
        SetUnion s = new SetUnion(g.nvertices());
        EdgePair[] e = toEdgeArray(g);
        Arrays.sort(e,0,e.length,WEIGHT_COMPARE);

        for(int i=0;i<e.length;i++){
            if(!s.sameComponent(e[i].x,e[i].y)){
                System.out.printf("edge (%d,%d) of weight %d in MST%n", e[i].x, e[i].y, e[i].weight);
                total += e[i].weight;
                s.unionSets(e[i].x, e[i].y);
            }
        }
    }

    private static EdgePair[] toEdgeArray(WGraph g) {
        EdgePair[] e = new EdgePair[g.nedges()];

        int m = 0;
        for (int i = 1; i <= g.nvertices(); i++) {
            for (EdgeNode p : g.getEdges(i)) {
                if (p.getY() > i) {
                    e[m++] = new EdgePair(i, p.getY(), p.getWeight());
                }
            }
        }

        return e;
    }

    private static class EdgePair {
        final int x, y;                       /* adjacency info */
        final int weight;                     /* edge weight, if any */

        EdgePair(int x, int y, int weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
        }
    }

    private static final Comparator<EdgePair> WEIGHT_COMPARE = new Comparator<EdgePair>() {
        @Override
        public int compare(EdgePair x, EdgePair y) {
            if (x.weight < y.weight) return -1;
            else if (x.weight > y.weight) return 1;
            else return 0;
        }
    };

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/wgrid"), "utf-8");
        WGraph g = new WGraph(scanner, false);
        g.print();

        Kruskal k = new Kruskal(g);
        System.out.println("Out of Prim");
        System.out.println(k.total);
    }
}
