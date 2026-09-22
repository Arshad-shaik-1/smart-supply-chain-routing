package com.arshad.supply_chain_routing.graph;

public class GraphEdge {
    private Long destination;
    private double distance;

    public GraphEdge(Long destination, double distance) {
        this.destination = destination;
        this.distance = distance;
    }

    public Long getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public String toString() {
        return "GraphEdge [destination=" + destination + ", distance=" + distance + "]";
    }
}
