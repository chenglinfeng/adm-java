package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class BFS {

    final Graph g;            /* The graph */
    final boolean[] processed;   /* which vertices have been processed */
    final boolean[] discovered;  /* which vertices have been found */
    final int[] parent;          /* discovery relation */

    public boolean discovered(int v) {
        return discovered[v];
    }

    public int parent(int v) {
        return parent[v];
    }

    public boolean processed(int v){
        return processed[v];
    }

    public BFS(Graph g) {
        this.g = g;
        this.processed = new boolean[g.nvertices() + 1];
        this.discovered = new boolean[g.nvertices() + 1];
        this.parent = new int[g.nvertices() + 1];

        initialize();
    }

    public void initialize() {
        for (int i = 0; i <= g.nvertices(); i++) {
            processed[i] = discovered[i] = false;
            parent[i] = -1;
        }
    }

    public void bfs(int s, GraphSearchCallback callback){
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        discovered[s] = true;

        while (!queue.isEmpty()){
            int v= queue.remove();
            callback.processVertexEarly(v); // process vertex early
            processed[v] = true;

            for(EdgeNode p:g.getEdges(v)){
                int y = p.getY();
                if(!processed[y]){
                    callback.processEdge(v,y); // process edge
                }
                if(!discovered[y]){
                    queue.add(y);
                    discovered[y] = true;
                    parent[y] = v;
                }
            }
            callback.processVertexLate(v); //process vertex late
        }
    }

    class BFSGraphSearchCallback extends GraphSearchCallback{

        public BFSGraphSearchCallback(){

        }

        @Override
        public void processVertexEarly(int v) {
            System.out.printf("processed vertex %d%n", v);
        }

        @Override
        public void processVertexLate(int v) {
        }

        @Override
        public void processEdge(int x, int y) {
            System.out.printf("processed edge (%d,%d)%n", x, y);
        }
    }


    public Stack<Integer> findPath(int start, int end){
        Stack<Integer> path = new Stack<>();
        for(int x = end; x != start; x = parent(x)){
            path.push(x);
        }
        path.push(start);
        return path;
    }

    public void printPath(Stack<Integer> path){
        final StringBuilder builder = new StringBuilder();

        while (!path.isEmpty()){
            builder.append(path.pop().toString());
        }

        if (!path.isEmpty()) {
            byte BLANK_SPACE=' '; //？？？
            builder.append(BLANK_SPACE).append(path.pop());
        }

        System.out.println(builder.toString());

    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/grid"), "utf-8");
        Graph g = new Graph(scanner, false);
        g.print();

        BFS bfs = new BFS(g);
        BFSGraphSearchCallback callback = bfs.new BFSGraphSearchCallback();

        bfs.bfs(1, callback);

        for (int i = 1; i <= g.nvertices(); i++)
            System.out.printf(" %d", bfs.parent(i));
        System.out.println();

        System.out.println();
        for (int i = 1; i <= g.nvertices(); i++) {
            Stack<Integer> path = bfs.findPath(1, i);
            bfs.printPath(path);
        }
    }
}
