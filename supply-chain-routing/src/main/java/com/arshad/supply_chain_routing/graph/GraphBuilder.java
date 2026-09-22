package com.arshad.supply_chain_routing.graph;

import com.arshad.supply_chain_routing.entity.RouteEdge;
import com.arshad.supply_chain_routing.repository.RouteEdgeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphBuilder {
    private final RouteEdgeRepository routeEdgeRepository;

    public GraphBuilder(RouteEdgeRepository routeEdgeRepository) {
        this.routeEdgeRepository = routeEdgeRepository;
    }

    public Graph buildGraph(){
        Graph g = new Graph();
        List<RouteEdge> routeEdges = routeEdgeRepository.findAll();

        for(RouteEdge routeEdge : routeEdges){
            Long source = routeEdge.getSourceLocation().getId();
            Long destination = routeEdge.getDestinationLocation().getId();
            double distance = routeEdge.getDistance();
            g.addEdge(source , destination , distance);
        }
        return g;
    }

}
