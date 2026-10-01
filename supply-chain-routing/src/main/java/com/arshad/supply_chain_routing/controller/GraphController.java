package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.dto.RouteResponse;
import com.arshad.supply_chain_routing.graph.Graph;
import com.arshad.supply_chain_routing.graph.GraphBuilder;
import com.arshad.supply_chain_routing.graph.DijkstraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin(origins = "http://localhost:5173")
public class GraphController {
    private final GraphBuilder graphBuilder;
    private final DijkstraService dijkstraService;

    public GraphController(GraphBuilder graphBuilder , DijkstraService dijkstraService) {
        this.graphBuilder = graphBuilder;
        this.dijkstraService = dijkstraService;
    }

    @GetMapping
    public Graph getGraph() {
        return graphBuilder.buildGraph();
    }

    @GetMapping("/shortest-path")
    public RouteResponse shortestPath(@RequestParam Long source , @RequestParam Long destination){
        return dijkstraService.findShortestPath(source,destination);
    }
}
