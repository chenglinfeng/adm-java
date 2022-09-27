/*
Copyright 2003 by Steven S. Skiena; all rights reserved.

Permission is granted for use in non-commercial applications
provided this copyright notice remains intact and unchanged.

This program appears in my book:

"Programming Challenges: The Programming Contest Training Manual"
by Steven Skiena and Miguel Revilla, Springer-Verlag, New York 2003.

See our website www.programming-challenges.com for additional information.

This book can be ordered from Amazon.com at

http://www.amazon.com/exec/obidos/ASIN/0387001638/thealgorithmrepo/

*/
package com.algorist.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Approximately match one string as a substring of another, where is s in t?
 * <p>
 * Generify from stringedit.c.
 *
 * @author csong2022
 */
public class SubStringEdit extends StringEdit {

    int[] goalCell(String s, String t, EditDistance.Cell[][] m) {
        int i = s.length() - 1;
        int j = 0;

        for (int k = 1; k < t.length(); k++)
            if (m[i][k].cost < m[i][j].cost) j = k;

        return new int[]{i, j};
    }

    void rowInit(int i, EditDistance.Cell[][] m) {        /* what is m[0][i]? */
        m[0][i] = new EditDistance.Cell(0, -1);
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/substringedit-in"), "utf-8");

        String s = ' ' + scanner.next();
        String t = ' ' + scanner.next();

        StringEdit stringEdit = new SubStringEdit();
        EditDistance editDistance = new EditDistance(stringEdit);

        System.out.printf("matching cost = %d %n", editDistance.stringCompare(s, t));

        editDistance.printMatrix(s, t, true);
        System.out.println();
        editDistance.printMatrix(s, t, false);

        int[] p = stringEdit.goalCell(s, t, editDistance.m);
        int i = p[0], j = p[1];
        System.out.printf("%d %d%n", i, j);

        editDistance.reconstructPath(s, t, i, j);
        System.out.println();
    }
}
