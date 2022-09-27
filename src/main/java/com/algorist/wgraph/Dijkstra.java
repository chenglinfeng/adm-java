package com.algorist.wgraph;

import com.algorist.graph.EdgeNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Dijkstra {
    private static final int MAXINT = 100007;
    private final int[] parent; /* discovery relation */

    public Dijkstra(WGraph g, int start){
        this.parent = new int[g.nvertices()+1];

        boolean[] intree = new boolean[g.nvertices()+1];
        int[] distance = new int[g.nvertices()+1];

        for(int i=1;i<=g.nvertices();i++){
            parent[i] = -1;
            intree[i] = false;
            distance[i] = MAXINT;
        }

        distance[start] = 0;
        int v =start;

        while (!intree[v]){
            intree[v] = true;
            for(EdgeNode p : g.getEdges(v)){
                int w = p.getY();
                int weight = p.getWeight();
                if(distance[w]>weight+distance[v]){
                    distance[w] = weight+distance[v];
                    parent[w] = v;
                }
            }

            v = 1;
            int dist = MAXINT;
            for (int i = 1; i <= g.nvertices(); i++){
                if (!intree[i] && dist > distance[i]) {
                    dist = distance[i];
                    v = i;
                }
            }
        }
    }

    public Stack<Integer> findPath(int start, int end){
        Stack<Integer> path = new Stack<>();
        for(int x = end; x != start; x = parent[x]){
            path.push(x);
        }
        path.push(start);
        return path;
    }

    public void printPath(Stack<Integer> path){
        //final StringBuilder builder = new StringBuilder();

        if (!path.isEmpty()){
            //builder.append(path.pop().toString());
            System.out.printf("%d",path.pop());
        }

        while (!path.isEmpty()) {
            System.out.printf(" %d",path.pop());
//            byte BLANK_SPACE=' '; //？？？
//            builder.append(BLANK_SPACE).append(path.pop());
        }

        System.out.println();
        //System.out.println(builder.toString());

    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/wgrid"), "utf-8");
        WGraph g = new WGraph(scanner, false);
        g.print();

        Dijkstra dijkstra = new Dijkstra(g, 1);
        System.out.println("Out of Dijkstra");

        System.out.println();
        for (int i = 1; i <= g.nvertices(); i++) {
            /*printf(" %d parent=%d\n",i,parent[i]);*/
            Stack<Integer> path = dijkstra.findPath(1, i);
            dijkstra.printPath(path);
        }
    }
}
