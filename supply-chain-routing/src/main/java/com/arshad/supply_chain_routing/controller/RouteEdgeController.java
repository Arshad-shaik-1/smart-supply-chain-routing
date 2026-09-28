package com.arshad.supply_chain_routing.controller;

import com.arshad.supply_chain_routing.entity.RouteEdge;
import com.arshad.supply_chain_routing.service.RouteEdgeService;
import com.arshad.supply_chain_routing.service.RouteEdgeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteEdgeController {
    private final RouteEdgeServiceImpl routeEdgeService;

    public RouteEdgeController(RouteEdgeServiceImpl routeEdgeService) {
        this.routeEdgeService = routeEdgeService;
    }

    @PostMapping
    public RouteEdge addRouteEdge(@RequestBody RouteEdge routeEdge) {
        return routeEdgeService.addRouteEdge(routeEdge);
    }

    @GetMapping
    public List<RouteEdge> getAllRoutes() {
        return routeEdgeService.getAllRoutes();
    }

    @GetMapping("/{id}")
    public RouteEdge getRouteById(@PathVariable Long id) {
        return routeEdgeService.getRouteById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteRouteById(@PathVariable Long id) {
        routeEdgeService.deleteRouteById(id);
        return "Route edge deleted successfully";
    }

}
