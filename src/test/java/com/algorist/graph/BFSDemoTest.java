package com.algorist.graph;

import com.algorist.test.TestCaseWithInput;
import com.algorist.test.TestEngine;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class BFSDemoTest implements TestCaseWithInput {
    @Override
    public void process(Scanner scanner) {
        Graph g = new Graph(scanner, false);
        g.print();

        BFS bfs = new BFS(g);
        BFS.BFSGraphSearchCallback callback = bfs.new BFSGraphSearchCallback();

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

    @Test
    public void test() throws IOException {
        TestEngine.execute(this, "grid", "grid-bfs-demo-out");
    }
}
