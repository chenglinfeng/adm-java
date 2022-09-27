package com.algorist.datastructure;

public class SetUnion {
    private final int[] p;        /* parent element */
    private final int[] size;           /* number of elements in subtree i */
    private final int n;                /* number of elements in set */

    public SetUnion(int n){
        this.n = n;
        this.p = new int[n+1];
        this.size = new int[n+1];

        for(int i=1;i<=n;i++){
            this.p[i] = i;
            this.size[i] = 1;
        }
    }

    private int find(int x){
        if(p[x]==x){
            return x;
        }else{
            return find(p[x]);
        }
    }

    public void unionSets(int s1, int s2){
        int r1 = find(s1);
        int r2 = find(s2);

        if(r1==r2){
            return;
        }

        if(size[r1]>=size[r2]){
            size[r1] += size[r2];
            p[r2] = r1;
        }else{
            size[r2] += size[r1];
            p[r1] = r2;
        }
    }

    public boolean sameComponent(int s1, int s2){
        return find(s1) == find(s2);
    }

    public void print() {
        for (int i = 1; i <= n; i++)
            System.out.printf("%d  set=%d size=%d %n", i, p[i], size[i]);

        System.out.println();
    }
}
