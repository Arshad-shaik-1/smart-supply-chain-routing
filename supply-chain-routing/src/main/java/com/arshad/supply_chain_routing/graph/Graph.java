package com.arshad.supply_chain_routing.graph;

import java.util.*;

public class Graph {

    private final Map<Long , List<GraphEdge>> adjacencyList = new HashMap<>() ;

    public void addLocation(Long locationId){
        adjacencyList.putIfAbsent(locationId , new ArrayList<>());
    }

    public void addEdge(Long source , Long destination , double distance){
        addLocation(source);
        addLocation(destination);
        adjacencyList.get(source).add(new GraphEdge(destination , distance));
    }
    public Map<Long , List<GraphEdge>> getAdjacencyList() {
        return adjacencyList;
    }
}
