package com.algorist.graph;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class TopSort {

    public TopSort(Graph g){
        DFS dfs = new DFS(g);
        TopSortCallBack callBack = new TopSortCallBack(dfs);

        for (int i = 1; i <= g.nvertices(); i++)
            if (!dfs.discovered(i))
                dfs.dfs(i, callBack);

        Stack<Integer> sorted = callBack.sorted();
        dfs.printPath(sorted); /* report topological order */
    }


    class TopSortCallBack extends GraphSearchCallback{

        private final DFS dfs;
        private final Stack<Integer> sorted;

        public TopSortCallBack(DFS dfs){
            this.dfs = dfs;
            this.sorted = new Stack<>();
        }

        @Override
        public void processVertexEarly(int v) {
        }

        @Override
        public void processVertexLate(int v) {
            sorted.push(v);
        }

        @Override
        public void processEdge(int x, int y) {
            DFS.EdgeType edgeType = dfs.edgeClassification(x,y);

            if(edgeType == DFS.EdgeType.BACK){
                System.out.println("Warning: directed cycle found, not a DAG");
            }
        }

        Stack<Integer> sorted() {
            return this.sorted;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/grid"), "utf-8");
        Graph g = new Graph(scanner, true);
        g.print();

        TopSort topSort = new TopSort(g);

    }
}
