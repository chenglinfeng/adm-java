package com.algorist.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static com.algorist.dp.EditDistance.DELETE;
import static com.algorist.dp.EditDistance.INSERT;

public class StringEdit {

    int match(char c, char d){
        return c == d? 0 : 1;
    }

    int indel(char c){
        return 1;
    }

    void rowInit(int i, EditDistance.Cell[][] m){
        if(i>0){
            m[0][i] = new EditDistance.Cell(i,INSERT);
        }else{
            m[0][i] = new EditDistance.Cell(i,-1);
        }
    }

    void colInit(int i, EditDistance.Cell[][] m){
        if(i>0){
            m[i][0] = new EditDistance.Cell(i,DELETE);
        }else{
            m[i][0] = new EditDistance.Cell(i,-1);
        }
    }

    void insertOut(String t, int i){
        System.out.print("I");
    }

    void deleteOut(String s, int i){
        System.out.print("D");
    }

    void matchOut(String s, String t, int i, int j) {
        if (s.charAt(i) == t.charAt(j)) System.out.print("M");
        else System.out.print("S");
    }

    int[] goalCell(String s, String t, EditDistance.Cell[][] m) {
        return new int[]{s.length() - 1, t.length() - 1};
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileInputStream("src/main/c/datafiles/stringedit-in"), "utf-8");

        String s = ' ' + scanner.next();
        String t = ' ' + scanner.next();

        StringEdit stringEdit = new StringEdit();
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
