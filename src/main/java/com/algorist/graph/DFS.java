package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class DFS {
    final Graph g;            /* The graph */
    final boolean[] processed;   /* which vertices have been processed */
    final boolean[] discovered;  /* which vertices have been found */
    final int[] parent;          /* discovery relation */
    private final int[] entryTime;    /* time of vertex entry */
    private final int[] exitTime;     /* time of vertex exit */
    private int time;                 /* current event time */
    private boolean finished;         /* if true, cut off search immediately */

    public boolean discovered(int v) {
        return discovered[v];
    }

    public int parent(int v) {
        return parent[v];
    }

    public boolean processed(int v){
        return processed[v];
    }

    int entryTime(int v) {
        return this.entryTime[v];
    }

    void setEntryTime(int v) {
        this.entryTime[v] = ++time;
    }

    int exitTime(int v) {
        return this.exitTime[v];
    }

    void setExitTime(int v) {
        this.exitTime[v] = ++time;
    }

    void setFinished() {
        this.finished = true;
    }



    public DFS(Graph g) {
        this.g = g;
        this.processed = new boolean[g.nvertices() + 1];
        this.discovered = new boolean[g.nvertices() + 1];
        this.parent = new int[g.nvertices() + 1];
        this.entryTime = new int[g.nvertices() + 1];
        this.exitTime = new int[g.nvertices() + 1];
        this.finished = false;
        initialize();
    }

    public void initialize(){
        for (int i = 0; i <= g.nvertices(); i++) {
            processed[i] = discovered[i] = false;
            parent[i] = -1;
        }
        this.time = 0;
    }

    /**
     * DFS edge types
     */
    enum EdgeType {
        TREE,    /* tree edge */
        BACK,    /* back edge */
        CROSS,   /* cross edge */
        FORWARD  /* forward edge */
    }

    EdgeType edgeClassification(int x, int y) {
        if (parent[y] == x) return EdgeType.TREE;
        if (discovered[y] && !processed[y]) return EdgeType.BACK;
        if (processed[y] && (entryTime[y] > entryTime[x])) return EdgeType.FORWARD;
        if (processed[y] && (entryTime[y] < entryTime[x])) return EdgeType.CROSS;

        System.out.printf("Warning: self loop (%d,%d)%n", x, y);
        return null;
    }


    public void dfs(int v, GraphSearchCallback callback){
        if(finished) return;

        discovered[v] = true;
        setEntryTime(v);

        callback.processVertexEarly(v);

        for(EdgeNode p:g.getEdges(v)){
            int y = p.getY();
            if(discovered[y] == false){
                parent[y] = v;
                callback.processEdge(v,y);
                dfs(y,callback);
            }else if(!processed[y]){
                callback.processEdge(v,y);
            }

            if(finished){
                return;
            }
        }
        callback.processVertexLate(v);
        setExitTime(v);
        processed[v] = true;
    }

    class DFSCallBack extends GraphSearchCallback {

        public DFSCallBack(){

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
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/grid"), "utf-8");
        Graph g = new Graph(scanner, false);
        g.print();

        DFS dfs = new DFS(g);
        DFSCallBack callback = dfs.new DFSCallBack();

        dfs.dfs(1, callback);

        for (int i = 1; i <= g.nvertices(); i++)
            System.out.printf(" %d", dfs.parent(i));
        System.out.println();

        System.out.println();
        for (int i = 1; i <= g.nvertices(); i++) {
            Stack<Integer> path = dfs.findPath(1, i);
            dfs.printPath(path);
        }
    }
}
