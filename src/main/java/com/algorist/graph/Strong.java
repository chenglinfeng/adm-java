package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Strong {

    public Strong(Graph g){
        DFS dfs = new DFS(g);

        StrongCallback callback = new StrongCallback(dfs,g);

        dfs.dfs(1,callback);

    }

    class StrongCallback extends GraphSearchCallback{

        private final int[] low;        /* oldest vertex surely in component of v */
        private final int[] scc;        /* strong component number for each vertex */

        private final Stack<Integer> active;            /* active vertices of unassigned component */
        private final DFS dfs;
        private int components_found;        /* number of strong components identified */

        public StrongCallback(DFS dfs, Graph g){
            this.dfs =dfs;
            low = new int[g.nvertices()+1];
            scc = new int[g.nvertices()+1];

            for(int i=1;i<=g.nvertices();i++){
                low[i] = i;
                scc[i] = -1;
            }

            active = new Stack<>();
        }

        @Override
        void processVertexEarly(int v) {
            active.push(v);
        }

        @Override
        void processEdge(int x, int y) {
            DFS.EdgeType edgeType = dfs.edgeClassification(x,y);

            if(edgeType == DFS.EdgeType.BACK){
                if(entryTime(y)<entryTime(low[x])){
                    low[x] = y;
                }

                if(edgeType == DFS.EdgeType.CROSS){
                    if(scc[y] == -1){
                        if(entryTime(y) < entryTime(low[x])){
                            low[x] = y;
                        }
                    }
                }
            }
        }

        @Override
        void processVertexLate(int v) {
            if(low[v] == v){
                popComponent(v);
            }
        }

        private void popComponent(int v){
            int t;                  /* vertex placeholder */

            components_found++;
            System.out.printf("%d is in component %d %n", v, components_found);

            scc[v] = components_found;
            while ((t = active.pop()) != v) {
                scc[t] = components_found;
                System.out.printf("%d is in component %d with %d %n", t, components_found, v);
            }
        }

        private int parent(int v) {
            return dfs.parent(v);
        }

        private int entryTime(int v) {
            return dfs.entryTime(v);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/strong-clr"), "utf-8");
        Graph g = new Graph(scanner, true);
        g.print();

        Strong strong = new Strong(g);
    }

}
