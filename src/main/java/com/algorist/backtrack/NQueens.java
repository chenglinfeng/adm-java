package com.algorist.backtrack;


public class NQueens implements BacktrackCallback<Integer>{
    private int solutionCount;

    NQueens(){
        solutionCount = 0;
    }

    @Override
    public boolean isaSolution(int[] a, int k, Integer input) {
        return k==input;
    }

    @Override
    public void processSolution(int[] a, int k, Integer input) {
        solutionCount++;
    }

    @Override
    public int constructCandidates(int[] a, int k, Integer input, int[] c) {
        boolean legalMove;

        int ncandicates = 0;
        for(int i=1;i<=input;i++){
            legalMove = true;

            for(int j=1;j<k;j++){
                if(Math.abs(k-j)==Math.abs(i-a[j])){
                    legalMove = false;
                }
                if(i==a[j]){
                    legalMove = false;
                }
            }
            if(legalMove){
                c[ncandicates] = i;
                ncandicates++;
            }
        }
        return ncandicates;
    }

    @Override
    public void makeMove(int[] a, int k, Integer input) {

    }

    @Override
    public void unmakeMove(int[] a, int k, Integer input) {

    }

    public void setSolutionCount(int solutionCount) {
        this.solutionCount = solutionCount;
    }

    public int solutionCount() {
        return this.solutionCount;
    }

    public static void main(String[] args) {
        int[] a = new int[100];            /* solution vector */

        for (int i = 1; i <= 10; i++) {
            NQueens nQueens = new NQueens();
            Backtrack backtrack = new Backtrack();
            nQueens.setSolutionCount(0);
            backtrack.backtrack(a, 0, i, nQueens);
            System.out.printf("n=%d  solution_count=%d%n", i, nQueens.solutionCount());
        }
    }
}
