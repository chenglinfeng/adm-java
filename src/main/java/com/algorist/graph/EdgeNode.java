package com.algorist.graph;

public class EdgeNode {
    private int y;
    private int weight;
    private EdgeNode next;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public EdgeNode getNext() {
        return next;
    }

    public void setNext(EdgeNode next) {
        this.next = next;
    }

    public EdgeNode(int y){
        this.y = y;
    }

    public EdgeNode(int y, int w){
        this.y = y;
        this.weight = w;
    }

    public EdgeNode copy(int v){
        return new EdgeNode(v);
    }

    public EdgeNode copy(int v, int w){
        return new EdgeNode(v,w);
    }
}
