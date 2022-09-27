package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Biconnected {

    public Biconnected(Graph g){

        DFS dfs = new DFS(g);
        BiconnectedCallback callback = new BiconnectedCallback(dfs,g);

        for (int i = 1; i <= g.nvertices(); i++)
            if (!dfs.discovered(i))
                dfs.dfs(i, callback);

    }

    class BiconnectedCallback extends GraphSearchCallback{
        DFS dfs;

        final int[] reachable_ancestor;
        final int[] tree_out_degree;

        BiconnectedCallback(DFS dfs, Graph g) {
            this.dfs = dfs;
            reachable_ancestor = new int[g.nvertices()+1];
            tree_out_degree = new int[g.nvertices()+1];

            for(int i=1; i <= g.nvertices(); i++){
                tree_out_degree[i] = 0;
            }
        }

        @Override
        void processVertexEarly(int v) {
            reachable_ancestor[v] = v;
        }

        @Override
        void processEdge(int x, int y) {
            DFS.EdgeType type;
            type = dfs.edgeClassification(x,y);

            if(type == DFS.EdgeType.TREE){
                tree_out_degree[x] = tree_out_degree[x] + 1;
            }else if(type == DFS.EdgeType.BACK && dfs.parent[x]!=y){
                if(dfs.entryTime(y)<dfs.entryTime(reachable_ancestor[x])){
                    reachable_ancestor[x] = y;
                }
            }
        }

        @Override
        void processVertexLate(int v) {

            int time_v;        /* earliest reachable time for v */
            int time_parent;    /* earliest reachable time for parent[v] */

            if(dfs.parent[v]<1){
                if(tree_out_degree[v]>1){
                    System.out.printf("root articulation vertex: %d %n", v);
                }
                return;
            }
            boolean root = parent(parent(v)) < 1;        /* test if parent[v] is root */

            if (reachable_ancestor[v] == parent(v) && (!root))
                System.out.printf("parent articulation vertex: %d %n", parent(v));

            if (reachable_ancestor[v] == v) {
                System.out.printf("bridge articulation vertex: %d %n", parent(v));

                if (tree_out_degree[v] > 0)    /* test if v is not a leaf */
                    System.out.printf("bridge articulation vertex: %d %n", v);
            }

            time_v = entryTime(reachable_ancestor[v]);
            time_parent = entryTime(reachable_ancestor[parent(v)]);

            if (time_v < time_parent)
                reachable_ancestor[parent(v)] = reachable_ancestor[v];
        }

        private int parent(int v) {
            return dfs.parent(v);
        }

        private int entryTime(int v) {
            return dfs.entryTime(v);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/clr-graph"), "utf-8");
        Graph g = new Graph(scanner, false);
        g.print();

        Biconnected topSort = new Biconnected(g);
    }

}
