package com.algorist.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Partition {

    private static final int MAXINT = 100000;        /* infinity */

    int[] s;              /* book thicknesses to partition */
    int n;                /* how many books? */
    int k;                /* how many partitions? */

    Partition(){

    }

    void read(Scanner scanner) {
        n = scanner.nextInt();
        k = scanner.nextInt();

        s = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            s[i] = scanner.nextInt();
        }
    }

    private void printBooks(int start, int end) {
        System.out.print("{");
        for (int i = start; i <= end; i++) System.out.printf(" %d ", s[i]);
        System.out.println("}");
    }

    private void reconstructPartition(int[][] d, int n, int k) {
        if (k == 1)
            printBooks(1, n);
        else {
            reconstructPartition(d, d[n][k], k - 1);
            printBooks(d[n][k] + 1, n);
        }
    }

    public void partition(){
        int[] p = new int[n+1];
        int[][] m = new int[n+1][k+1];
        int[][] d = new int[n+1][k+1];

        int cost;

        p[0] = 0;                /* construct prefix sums */
        for (int i = 1; i <= n; i++) p[i] = p[i - 1] + s[i];

        for (int i = 1; i <= n; i++) m[i][1] = p[i];    /* initialize boundaries */
        for (int j = 1; j <= k; j++) m[1][j] = s[1];

        for(int i=2;i<=n;i++){
            for(int j=2;j<=k;j++){
                m[i][j] = MAXINT;
                for(int x=1;x<=(i-1);x++){
                    cost = Math.max(m[x][j-1],p[i]-p[x]); //若需压缩空间，只需保存第j行和第j-1行的dp数组值即可，其他可迭代
                    if(m[i][j]>cost){
                        m[i][j] = cost;
                        d[i][j] = x;
                    }
                }
            }
        }
        reconstructPartition(d,n,k);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Partition p = new Partition();
        p.read(new Scanner(new FileInputStream("src/main/c/datafiles/partition-data1"), "utf-8"));
        p.partition();
    }

}
