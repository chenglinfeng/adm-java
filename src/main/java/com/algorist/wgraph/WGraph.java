package com.algorist.wgraph;


import com.algorist.datastructure.List;
import com.algorist.graph.EdgeNode;

import java.util.Scanner;

public class WGraph {
    private int nvertices; //number of vertices
    private int nedges; //number of edges
    boolean directed; //is the graph directed?
    private List<EdgeNode>[] edges; //adjacency info
    int[] degree; //outdegree of each vertex

    public int nvertices() {
        return nvertices;
    }

    public int nedges() {
        return nedges;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<EdgeNode> getEdges(int v) {
        return edges[v];
    }

    public int[] degree() {
        return degree;
    }

    public WGraph(int nvertices, boolean directed){
        this.nvertices = nvertices;
        this.nedges = 0;
        this.directed = directed;

        this.edges = new List[nvertices+1];
        this.degree = new int[nvertices+1];

        for(int i=1;i<this.degree.length;i++){
            this.degree[i] = 0;
            this.edges[i] = new List<>();
        }
    }

    public WGraph(Scanner scanner, boolean directed){
        this(scanner.nextInt(),directed);

        final int nedges = scanner.nextInt();

        int x, y, w;            /* vertices in edge (x,y) */
        for (int i = 1; i <= nedges; i++) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            insertEdge(x, new EdgeNode(y,w), directed);
        }

    }

    public void insertEdge(int x, EdgeNode n, boolean directed){
        edges[x].insert(n);
        degree[x]++;

        if (!directed)
            insertEdge(n.getY(), (EdgeNode) n.copy(x,n.getWeight()), true);
        else
            nedges++;
    }

    public void print() {
        for (int i = 1; i <= nvertices; i++) {
            System.out.printf("%d: ", i);

            for (EdgeNode p : edges[i]) {
                System.out.printf(" %d", p.getY());
            }
            System.out.println();
        }
    }
}
