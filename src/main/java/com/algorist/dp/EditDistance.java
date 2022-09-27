package com.algorist.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EditDistance {

    static final int MAXLEN = 101; /* longest possible string */
    static final int MATCH = 0;    /* enumerated type symbol for match */
    static final int INSERT = 1;   /* enumerated type symbol for insert */
    static final int DELETE = 2;   /* enumerated type symbol for delete */
    final Cell[][] m;              /* dynamic programming table */
    public StringEdit stringEdit;

    public EditDistance(StringEdit stringEdit){
        this.m = new Cell[MAXLEN+1][MAXLEN+1];
        this.stringEdit = stringEdit;
    }

    public int stringCompare(String s, String t){
        for(int j=0;j<t.length();j++){
            stringEdit.rowInit(j,m);
        }
        for(int i=0;i<s.length();i++){
            stringEdit.colInit(i,m);
        }

        int[] opt = new int[3];
        for(int i=1;i<s.length();i++){
            for(int j=1;j<t.length();j++){
                opt[MATCH] = m[i-1][j-1].cost + stringEdit.match(s.charAt(i),t.charAt(j));
                opt[INSERT] = m[i][j-1].cost + stringEdit.indel(t.charAt(j));
                opt[DELETE] = m[i-1][j].cost + stringEdit.indel(s.charAt(i));

                m[i][j] = new Cell(opt[MATCH], MATCH);
                for (int k = INSERT; k <= DELETE; k++)
                    if (opt[k] < m[i][j].cost) {
                        m[i][j].cost = opt[k];
                        m[i][j].parent = k;
                    }
            }
        }

        int[] c = stringEdit.goalCell(s,t,m);
        return m[c[0]][c[1]].cost;
    }

    public void reconstructPath(String s, String t, int i, int j) {
        switch (m[i][j].parent) {
            case MATCH:
                reconstructPath(s, t, i - 1, j - 1);
                stringEdit.matchOut(s, t, i, j);
                break;

            case INSERT:
                reconstructPath(s, t, i, j - 1);
                stringEdit.insertOut(t, j);
                break;

            case DELETE:
                reconstructPath(s, t, i - 1, j);
                stringEdit.deleteOut(s, i);
                break;

            default:
                break;
        }
    }

    public void printMatrix(String s, String t, boolean costQ) {
        System.out.print("   ");
        for (int i = 0; i < t.length(); i++)
            System.out.printf("  %c", t.charAt(i));
        System.out.println();

        for (int i = 0; i < s.length(); i++) {
            System.out.printf("%c: ", s.charAt(i));
            for (int j = 0; j < t.length(); j++) {
                if (costQ)
                    System.out.printf(" %2d", m[i][j].cost);
                else
                    System.out.printf(" %2d", m[i][j].parent);

            }
            System.out.println();
        }
    }

    static class Cell{
        int cost;
        int parent;

        public Cell(int cost, int parent){
            this.cost = cost;
            this.parent = parent;
        }
    }


}
