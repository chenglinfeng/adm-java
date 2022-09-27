package com.algorist.backtrack;

public class Backtrack {
    private static final int MAXCANDIDATES = 100;
    private boolean finished = false;

    public <T> void backtrack(int[] a, int k, T input, BacktrackCallback<T> callback){
        if(callback.isaSolution(a,k,input)){
            callback.processSolution(a,k,input);
        }else{
            k++;
            int[] c = new int[MAXCANDIDATES];
            int ncandidates = callback.constructCandidates(a,k,input,c);
            for(int i=0 ; i<ncandidates; i++){
                a[k] = c[i];
                callback.makeMove(a,k,input);
                backtrack(a,k,input,callback);
                if(finished){
                    return;
                }
                callback.unmakeMove(a,k,input);
            }
        }
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
